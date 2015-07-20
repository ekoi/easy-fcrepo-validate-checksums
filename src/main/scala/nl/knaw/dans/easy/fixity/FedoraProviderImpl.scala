/**
 * *****************************************************************************
 * Copyright 2015 DANS - Data Archiving and Networked Services
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ****************************************************************************
 */

package nl.knaw.dans.easy.fixity

import scala.util.Try
import java.net.URL
import com.yourmediashelf.fedora.client.FedoraCredentials
import com.yourmediashelf.fedora.client.FedoraClient._
import com.yourmediashelf.fedora.client.request.FedoraRequest
import com.yourmediashelf.fedora.client.FedoraClient
import scala.collection.JavaConversions._
import com.yourmediashelf.fedora.client.request.FindObjects

case class FedoraProviderImpl(server: URL, user: String, password: String) extends FedoraProvider {
  val creds = new FedoraCredentials(server, user, password)
  val client = new FedoraClient(creds)
  FedoraRequest.setDefaultClient(client)

  case class PidIterator(namespace: String) extends Iterator[String] {
    var hasNextBatch = true
    var token: String = null
    var batch: Seq[String] = null
    var i = 0

    override def hasNext: Boolean =
      nextFromBatch match {
        case None => nextBatch match {
          case None => false
          case Some(b) => batch = b; true
        }
        case Some(_) => true
      }

    override def next() = {
      hasNext match {
        case true => nextFromBatch.get
        case false => throw new NoSuchElementException
      }
    }

    def nextFromBatch: Option[String] =
      if (i >= batch.size) None
      else Some(batch(i))

    def nextBatch: Option[Seq[String]] = 
      if (hasNextBatch) {
        var search = findObjects().pid().query(s"pid~$namespace:*")
        if (token != null && !token.isEmpty) search = search.sessionToken(token)
        val response = search.execute()
        hasNextBatch = response.hasNext
        token = response.getToken
        if (response.getPids.isEmpty()) None
        else Some(response.getPids)
      } else None
  }

  def iterator(namespace: String): Iterator[String] = new PidIterator(namespace)

  def validateChecksum(doId: String, dsId: String): Try[Boolean] = {
    Try {
      getDatastream(doId, dsId)
        .format("xml")
        .validateChecksum(true).execute()
        .getDatastreamProfile
        .getDsChecksumValid
        .toBoolean
    }
  }
}
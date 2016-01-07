/**
 * Copyright (C) 2015-2016 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.easy.valchecksum

import com.yourmediashelf.fedora.client.FedoraClient._
import scala.collection.JavaConversions._

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

  override def next() =
    hasNext match {
      case true => 
        val n = nextFromBatch.get
        i += 1
        n
      case false => throw new NoSuchElementException
    }

  def nextFromBatch: Option[String] =
    if (batch == null || i >= batch.size) None
    else Some(batch(i))

  def nextBatch: Option[Seq[String]] =
    if (hasNextBatch) {
      i = 0
      var search = findObjects().pid().query(s"pid~$namespace:*")
      if (token != null && token.nonEmpty) search = search.sessionToken(token)
      val response = search.execute()
      hasNextBatch = response.hasNext
      token = response.getToken
      if (response.getPids.isEmpty()) None
      else Some(response.getPids)
    } else None
}
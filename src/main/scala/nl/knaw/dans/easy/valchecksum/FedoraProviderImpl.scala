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

import java.net.URL

import com.yourmediashelf.fedora.client.FedoraClient._
import com.yourmediashelf.fedora.client.request.FedoraRequest
import com.yourmediashelf.fedora.client.{FedoraClient, FedoraCredentials}

import scala.util.Try

class FedoraProviderImpl(server: URL, user: String, password: String) extends FedoraProvider {
  val creds = new FedoraCredentials(server, user, password)
  val client = new FedoraClient(creds)
  FedoraRequest.setDefaultClient(client)

  override def iterator(namespace: String): Iterator[String] = new PidIterator(namespace)

  override def getControlGroup(pid: String, dsId: String): Try[Char] =
    Try {
      getDatastream(pid, dsId)
        .format("xml")
        .execute()
        .getDatastreamProfile
        .getDsControlGroup.charAt(0)
  }

  override def validateChecksum(pid: String, dsId: String): Try[Boolean] =
  Try {
      getDatastream(pid, dsId)
        .format("xml")
        .validateChecksum(true)
        .execute()
        .getDatastreamProfile
        .getDsChecksumValid
        .toBoolean
    }

  override def logMessage(pid: String, dsId: String, msg: String): Try[Unit] =
    Try {
      modifyDatastream(pid, dsId)
        .ignoreContent(true)
        .logMessage(msg)
        .execute()
    }
}
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

import java.net.URL
import com.yourmediashelf.fedora.client.FedoraCredentials
import scala.util.Success
import org.slf4j.LoggerFactory
import scala.util.Failure

case class NamespaceSpec(name: String, dsIds: List[String])

/**
 * Implements the application logic
 */
case class FixityClient(
  fedora: FedoraProvider,
  namespaces: List[NamespaceSpec],
  logFormat: String,
  delay: Int) {

  val log = LoggerFactory.getLogger(getClass)

  def run(): Unit =
    namespaces.map(validateNamespace)

  def validateNamespace(namespace: NamespaceSpec): Unit = {
    val iter = fedora.iterator(namespace.name)
    while (iter.hasNext)
      validateDigitalObject(iter.next(), namespace)
  }

  def validateDigitalObject(pid: String, namespace: NamespaceSpec): Unit = {
    namespace.dsIds.map(validateDatastream(pid))
  }

  def validateDatastream(pid: String)(dsId: String): Unit =
    fedora.validateChecksum(pid, dsId) match {
      case Success(valid) =>
        if (valid) log.info(s"Checksum valid. pid = $pid, dsId = $dsId")
        else log.error(s"Checksum INVALID. pid = $pid, dsId = $dsId")
      case Failure(e) => log.warn("Could not validate checksum. pid = $pid, dsId = $dsId")
    }

}
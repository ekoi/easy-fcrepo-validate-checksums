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
import scala.util.Try

case class NamespaceSpec(name: String, dsIds: List[String])

/**
 * Implements the application logic.
 */
class FixityClient(
  fedora: FedoraProvider,
  namespaces: List[NamespaceSpec],
  logResult: Boolean,
  skipExternalDatastreams: Boolean,
  delay: Int) {

  val log = LoggerFactory.getLogger(getClass)

  def run(): Unit =
    namespaces.foreach(validateNamespace)

  def validateNamespace(namespace: NamespaceSpec): Unit =
    fedora
      .iterator(namespace.name)
      .foreach(validateDigitalObject(_, namespace))

  def validateDigitalObject(pid: String, namespace: NamespaceSpec): Unit =
    namespace.dsIds.foreach(validateDatastream(pid))

  def validateDatastream(pid: String)(dsId: String): Unit =
    mustSkipStream(pid, dsId) match {
      case Success(true) => log.info(s"Skipping external datastream $pid/$dsId")
      case Success(false) => validateDatastreamWithDelay(pid, dsId)
      case Failure(e) => log.error(s"Could not get control group of $pid/$dsId: ${e.getMessage}")
    }

  def mustSkipStream(pid: String, dsId: String): Try[Boolean] =
    if (skipExternalDatastreams)
      fedora.getControlGroup(pid, dsId).map(cg => List('R', 'E').contains(cg))
    else Success(false)

  def validateDatastreamWithDelay(pid: String, dsId: String): Unit =
    {
      if (delay > 0) Thread.sleep(delay)
      fedora.validateChecksum(pid, dsId) match {
        case Success(true) =>
          log.info(s"Checksum OK for $pid/$dsId")
          if (logResult) fedora.logMessage(pid, dsId, "Validated checksum: OK")
        case Success(false) =>
          /*
           * We do NOT log a message in Fedora here, as this would update the checksum and make it valid
           * again. Instead, we rely on the error logging configuration to notify the responsible staff.
           */
          log.error(s"Checksum INVALID for $pid/$dsId")
        case Failure(e) => log.warn(s"COULD NOT VALIDATE checksum for $pid/$dsId: ${e.getMessage}")
      }
    }
}
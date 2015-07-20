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

import org.rogach.scallop.ScallopConf
import java.io.File
import java.net.URL

class Conf(args: Seq[String]) extends ScallopConf(args) {
  printedName = "easy-fcrepo-fixity-checker"
  version(s"$printedName ${Version()}")
  banner("""
            | Check the fixity of a Fedora Commons Repository's datastreams.
            |
            | Usage: easy-fcrepo-fixity-checker 
            |              [-f <fcrepo-server>] \
            |              [-u <fcrepo-user> \
            |               -p <fcrepo-password>] \
            |              [-n <namespace-list>] \
            |              [-d <datastream-id-list>] \
            |              [-l <log-message-format-string>] \
            |              [-m <milliseconds-between-calls>]
            | Options:
            |""".stripMargin)
  val fedora = ???
  val user = ???
  val password = ???
  val namespaces = ???
  val datastreamIds = ???
  val logFormat = ???
  val delay = ???
} 
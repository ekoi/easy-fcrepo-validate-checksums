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

import java.io.File
import java.net.URL

import org.apache.commons.configuration.PropertiesConfiguration
import org.rogach.scallop.ScallopConf

class Conf(args: Seq[String]) extends ScallopConf(args) {
  val props = new PropertiesConfiguration(new File(homedir, "cfg/application.properties"))
  printedName = "easy-fcrepo-validate-checksums"
  version(s"$printedName ${Version()}")
  banner(s"""
            | Validate the checksums of a Fedora Commons Repository's datastreams.
            |
            | Usage: $printedName
            |              [-f <fcrepo-server>] \\
            |              [-u <fcrepo-user> \\
            |               -p <fcrepo-password>] \\
            |              [-l][-s] \\
            |              [-t <time-between-calls>]
            | Options:
            |""".stripMargin)
  val fedora = opt[URL]("fcrepo-server", short = 'f', default = Some(new URL(props.getString("default.fcrepo-server"))))
  val user = opt[String]("fcrepo-user", short = 'u', default = Some(props.getString("default.fcrepo-user")))
  val password = opt[String]("fcrepo-password", short = 'p', default = Some(props.getString("default.fcrepo-password")))
  val logResult = opt[Boolean]("log-result", short = 'l', default = Some(props.getBoolean("default.log-result")))
  val skipExternalDatastreams = opt[Boolean]("skip-external-datastreams", short = 's', default = Some(props.getBoolean("default.skip-external-datastreams")))
  val delay = opt[Int]("time-between-calls", short = 't', default = Some(props.getInt("default.time-between-calls")))
} 
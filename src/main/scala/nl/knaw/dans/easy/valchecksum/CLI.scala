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

package nl.knaw.dans.easy.valchecksum

import java.io.File

import org.apache.commons.configuration.PropertiesConfiguration

import scala.collection.JavaConversions._

object CLI {
  def main(args: Array[String]): Unit = {
    val conf = new Conf(args)
    val fedora = new FedoraProviderImpl(conf.fedora(), conf.user(), conf.password())
    val fc = new FixityClient(fedora, getNamespaceSpecs, conf.logResult(), conf.skipExternalDatastreams(), conf.delay())
    fc.run()
  }

  private def getNamespaceSpecs: List[NamespaceSpec] = {
    val props = new PropertiesConfiguration(new File(homedir, "cfg/validation.properties"))
    props.getKeys.map(ns => NamespaceSpec(ns, props.getStringArray(ns).toList.map(getDatastreamSpec))).toList
  }

  private def getDatastreamSpec(s: String): DatastreamSpec =
    if(s.endsWith("*")) DatastreamSpec(s.take(s.length - 1), true)
    else  DatastreamSpec(s, false)
}
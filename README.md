easy-fcrepo-validate-checkums
=============================
[![Build Status](https://travis-ci.org/DANS-KNAW/easy-fcrepo-validate-checksums.svg?branch=master)](https://travis-ci.org/DANS-KNAW/easy-fcrepo-validate-checksums)

Validate the checksums of a Fedora Commons Repository's datastreams.


SYNOPSIS
--------

    easy-fcrepo-validate-checkums \
       [-f <fcrepo-server>] \
       [-u <fcrepo-user> \
        -p <fcrepo-password> ] \
       [-l][-s] \
       [-t <time-between-calls>] \


DESCRIPTION
-----------

Command that validates the checksums of datastreams in a Fedora Commons 3.x repository. It iterates over all the digital 
objects in the namespaces configured in ``validation.properties`` and checks the datastreams configured for each namespace 
(see section [Configuration](#configuration) below). The validation is performed using the Fedora [getDatastream] method,
specifying ``validateChecksum = true``. Successful validation can optionally be recorded in Fedora's audit log. Since the
action of recording a log message causes Fedora to update the datastream's checksum, an invalid checksum will *not* be
recorded. It will however be written to the error log. It is of course advisable to configure the smtp appender of the 
logger configuration to send an e-mail to responsible staff to warn them about the checksum mismatch.


ARGUMENTS
---------

    -p, --fcrepo-password  <arg>       (default = fedoraAdmin)
    -f, --fcrepo-server  <arg>         (default = http://localhost:8080/fedora)
    -u, --fcrepo-user  <arg>           (default = fedoraAdmin)
    -l, --log-result
    -s, --skip-external-datastreams
    -t, --time-between-calls  <arg>    (default = 0)
        --help                        Show help message
        --version                     Show version of this program


INSTALLATION AND CONFIGURATION
------------------------------

### Installation steps:

1. Unzip the tarball to a directory of your choice, e.g. /opt/
2. A new directory called easy-fcrepo-validate-checksums-<version> will be created
3. Create an environment variabele ``EASY_FCREPO_VALIDATE_CHECKSUMS_HOME`` with the directory from step 2 as its value


### Configuration

General configuration settings can be set in ``$EASY_FCREPO_VALIDATE_CHECKSUMS_HOME/cfg/application.properties`` 
and logging can be configured in ``EASY_FCREPO_VALIDATE_CHECKSUMS_HOME/cfg/logback.xml``. The available settings are 
explained in comments in aforementioned files.

The datastreams that must be validated must be configured in the ``EASY_FCREPO_VALIDATE_CHECKSUMS_HOME/cfg/validation.properties``.
The format of this file is as follows:

      <namespace-id>=<datastream-id-list>
      
in which ``<namespace-id>`` is the namespace to validate and ``datastream-id-list`` is a comma separated list of datastreams
to check in that namespace.


BUILDING FROM SOURCE
--------------------

Prerequisites:

* Java 8 or higher
* Maven 3.3.3 or higher
 
Steps:

        git clone https://github.com/DANS-KNAW/easy-fcrepo-validate-checksums.git
        cd easy-fcrepo-validate-checksums
        mvn install
        
[getDatastream]: https://wiki.duraspace.org/display/FEDORA38/REST+API#RESTAPI-getDatastream

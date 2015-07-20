*Note: this project is in pre-alpha state, so below instructions may not work completely yet*

easy-fcrepo-fixity-checker
==========================


SYNOPSIS
--------

    easy-fcrepo-fixity-checker


DESCRIPTION
-----------



ARGUMENTS
---------


INSTALLATION AND CONFIGURATION
------------------------------

### Installation steps:

1. Unzip the tarball to a directory of your choice, e.g. /opt/
2. A new directory called easy-fcrepo-fixity-checker-<version> will be created
3. Create an environment variabele ``EASY_FCREPO_FIXITY_CHECKER_HOME`` with the directory from step 2 as its value


### Configuration

General configuration settings can be set in ``$EASY_FCREPO_FIXITY_CHECKER_HOME/cfg/appliation.properties`` 
and logging can be configured in ``$EASY_FCREPO_FIXITY_CHEKCER_HOME/cfg/logback.xml``. The available settings are 
explained in comments in aforementioned files.


BUILDING FROM SOURCE
--------------------

Prerequisites:

* Java 7 or higher
* Maven 3.3.3 or higher
 
Steps:

1. Clone and build the [dans-parent] project (*can be skipped if you have access to the DANS maven repository*)
      
        git clone https://github.com/DANS-KNAW/dans-parent.git
        cd dans-parent
        mvn install
2. Clone and build this project

        git clone https://github.com/DANS-KNAW/easy-fcrepo-fixity-checker.git
        cd easy-fcrepo-fixity-checker
        mvn install
        

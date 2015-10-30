                  ********** TendereteOnline **********

Environment variables
---------------------

This is an excerpt of a "~/.bash_profile" file for Unix-like systems. Adapt to
your environment.

# J2SE
export JAVA_HOME=/opt/jdk1.7.0_05
# For Mac OS X, use:
#export JAVA_HOME=/Library/Java/JavaVirtualMachines/1.7.0.jdk/Contents/Home
# For convenience.
PATH=$JAVA_HOME/bin:$PATH

# Maven
MAVEN_HOME=/opt/apache-maven-3.0.4
PATH=$MAVEN_HOME/bin:$PATH
export MAVEN_OPTS="-Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m"

# MySQL.
MYSQL_HOME=/opt/mysql-5.5.25a-linux2.6-i686
PATH=$MYSQL_HOME/bin:$PATH

# Tomcat.
export CATALINA_OPTS=$MAVEN_OPTS

# Eclipse.
PATH=/opt/eclipse:$PATH

In MS-Windows define the following environment variables by using the control
panel (System):

JAVA_HOME=c:\jdk1.7.0_05
PATH=%JAVA_HOME%\bin;%PATH%

MAVEN_HOME=c:\apache-maven-3.0.4
PATH=%MAVEN_HOME%\bin;%PATH%
MAVEN_OPTS=-Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m

MYSQL_HOME=c:\MySQLServer5.5
PATH=%MYSQL_HOME%\bin;%PATH%

CATALINA_OPTS=%MAVEN_OPTS%

PATH=c:\eclipse;%PATH%

Basic installation and usage of MySQL Community Edition
-------------------------------------------------------

+ Installation of MySQL 5.5.25a on Unix-like operating systems:

  We usually install MySQL as root user and run the server as normal user.
  As "root" user, proceed as follows (example for Linux):

   - Unpack mysql-5.5.25a-linux2.6-i686.tar.gz on some directory
     (e.g. /usr/local).
   - chown -R root:root mysql-5.5.25a-linux2.6-i686
     NOTE: For Mac OS X, use "root:wheel" instead of "root:root" with "chmod".
   - chmod -R 755 mysql-5.5.25a-linux2.6-i686
   - Create the symbolic link "/usr/local/mysql" pointing to the directory
     where MySQL is installed:
     ln -s `pwd`/mysql-5.5.25a-linux2.6-i686 /usr/local/mysql

   - In ubuntu OS, you must install libio1 library
     sudo apt-get install libaio1

   As normal user, proceed as follows:

   - Create a directory where MySQL will store its databases. For example:
     /home/user/.MySQLData.
   - Create $HOME/.my.cnf file with a similar content to the following:

     [mysqld]
     datadir=/home/user/.MySQLData

     Change the value of "datadir" to the directory created previously.

   - cd /usr/local/mysql
   - scripts/mysql_install_db

     This will create "mysql" and "test" databases in the directory specified
     by the previous "datadir" option.

+ Installation of MySQL 5.5.25a on MS-Windows operating systems:

   - Double-click on mysql-5.5.25a-winx64.msi. Use default options.
   - After the installation of MySQL, the MySQL Server Instance Configuration
     Wizard will run. Use default options but the following ones:

     + Best Support For Multilingualism.
     + Uncheck "Install As Windows Service" and check "Install Bin Directory
       In Windows PATH".

Create two databases
--------------------

In order to minimize changes to pojo-examples default configuration, create two
databases on MySQL with names "pojo" and "pojotest", and a user with name
"pojo" and password "pojo". In the case of a Unix-like operating system, run
the following commands as normal user.

+ Start the MySQL server.

  - Unix-like operating systems: mysqld
  - MS-Windows operating systems: mysqld

- Create two databases with names "pojo" and "pojotest". "pojo" is used to run
  applications and "pojotest" for the Maven test phase.

  mysqladmin -u root create pojo
  mysqladmin -u root create pojotest

- Create a user with name "pojo" and password "pojo", and allow her/him to
  access both databases from local host.

  mysql -u root

  GRANT ALL PRIVILEGES ON pojo.* to pojo@localhost IDENTIFIED BY 'pojo';
  GRANT ALL PRIVILEGES ON pojotest.* to pojo@localhost IDENTIFIED BY 'pojo';

- Try to access both databases as user "pojo" with password "pojo".

  mysql -u pojo --password=pojo pojo
  mysql -u pojo --password=pojo pojotest

- Shutdown the MySQL server.

  mysqladmin -u root shutdown

Initializing the database and building the examples
---------------------------------------------------

- mvn sql:execute install

Installation and configuration of Tomcat
----------------------------------------

- Unpack apache-tomcat-7.0.29.tar.gz.

- Copy the JDBC driver to Tomcat's "lib" directory.

  * MySQL JDBC driver:
    M2_REPO/repository/mysql/mysql-connector-java/5.1.21/mysql-connector-java-5.1.21.jar

    M2_REPO is the location of the local Maven repository (by default,
    $HOME/.m2 in Unix-like operating systems,
    C:\Documents and Settings\<loginName>\.m2 in XP and
    C:\Users\<loginName>\.m2 in Vista/7).

- Define a global DataSource named "jdbc/pojo-examples-ds".

  * Add the following lines to conf/server.xml, inside the
    "<GlobalNamingResources>" tag.

    <!-- MySQL -->
    <Resource name="jdbc/pojo-examples-ds"
              auth="Container"
              type="javax.sql.DataSource"
              driverClassName="com.mysql.jdbc.Driver"
              url="jdbc:mysql://localhost/pojo"
              username="pojo"
              password="pojo"
              maxActive="4"
              maxIdle="2"
              maxWait="10000"
              removeAbandoned="true"
              removeAbandonedTimeout="60"
              logAbandoned="true"
              validationQuery="SELECT COUNT(*) FROM PingTable"/>

  * Add the following lines to conf/context.xml, inside the <Context> tag.

    <ResourceLink name="jdbc/pojo-examples-ds" global="jdbc/pojo-examples-ds"
                  type="javax.sql.DataSource"/>


Running Web appplications
-------------------------

pojo-servjsptutorial, pojo-tapestrytutorial, and pojo-minibank provide Web 
applications. pojo-minibank requires the database server to be running.

- Running with Maven/Jetty.

Below it is indicated how to run pojo-minibank. Rest of Web applications are
run in a similar way.

cd pojo-minibank
mvn jetty:run

Browse to http://localhost:9090/pojo-minibank

+ Running with Tomcat.

    * Copy the ".war" file (e.g. pojo-minibank/target/pojo-minibank.war) to
      Tomcat's "webapp" directory.

    * Start Tomcat:
      cd <<Tomcat home>>/bin
      startup.sh

    * Browse to http://localhost:8080/pojo-minibank.

    * Shutdown Tomcat:
      shutdown.sh

Configuring Eclipse
-------------------

- "Preferences" wizard is available under "Window" menu option on Windows and
   Linux, and "Eclipse" menu option under Mac OS X.

- Use Java 1.7:
  * Go to "Preferences>Java>Compiler" and select "1.7" for "Compiler
    compliance level".
  * Go to "Preferences>Java>Installed JREs" and select the JVM 1.7.0.

- Set the default text encoding for Eclipse to UTF-8
  Go to "Preferences>General>Workspace" and select UTF-8 as the Text
  File Encoding. This should set the default encoding for the resources in
  your workspace.

- Set the default text encoding for Java properties files to UTF-8
  Go to "Preferences>General>Content Types>Text>Java Properties File",
  set "Default encoding" to "UTF-8" and click "Update".
  
- Set the XML colorer for Tapestry page template files
  Go to "Preferences>General>Content Types" and add the "*.tml" file
  extension to the "Text / XML" content type.
  
- Install the Subclipse plugin (it provides support for Subversion within
  Eclipse)
  Go to "Help>Install New Software...".
    Use the following site: http://subclipse.tigris.org/update_1.8.x
    Check all the items, accept the license and finish the installation.
	
  For OSs where JavaHL (JNI) is not available (e.g. Linux OS and Mac OS X):
    Configuration: Go to "Preferences>Team>SVN>SVN interface" and
      set "Client" to "SVNKit (Pure Java)".
	
- Install the Maven SCM handler for Subclipse compliant with Subclipse 1.8.x	
  Go to "Help>Install New Software...".
    Use the following site: http://subclipse.tigris.org/m2eclipse/1.0
    Check all the items, accept the license and finish the installation.

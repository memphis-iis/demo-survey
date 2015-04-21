demo-survey
================

Simple Java web survey demo for the AWS platform

Be sure to look in `Project Reports` below for the JavaDocs!

TODO: Links to meep-meep documents

TODO: Break into separate documents

Introduction and Requirements
------------------------------

This is a sample project meant to demonstrate how you might create a small
data collection website. In this case it's a very simple survey. No one
logs in, everyone supplies their own participant code, and the questions
are silly. We only have a few high-level requirements:

  1. This project will be in Java, since that's a very common development
     language around here
  2. We should save survey results
  3. We want our code to understand records with keys in some sense
  4. We want to run this application in the "cloud" at Amazon Web Services (AWS)

We will be making some assumptions about your development workstation

  * You have Git installed (which includes Git Bash on Windows)
  * You have the Java 7 (or later) **JDK** installed
  * You have Apache Maven installed.
  * You have a functional AWS account to use for deployment (at least for
    testing). If you have an account with Amazon, then you can use it for AWS.
  * You have installed the "local" version of Amazon DynamoDB. See below for
    more instructions.

All the major Linux distributions have packages for a Java JDK and Maven. You
should be able to install Maven via homebrew on a Mac. On a Windows machine,
you'll probably need to download and install them yourself. They can be found.
Your best bet is probably to Google for them, but as of this writing, they could
downloaded here:

  * [Java 7 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
  * [Maven](https://maven.apache.org/download.cgi)

If you use the manual downloads, you'll also want to make sure that the bin
directories from both the JDK and the Maven distribution are in your PATH.

A Brief Overview of Java Web Applications
------------------------------------------

It is assumed that you already understand Java programming and how Java web
applications work. However, just in case that's not true, let's go over how
a Java application is put together.

A Java web application is compiled into a WAR file, which is just a special
kind of JAR. It represents a web application that will be executed by a Java
web server. This server can be a simple Servlet/JSP container (like Tomcat or
Jetty), or it can be a full-fledged application server (like Wild Fly or
GlassFish). In our case it will be Tomcat.

Each WAR file is loaded as a web application (or context) and responds to
HTTP requests using Servlets. Servlets are generally classes that implement
a special interface. Additionally, Java ServerPages (JSP's) can use a
combination of HTML, Java code, and special "tags" to create HTML output.

The WAR file includes (amoung other things) any JSP's and other content to
be served to the user and a special directory named WEB-INF. Inside WEB-INF
is a file named web.xml that defined various parameters for the web application,
a classes folder with your compiled Java classes, and a lib folder with any
JAR's that your web application will need. You may include your own file in
WEB-INF, but remember that they will not be immediately accessible to your
users. You must write code of some kind to expose any resources in WEB-INF that
your users should be able to download.

In general, a Java web server maps each "directory" at the root of the URL
to a WAR file, and the WAR file is responsible for handling URL's in that
namespace. For instance, this application will generate a WAR file named
demosurvey.WAR. It includes a Servlet named HomeServlet (see
edu.memphis.iis.demosurvey.HomeServlet) that is mapped to `/home`. As a
result, if we are deployed on a server at `www.somewhere.com` the URL
`http://www.somewhere.com/demosurvey/home` will map to our servlet.

A Quick Introduction to Maven
------------------------------

This is not an extensive introduction to Maven. In fact, you should already
by somewhat familiar with Maven (or at least have read some introductory
material); however, in the interest of completeness, a brief overview of
Maven will be given.

Maven is a build tool with extensive support for transitive dependencies.
For instance, if you declare a dependence on library A, which requireds
both libraries B and C, each of which required D, then Maven will
automatically download all four libraries (A, B, C, and D) for your project.
You may specify when you "need" your dependencies (only to compile, only
for testing, etc).

**Important!** If you take nothing else away from this section, remember
dependency management. It's important, but even if you disagree, please
keep in mind that the Java community has been using automated dependency
management for years. As a result, few projects worry about how difficult
it will be for you to find all the necessary JAR's to run their code. They
assume that a dependency manager will do this for you.

Maven is based around the concept of a "Project Object Model", abbreviated
by POM. You describe a project with a POM in an XML file named pom.xml
and let Maven take care of taks like downloading your dependencies, building
your WAR (or JAR) file, running your unit tests, etc. For instance, just
running `mvn package` will compile, test, and package your code. In our
project, that means that you'll be able to find `demosurvey.war` in the
`target` directory.

Maven is based on convention over configuration, so you should follow the
Maven conventions. A few to keep in mind:

  * Your code, resource files, etc will live in the `src` directory
  * Everything Maven builds for you will end up in the `target` directory.
    DO **NOT** add the `target` directory to source code control (you'll
    notice that it is in the `.gitignore` file in our directory).
  * Your Java code goes in `src/main/java`
  * Any resource files (like configuration files you bundled in your WAR)
    go in `src/main/resources`
  * The "special" contents of your WAR (like JSP's and your WEB-INF directory)
    go in `src/main/webapp`
  * You Java test code goes in `src/test/java`
  * Any resources you need to run unit tests go in `src/test/resources`
  * If you're using the Maven site command to build an informational site
    for your project, the site.xml file and any extra content go in
    `src/site`

Maven also has a large plugin ecosystem for adding all kinds of functionality
to your project. For instance, this project uses the Tomcat plugin. As a
result, you can run this project in a special test Tomcat instance on your
workstation just by running `mvn tomcat7:run`. However, please "Setting
Up Your Development Workstation" below for real details.

Setting up your development workstation
----------------------------------------

As mentioned above you should have Git, a Java JDK (at least version 7), and
Maven installed. You will probably want a nice IDE for development like Eclipse
or NetBeans. A brief description of setting up an Eclipse development environment
will be given, but all the "raw" Maven commands will be our focus.

In addition to the standard development tools listed above, you'll also want to
be able to test the application locally when you make changes. Otherwise you
would need to deploy your application to the Amazon cloud every time you wanted
to test. We use the Maven Tomcat plugin for hosting our web application, but the
application needs a data source. In the Amazon cloud, data is stored in DynamoDB.
Luckily Amazon provides "local" version for testing that emulates enough real
functionality for testing. To setup "DynamoDB Local" you can just follow the
directions at http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Tools.DynamoDBLocal.html

Basically, you just need to:

  * Download the .zip or .tar.gz archive
  * Extract everything somewhere appropriate
  * Run the server whenever you are testing. Below this directory will be
    referred to as the "DynamoDB directory"

That last step is the doozy: if you aren't running the local DynamoDB server,
the web application will have errors when your are running in on your local
workstation. You can still write code, execute the unit tests, push your
changes to a repository, etc, but you won't be able to actually run the
entire web application if you aren't running DynamoDB Local.

Unfortunately there isn't a simple script file (BAT file for Windows people) in
the distributed files for running the server. You can create your own and just
add the line:
`java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar`.
Then when you want to run the server, you can open your terminal (command
prompt for Windows people), `cd` to your DynamoDB directory, and run
your script file.

You might also want to download a tool like the SQLite Browser available
at http://sqlitebrowser.org/. DynamoDB Local uses SQLite to store data,
so while you are testing you can just use this handy browser to view the
data in the file `TEST_us-east-1.db` in your DynamoDB directory.

Assuming that you have already above setup and your DynamoDB Local is running,
you're ready to get the latest code, build the web application, and test.
To do all this you only need three commands:

```
$ git clone https://github.com/memphis-iis/demo-survey.git
$ cd demo-survey
$ mvn tomcat7:run
```

Deploying the Documentation
------------------------------

To provide a complete example, this project uses a somewhat advanced technique.
Maven is used to generate a local site, and that site is pushed to a special
branch on GitHub where it is served as a GitHub Pages Project Page. In fact,
that's what you're reading now.

TODO: Maven site details

TODO: GitHub pages overview

TODO: how the script works


Deploying to AWS
------------------

TODO: one-time setup - IAM role, DynamoDB role add, environment setup

TODO: deploying latest build to AWS

TODO: settings to consider in environment (T2.micro, mem settings)

TODO: AWS step-by-step video


Design Walkthru
-----------------

TODO: document with Configuration, Data Overview, Logging, and Testing

Configuration
--------------

TODO: (dev/test and production)
TODO: IoC and why not used

Data Overview (including the model and DAO classes)
-----------------------------------------------------------

TODO: DynamoDB (and see also for local setup above)

TODO: Model class and attributes used

TODO: DAO class and how used

Logging
--------

TODO: basic logging, logback, and Beanstalk's change to Tomcat

Testing
--------

TODO: unit testing

demo-survey
================

This is a sample app demonstrating how to use Java and Amazon Web Services
to create a web form whose results are saved to a database.

In addition to this overview, there is also a
[brief design walkthru](design.html). You should also look in
[Project Reports](project-reports.html) for the JavaDocs and other goodies!

TODO: Links to meep-meep documents
TODO: link to videos (overview and AWS setup)
TODO: Break into separate documents


Introduction and Requirements
------------------------------

This is a sample project meant to demonstrate how you might create a small
data collection website. In this case it's a very simple survey. No one
logs in, everyone supplies their own participant code, and the questions
are silly.

Since we are demonstrating a web-form whose results are saved to a database,
we should define some terms. When you store a piece of data (whether it's
a checking account entry, a student's grade, or a set of responses to a survey)
it it called a *record*. Each individual feature of the data (name, grade,
withdrawal amount, shoe size, etc) is a *field*. Often there is a field that
whose content uniquely identifies each record, it is called a *key*. For
instance, when your bank gives you an account number, that field is almost
certainly the key used to locate your account's record.

We only have a few high-level requirements:

  1. This project will be in Java, since that's a very common development
     language around here
  2. We should save survey results
  3. We want to demonstrate saving each record with a key
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
For instance, if you declare a dependence on library A, which requires
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

Maven provides the `site` goal, which automatically generates a project site
that reports all the various project data contained in the `pom.xml` file.
It also allows additional pages to be added to the site. The site configuration
file (`site.xml`) and the additional pages are all located in the `src/site`
directory.

GitHub provides a way of hosting pages for each project. The web pages are
committed to a special branch named gh-pages. They are then displayed at a
special URL based on the GitHub organization/user and the project name. In
our case, we just upload our static site as generated by Maven. You may also
use GitHub's jekyll processing to create a site that is pre-rendered for you.
If you're interested, you can check out our meep-meep project for an example
of using this extra functionality.

The script won't be described in detail at this time since it's a fairly
advanced example of git usage. However, we'll quickly explain the basic
logic of the script. Please keep in mind that the following points may not
make much sense unless you've a fairly advanced git user. They are here to
demonstrate how much you can automate with git, a build system like Maven,
and a little scripting.

  1. After some housekeeping, the main branch ("master") is checked out and
     any changes in the repository are pulled. Then same is done for the
     target branch ("gh-pages"), and then we switch back to master. All of
     this is to ensure that we our eventual push will succeed and that some
     of the helpful things that git does for us on checkout are already handled
  2. We use Maven to generate the site files. Note that we also check to make
     sure that our files are there.
  3. We change git's symbolic ref HEAD to point to gh-pages and reset the current
     working tree to be the generated site directory (`target/site`)
  4. We (recursively) add the all the generated files in the generated site
     directory, commit the changes, and push them to GitHub.
  5. We reset the symbolic ref HEAD and reset the working directory


Deploying to AWS
------------------

Currently, all of this is documented in our AWS setup video. However, in the
interest of completeness we'll note some things to remember:

If you're using an account that has never used Elastic Beanstalk
before you can just use the demo setup.
**Be sure to select a Tomcat application and change the default to Tomcat 7**
However, you'll still need to add DynamoDB security to the IAM role.

 * You will need an application environment. You want a Tomcat environment for
   Tomcat 7. This is mainly what is created for you if you use the first-time
   demo setup. Unless money is no object, you should also select the "Single
   Instance" environment type.

 * You will need to create an IAM role or select an existing IAM role. If you
   are using the first-time demo setup, one will be created and selected for you
   automatically.

 * You will need to add the AmazonDynamoDBFullAccess policy to the IAM role
   from above. Note that you'll need to do this regardless of whether or not
   you are using the first-time demo setup.

 * The WAR file you need to upload will be in the `target` directory after
   you run `mvn package`

 * There are some settings that you should probably change (although these are
   entirely optional):
    - Change the instance type from the default of T1.micro to T2.micro. The T2
      version has more RAM, performs slightly better, and costs less (!!!)
    - You can change the environment settings to allow more RAM for the Tomcat
      process. If you are using T2.micro as above, you can set the initial JVM
      heap size to "512m" and the Maximum JVM heap size to "800m"

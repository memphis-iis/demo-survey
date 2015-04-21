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

TODO: quick overview of a Java web app

TODO: quick Maven overview (and how it works with a web app)

Setting up your development workstation
----------------------------------------

TODO: Java 7 JDK and Maven

TODO: Eclipse and Maven builds

TODO: Windows and Git Bash

TODO: (and don't forget DynamoDB local with see also for overview below)

TODO: You can examine your DynamoDB local database with sqlite browser

Configuration
--------------

TODO: (dev/test and production)
TODO: IoC and why not used

Data Overview (including the model and DAO classes)
-----------------------------------------------------------

TODO: DynamoDB (and see also for local setup above)

TODO: Model class and attributes used

TODO: DAO class and how used 

Design
--------

TODO: Servlet's and "home-grown" MVC

Logging
--------

TODO: basic logging, logback, and Beanstalk's change to Tomcat

Testing
--------

TODO: unit testing

Deploying
----------

TODO: running for "real" on AWS: IAM role, DynamoDB role add, deploy application, settings to consider

TODO: AWS step-by-step video

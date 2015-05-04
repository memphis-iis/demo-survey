Design Walkthrough
==================

You can get a general overview of this project, what is required to run it,
and how to set it up in
[Introduction and Overview](index.html).

However, just knowing that this a Java web application built with Maven doesn't
necessarily tell you or why the application is structured in one way or another.
Let's take a second and cover some of the major design points.

MVC
---

This application is designed around a very lightweight implementation of the
[Model-View-Controller] (http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller)
pattern (also know by the initialism MVC . In essence, we build a "model"
representing the entities our software is really interested in (in this case,
a survey) and various "views" that we present to the user. Standing between
these two pieces of functionality are our "controllers".

In a web application built with an MVC pattern, you will generally see something
that maps the URL's of incoming requests to a piece of code. That controller
looks at the request, does something that generally involves the model, and then
chooses the view to render to the user.

In this application, we use Servlet annotations to map a URL to a Servlet where
the `doGet` and `doPost` methods handle the appropriate action. The Survey
class functions as our model, and the JSP files in WEB-INF/Views. As an example,
let's walk through what happens to fill out a survey:

 1. A user requests something from the URL `/demo-survey/home`. Everything
    starting `/demo-survey` is mapped to our application. Our `HomeServlet`
    class extends `HttpServlet` via our helper base class `BaseServlet`
    **and** is annotated: `@WebServlet(value="/home", loadOnStartup=1)`
    so that the server knows to use class to handle this request.
 2. Since this is an "ordinary" GET request, the `doGet` method is called on
    an instance of `HomeServlet`.
 3. That method gets a default `Survey` instance, makes it available to views,
    and choose the view to render. In this case that view is `/WEB-INF/view/home.jsp`
 4. The page `home.jsp` is rendered like any other JSP page using JSTL. Note
    that it (like all our views) uses a "base page" tag that we created to
    centralize UI layout/design. You can see the tag in `/WEB-INF/tags/basepage.tag`
 5. The user fills out the survey and submits the form. We'll ignore the
    client-side validation for now.
 6. If you look at the HTML form, you'll notice that it specifies `method="POST"`
    but not `action` attribute. As a result, when the form is submitted it will
    be a POST request to the same URL.
 7. As you can probably guess, the `doPost` method on the same class
    `HomeServlet` is called.
 8. If there is an error saving the survey, the error view is rendered. If
    everything is fine, then a **redirect** is issued. This is known as the
    PRG pattern (Post, Redirect, Get) and is to make sure that (among other
    things) duplicate saves don't occur. You can read about it in depth here:
    [PRG Pattern](http://en.wikipedia.org/wiki/Post/Redirect/Get).
 9. We'll just assume that the survey was saved successfully. In that case, that
    browser is redirected to `/demo-survey/thanks`. Using the same logic as
    \#1 above, we know this request will be routed to the `doGet` method of
    the `ThanksServlet` "controller" which will render the view `/WEB-INF/view/thanks.jsp`


Configuration
--------------

Generally a large application will have multiple layers of configuration. For
instance, there might be a default or "base" set of configuration values in a
file in `src/main/resources` (which would be deployed with the application).
Environment variables or external configuration files would provide a means to
customize the application. This is generally used for running the application
(at a minimum) in a "development" mode (on your workstation), a "test" mode,
and a "production" mode.

This application doesn't really require much configuration. In fact, because
we work with the default values in most situations, there is currently only
one configuration value needed. The class `DataStoreClient` checks for the
Java system property `aws.dynamoEndpoint`. This value isn't specified by
Amazon Elastic Beanstalk. This is fine because the default endpoint for
DynamoDB is to use the "correct" one for Amazon.

The issue is testing on our local workstation. Since we don't have access to
the AWS cloud, we run a local version of DynamoDB (see "Setting up your
development workstation" in [Introduction and Overview](index.html) for
details). Luckily, the Tomcat plugin for Maven we use allows us to set Java
system properties. If you look at the `<build>` section of the `pom.xml`
file, you'll see this:

    <plugin>
        <groupId>org.apache.tomcat.maven</groupId>
        <artifactId>tomcat7-maven-plugin</artifactId>
        <version>2.1</version>
        <configuration>
            <useTestClasspath>true</useTestClasspath>
            <!-- We set these system properties for testing on a local workstation -->
            <systemProperties>
                <aws.accessKeyId>TEST</aws.accessKeyId>
                <aws.secretKey>TEST</aws.secretKey>
                <aws.dynamoEndpoint>http://localhost:8000</aws.dynamoEndpoint>
            </systemProperties>
        </configuration>
    </plugin>

You can see that we also specify spurious values for the AWS Access Key ID
and Secret Key. These are usually handled for you on Elastic Beanstalk, but
we need to specify *something* for the local DynamoDB. ***Pro Tip:*** the
key name TEST is used as part of the SQLite database named created by the
Local DynamoDB.

One final note on configuration: modern Java applications generally use a
development pattern called "Inversion of Control" and then configure the
software components at runtime using another, similar pattern called
"Dependency Injection" (often abbreviated IoC or DI). This particular
pattern wasn't used for this application since the general complexity of
this application was already pretty high. However, interested readers may
want to read Martin Fowler's article
[Inversion of Control Containers and the Dependency Injection pattern](http://martinfowler.com/articles/injection.html).

In our case, the class DataStoreClient might not continue to create its own
instances of the classes. `AmazonDynamoDBClient`, `DynamoDB`, and
`DynamoDBMapper`. In addition, the controllers (servlets) that just create
a new instance of DataStoreClient would be "given" the instance to use. All of
which brings us to the data design.

Data Overview (including the model and DAO classes)
-----------------------------------------------------------

Like most applications our handling of data begins with the model. In our case,
that's the class `Survey`. In this situation, we begin with the assumption that
we are always going to use Amazon DynamoDB for our data storage. As a result,
we go ahead and annotate `Survey` to indicate the DynamoDB table we'll use,
which attribute is the key (it's `participantCode`), name the other attributes,
and specify attributes that should be ignored when loading or saving a survey
(`isValid` shouldn't be written to the database).

Our annotated model means that we can use the "high level" data model that
Amazon provides for DynamoDB. The curious and industrious can find loads of
details in the Amazon documentation
[here](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/ORM.html).

However, we still need to answer a few questions:

 1. How will our initial database get created?
 2. Where will the code to read/write our data "live"?

The initial answer to both questions is our `DataStoreClient` class. This class
loosely follows the
[Data Access Object pattern](http://en.wikipedia.org/wiki/Data_access_object)
which is often referred to as "DAO". In the name of clarity, our DAO is fairly
concrete. If for some reason you wanted to support multiple data backends, you
would want multiple implementation of the `DataStoreClient`. Regardless, our
DAO makes available three actions:

 1. `ensureSchema` which creates any missing tables
 2. `saveSurvey` which saves a single instance of `Survey`
 3. `findSurveys` which returns a `List` of the saved `Survey` instances.

When and where the save/find method are called is fairly obvious and can be
seen in the servlet/controller classes `HomeServlet` and `DataDumpServlet`.
The slightly more mysterious method is for initially creating the database. It
works via a combination of things:

 1. The static variable `TABLES` has a list of all DynamoDB-annotated "tables"
    (which are just classes from our model) to be created.
 2. We call the method from the `init` method of the `HomeServlet` class
 3. We make sure that of all our servlets, the `loadOnStartup` attribute of
    the `@WebServlet` annotation is the **lowest**.

Any Java web server our application runs on will make sure that the load order
for servlets is sorted by loadOnStartup. It will also insure that the `init`
method on any servlet is called only once. The final result is that every time
our application starts up, the first thing we do is check to ensure that the
database is created.

Logging
--------

An important component to any application that will run on a server is how
information will be logged. Currently we use the fairly standard
[SLF4J](http://www.slf4j.org/) logging framework. You can get an excellent
introduction via the succinct [SLF4J user manual](http://www.slf4j.org/manual.html).
The basics are that you log to the SLF4J API and select a logging "backend"
at runtime.

In Elastic Beanstalk Java applications, the Tomcat server has been modified to
use the `java.util.logging` log facilities, which SLF4J can use. For local
testing, we use the excellent logback-classic library.

Testing
--------

Like most application, we have included unit tests as a gentle introduction
to jUnit. You'll note that Maven will automatically run the unit tests for
you if you forget. They can also be run in the jUnit view in most IDE's
(although only Eclipse has been tested).

In attempt to keep the complexity low, no there aren't as many unit tests
as there might be in a "real" application. For the sake of this demo, we
are side-stepping the question of how to unit test views or controllers, and
we haven't included any integration tests that might check our DAO classes.

Static Analysis
------------------

Although a Java web application might seem complex, once you have all the
various pieces together, it's fairly easy to add some impressive software
engineering tools. As an example, we demonstrate how to add static analysis
to your application.

In our case, we've added a [FindBugs](http://findbugs.sourceforge.net/)
report via the [FindBugs Maven Plugin](http://gleclaire.github.io/findbugs-maven-plugin/).
Although this may seem complicated, it's really quite simple. We added
the FindBugs plugin to the `<reporting>` section and the report is
included as part of our site generation. We have helpfully left in code
to flag a FindBugs message so that you can see an example of the kind
of help it can provide. Of course, this is fairly mild example since we want
the application to actually function!

Interested readers may want to look at the list of Java static analysis tools
at [this Wikipedia page](http://en.wikipedia.org/wiki/List_of_tools_for_static_code_analysis#Java).
Of those listed, two very popular tools in industry are:
[Checkstyle](https://maven.apache.org/plugins/maven-checkstyle-plugin/) and
[PMD](http://www.javavillage.in/PMD-CPD-in-maven.php).

Project Reports
-------------------

Be sure to check out the [Project Reports](project-reports.html) in the site
generated. It includes the JavaDocs generated from the JavaDoc comments in
the source code, the output from running the unit tests, the FindBugs report
mentioned above and more.

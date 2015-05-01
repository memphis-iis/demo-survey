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
a new instance of DataStoreClient would be "given" the instance to use.
  

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

TODO: findbugs reports

TODO: unit testing


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
the `doGet` and `doPost` methods handle the appropriate action.


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

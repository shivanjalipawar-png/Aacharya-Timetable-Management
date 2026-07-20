
=========================================
Day 02 – Spring Boot Setup & First REST API
Date: __________
=========================================

Today's Objective:
- Configure the Spring Boot project.
- Connect the application to MySQL.
- Understand project structure.
- Run the backend successfully.
- Create the first REST API.

-------------------------------------------------
Topics Learned
-------------------------------------------------

1. Spring Boot Project Structure
- Understood the purpose of:
  - src/main/java
  - src/main/resources
  - application.properties
- Learned that Java classes are organized using packages.

2. Package Concept
- Packages help organize related Java classes.
- They avoid naming conflicts.
- Spring Boot automatically scans classes inside the main package and its sub-packages.

3. application.properties
- Learned that it is the configuration file of a Spring Boot application.
- Configured:
  - Application name
  - Database URL
  - MySQL username
  - MySQL password
- Understood that Spring Boot reads this file during application startup.

4. MySQL Connection
- Created database:
  timetable_management
- Connected Spring Boot with MySQL successfully.
- Learned what a DataSource is and why it is required.

5. Understanding Startup Errors
- Faced the error:
  "Failed to configure a DataSource"
- Learned how to read and understand Spring Boot error messages.
- Fixed the issue by configuring database properties.

6. Embedded Tomcat
- Learned that Spring Boot contains an embedded Tomcat server.
- Successfully started the application on:
  http://localhost:8080

7. First REST Controller
- Created package:
  controller
- Created class:
  HomeController

8. First REST API
- Learned:
  @RestController
  @GetMapping("/")
- Created first API that returns:

  "Backend is running successfully!"

9. Browser Testing
- Initially received:
  HTTP 404 (Whitelabel Error Page)
- Understood why it occurred.
- Successfully tested the REST API in the browser after creating HomeController.

-------------------------------------------------
Concepts Understood
-------------------------------------------------

✓ Spring Boot Framework
✓ Maven
✓ Packages
✓ application.properties
✓ DataSource
✓ MySQL Connection
✓ Embedded Tomcat
✓ REST Controller
✓ GET Mapping
✓ Localhost
✓ HTTP Request & Response

-------------------------------------------------
Challenges Faced
-------------------------------------------------

- Project JDK was not configured.
- Spring Boot failed due to missing DataSource configuration.
- Controller package was initially created in the wrong location.
- Learned how to fix package structure and configuration issues.

-------------------------------------------------
Today's Achievement
-------------------------------------------------

✔ Successfully configured Spring Boot.
✔ Successfully connected MySQL database.
✔ Successfully started Tomcat server.
✔ Created the first REST API.
✔ Understood the complete request flow:
Browser → Controller → Response.

-------------------------------------------------
Next Day Plan (Day-03)
-------------------------------------------------

- Understand Spring Boot architecture in depth.
- Learn MVC architecture.
- Create Teacher Entity.
- Understand JPA annotations.
- Automatically create the Teacher table in MySQL.
- Build CRUD APIs for Teacher Management.


Todays Learning :
-------------------------------------------------
Reflection
-------------------------------------------------

Today I learned how a Spring Boot application starts, connects to a MySQL database, and handles HTTP requests using a REST Controller. I also learned that debugging startup errors is an important part of backend development. Successfully running my first API increased my confidence and gave me a better understanding of how backend applications work.
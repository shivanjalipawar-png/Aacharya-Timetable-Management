
# 📅 Day-03 Progress Report
**Date:** 15 July 2026

## 🎯 Goal
Understand Hibernate Entity Mapping and create the first database table using Spring Boot and MySQL.

---

## 📚 Concepts Learned

### ✅ Hibernate
- Learned that Hibernate is an ORM (Object Relational Mapping) framework.
- It converts Java objects into database tables and vice versa.
- Reduces manual SQL writing and simplifies database operations.

### ✅ Entity
- Understood that an Entity is a Java class that represents a database table.
- Learned that only classes annotated with `@Entity` are converted into database tables.

### ✅ @Entity
- Learned that `@Entity` is a JPA annotation.
- It tells Hibernate that this Java class should be mapped to a database table.

### ✅ Primary Key
- Understood the importance of a Primary Key.
- Every record in a table must have a unique identifier.

### ✅ @Id
- Learned that `@Id` marks a field as the Primary Key of the table.
- It does **not** generate the ID; it only identifies which field is the primary key.

### ✅ @GeneratedValue
- Learned that `@GeneratedValue` automatically generates values for the Primary Key.
- Used:
  ```java
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  ```
- Understood that `GenerationType.IDENTITY` uses MySQL's `AUTO_INCREMENT` feature.

### ✅ application.properties
Learned the purpose of important Spring Boot properties:

- `spring.datasource.url` → Connects the application to MySQL.
- `spring.datasource.username` → MySQL username.
- `spring.datasource.password` → MySQL password.
- `spring.jpa.hibernate.ddl-auto=update` → Automatically creates/updates database tables.
- `spring.jpa.show-sql=true` → Displays generated SQL queries in the console.
- `spring.jpa.properties.hibernate.format_sql=true` → Formats SQL queries for better readability.

---

## 💻 Practical Work Completed

- Created the `Teacher` Entity.
- Added the following fields:
  - teacherId
  - name
  - email
  - phone
  - qualification
  - specialization
- Added required JPA annotations:
  - `@Entity`
  - `@Id`
  - `@GeneratedValue`

---

## 🚀 Project Execution

Successfully ran the Spring Boot application.

Verified that:
- Spring Boot started successfully.
- Tomcat started on Port 8080.
- Hibernate connected to MySQL.
- MySQL connection was successful.

---

## 🗄️ Database Achievement

🎉 Hibernate automatically generated the **teacher** table inside the `timetable_management` database.

Verified using MySQL Workbench:
- `SHOW TABLES;`
- Confirmed that the `teacher` table was created successfully.

This was my **first database table created automatically using Hibernate**.

---

## 🧠 Key Learnings

- Difference between `@Id` and `@GeneratedValue`.
- Difference between `@GeneratedValue` and `GenerationType.IDENTITY`.
- How Hibernate converts Java classes into SQL tables.
- Importance of automatic Primary Key generation.
- Basic understanding of Spring Boot startup logs.
- Learned that warnings are not always errors.

---

## 🌟 Milestone Achieved

✅ Successfully built my first Spring Boot + Hibernate + MySQL backend application.

This is my first project as a Backend Developer, and today I witnessed Hibernate automatically creating a database table from my Java Entity class without writing any SQL `CREATE TABLE` statement.

A memorable milestone in my backend development journey! 🚀
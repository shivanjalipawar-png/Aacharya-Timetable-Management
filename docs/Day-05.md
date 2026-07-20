
# 📅 Day-05: Spring Boot CRUD (GET, PUT & DELETE)

## 🎯 Objective

Today, I completed the remaining CRUD operations of the **Teacher Module** using Spring Boot and tested all APIs using Postman.

---

# 📖 Topics Covered

## ✅ 1. GET API (Read)

### Get All Teachers

- Annotation: `@GetMapping`
- URL:

```http
GET /teachers
```

- Repository Method:

```java
findAll()
```

- Purpose:
Retrieves all teachers from the database.

---

### Get Teacher By ID

- Annotation:

```java
@GetMapping("/{id}")
```

- URL:

```http
GET /teachers/3
```

- Repository Method:

```java
findById(id)
```

- Special Annotation:

```java
@PathVariable
```

### Purpose

Retrieves a specific teacher using the teacher ID.

---

## ✅ 2. Understanding Optional

```java
teacherRepository.findById(id).orElse(null);
```

### Meaning

- If teacher exists → return Teacher object.
- Otherwise → return `null`.

---

## ✅ 3. PUT API (Update)

### Purpose

Updates an existing teacher.

### URL

```http
PUT /teachers/{id}
```

Example

```http
PUT /teachers/3
```

### Controller Annotation

```java
@PutMapping("/{id}")
```

### Repository Method Used

```java
save()
```

### Update Process

```text
Find Teacher
      ↓
Update Fields
      ↓
Save Teacher
```

### Important Concept

`save()` is used for both:

- Create
- Update

Spring Boot decides whether to INSERT or UPDATE based on the Primary Key.

---

## ✅ 4. DELETE API

### Purpose

Deletes a teacher from the database.

### URL

```http
DELETE /teachers/{id}
```

Example

```http
DELETE /teachers/3
```

### Controller Annotation

```java
@DeleteMapping("/{id}")
```

### Repository Method

```java
deleteById(id)
```

---

# 📚 JpaRepository Methods Used

| Method | Purpose |
|---------|----------|
| save() | Create / Update |
| findAll() | Retrieve all records |
| findById() | Retrieve one record |
| deleteById() | Delete record |

---

# 🌐 CRUD Flow

```text
Postman
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
Hibernate (JPA)
   ↓
MySQL Database
```

---

# 📝 HTTP Methods Learned

| Method | Purpose |
|---------|----------|
| POST | Create new data |
| GET | Retrieve data |
| PUT | Update existing data |
| DELETE | Delete data |

---

# ⚠ HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | OK |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 405 | Method Not Allowed |
| 409 | Conflict |
| 415 | Unsupported Media Type |
| 500 | Internal Server Error |

---

# 💡 Key Learnings

- Learned the complete CRUD cycle.
- Understood the role of Controller, Service, Repository and Hibernate.
- Learned how `@PathVariable` extracts values from the URL.
- Learned how `@RequestBody` converts JSON into a Java object.
- Understood why `save()` is used for both Create and Update.
- Learned that `JpaRepository` already provides built-in CRUD methods.
- Tested all APIs successfully using Postman.

---

# 🎯 CRUD Progress

```text
✅ CREATE  (POST)
✅ READ    (GET)
✅ UPDATE  (PUT)
✅ DELETE  (DELETE)
```

🎉 **Teacher Module CRUD Completed Successfully!**

---

# 📌 Interview Notes

- CRUD = Create, Read, Update, Delete.
- `@RequestBody` converts JSON to a Java object.
- `@PathVariable` extracts values from the URL.
- `save()` performs both INSERT and UPDATE.
- `JpaRepository` provides built-in CRUD methods.
- Controller handles requests.
- Service contains business logic.
- Repository communicates with the database.
- Hibernate converts Java objects into SQL queries automatically.

---

# 🚀 Project Status

```
✔ Spring Boot Setup
✔ Database Connection
✔ Entity
✔ Repository
✔ Service
✔ Controller
✔ POST API
✔ GET APIs
✔ PUT API
✔ DELETE API

Teacher Module CRUD Completed ✅
```
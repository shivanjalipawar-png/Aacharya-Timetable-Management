# Day-09: REST API Development and Exception Handling

**Project:** Timetable Management System  
**Technology:** Spring Boot, Spring MVC, Spring Data JPA, MySQL

---

# Objective

Improve the REST APIs by:

- Implementing complete CRUD operations
- Returning appropriate HTTP Status Codes
- Using ResponseEntity
- Creating a custom exception
- Implementing centralized exception handling
- Testing APIs using Postman

---

# Topics Covered

## 1. CRUD APIs

Implemented complete CRUD operations for:

- Batch
- Teacher
- Subject

Operations:

- Create
- Read
- Update
- Delete

---

## 2. ResponseEntity

Replaced normal return types with ResponseEntity.

Example:

```java
return ResponseEntity.status(HttpStatus.CREATED)
        .body(savedBatch);
```

Advantages:

- Control HTTP Status Codes
- Return custom response body
- Build professional REST APIs

---

## 3. HTTP Status Codes

Implemented proper status codes.

| Status Code | Meaning | Used For |
|-------------|----------|----------|
| 200 | OK | Get & Update |
| 201 | Created | Create |
| 204 | No Content | Delete |
| 404 | Not Found | Resource not available |

---

## 4. ResourceNotFoundException

Created a custom exception class.

Purpose:

Whenever requested data is not found in the database, instead of returning null, throw an exception.

Example:

```java
throw new ResourceNotFoundException(
    "Teacher not found with id: " + id
);
```

---

## 5. Global Exception Handler

Created

```java
@ControllerAdvice
```

to handle exceptions globally.

Handled:

```java
ResourceNotFoundException
```

Returned:

```http
404 Not Found
```

with

```json
{
    "message":"Teacher not found with id: 20"
}
```

---

# Changes Made

## Subject Module

Implemented:

- saveSubject()
- getAllSubjects()
- getSubjectById()
- updateSubject()
- deleteSubject()

Added:

- ResourceNotFoundException
- ResponseEntity
- Proper HTTP Status Codes

---

## Teacher Module

Implemented:

- saveTeacher()
- getAllTeachers()
- getTeacherById()
- updateTeacher()
- deleteTeacher()

Added:

- ResponseEntity
- ResourceNotFoundException
- Proper HTTP Status Codes

---

## Batch Module

Implemented:

- saveBatch()
- getAllBatches()
- getBatchById()
- updateBatch()
- deleteBatch()

Added:

- ResourceNotFoundException
- CRUD completion

---

# Files Modified

- BatchController.java
- TeacherController.java
- SubjectController.java

- BatchService.java
- TeacherService.java
- SubjectService.java

- ResourceNotFoundException.java
- GlobalExceptionHandler.java

---

# APIs Tested

Tested all CRUD APIs using Postman.

Verified:

- Create
- Read
- Update
- Delete

Verified:

- Correct HTTP Status Codes
- Correct JSON Responses
- ResourceNotFoundException handling

---

# Important Concepts Learned

## ResponseEntity

Used to return:

- HTTP Status Code
- Response Body

instead of returning only Java objects.

---

## Custom Exception

Created custom exception instead of using generic RuntimeException.

Advantages:

- Better readability
- Better debugging
- Professional error handling

---

## Global Exception Handling

Instead of writing try-catch blocks inside every controller,

used

```java
@ControllerAdvice
```

to handle exceptions from one central place.

Advantages:

- Cleaner Controllers
- Reusable code
- Easy maintenance

---

# Key Learnings

- Difference between normal return type and ResponseEntity
- Proper use of HTTP Status Codes
- Why custom exceptions are better than RuntimeException
- Centralized exception handling using @ControllerAdvice
- Professional REST API design
- API testing using Postman

---

# Day-09 Outcome

✅ CRUD APIs completed

✅ ResponseEntity implemented

✅ HTTP Status Codes implemented

✅ ResourceNotFoundException created

✅ Global Exception Handler implemented

✅ CRUD APIs tested successfully

---

# Next Goal (Day-10)

- Bean Validation
- @Valid
- @NotBlank
- MethodArgumentNotValidException
- BindingResult
- FieldError
- Validation Error Responses
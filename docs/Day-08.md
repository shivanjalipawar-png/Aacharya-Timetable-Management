
# Day-08: Subject Module Completion & Entity Relationship Validation

**Project:** Timetable Management System  
**Technology:** Spring Boot, Spring Data JPA, MySQL, Postman  
**Day:** 08  
**Status:** ✅ Completed Successfully

---

# Objectives

- Create the Subject module.
- Establish relationship between Subject and Teacher.
- Implement CRUD operations for Subject.
- Validate Teacher before saving/updating Subject.
- Test all APIs using Postman.
- Debug compilation and runtime errors.

---

# Topics Learned

## 1. Creating Subject Entity

Created a new entity named `Subject`.

Fields:

- subjectId
- subjectName
- Teacher (Many-to-One Relationship)

---

## 2. Many-to-One Relationship

Used:

```java
@ManyToOne
@JoinColumn(name = "teacher_id")
private Teacher teacher;
```

### Meaning

- One Teacher can teach many Subjects.
- Every Subject belongs to only one Teacher.

Example:

Teacher

```
Rohit
```

Subjects

```
Java
Spring Boot
Hibernate
```

One Teacher → Many Subjects

---

## 3. Subject Repository

Created:

```java
public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
```

Learned:

- No need to write CRUD methods manually.
- JpaRepository already provides them.

---

## 4. Subject Service

Implemented:

### Save

```java
save()
```

### Get All

```java
getAllSubjects()
```

### Get By Id

```java
getSubjectById()
```

### Update

```java
updateSubject()
```

### Delete

```java
deleteSubject()
```

---

# Important Improvement

Initially I was directly saving:

```java
subject.setTeacher(updatedSubject.getTeacher());
```

Improved version:

```java
Long teacherId = subject.getTeacher().getTeacherId();

Teacher teacher = teacherRepository.findById(teacherId).orElse(null);

if (teacher == null) {
    return null;
}

subject.setTeacher(teacher);

return subjectRepository.save(subject);
```

### Why?

Because the Teacher must already exist in the database before assigning it to a Subject.

This prevents invalid foreign keys.

---

# Subject Controller

Implemented REST APIs:

```java
POST    /subjects
GET     /subjects
GET     /subjects/{id}
PUT     /subjects/{id}
DELETE  /subjects/{id}
```

---

# Batch Controller Improvement

Discovered missing endpoint:

```java
@GetMapping
public List<Batch> getAllBatches() {
    return batchService.getAllBatches();
}
```

Learned that forgetting this endpoint results in:

```
405 Method Not Allowed
```

---

# API Testing

## POST

Successfully created Subject.

Example JSON

```json
{
    "subjectName": "Java",
    "teacher": {
        "teacherId": 2
    }
}
```

---

## GET All

Successfully fetched all Subjects.

---

## GET By Id

Successfully fetched individual Subject.

---

## PUT

Successfully updated Subject details.

---

## DELETE

Successfully deleted Subject.

---

# Errors Faced

## Error 1

```
Cannot resolve symbol Batch
```

### Reason

Teacher.java had syntax/import issues.

### Fix

Corrected imports and Batch declaration.

---

## Error 2

```
Cannot resolve symbol subjectRepository
```

### Reason

Forgot to inject SubjectRepository.

### Fix

Added:

```java
@Autowired
private SubjectRepository subjectRepository;
```

---

## Error 3

```
Cannot resolve method saveSubject()
```

### Reason

Controller method name didn't match Service method.

### Fix

Changed:

```java
subjectService.save(subject);
```

---

## Error 4

```
Cannot resolve method updateSubject()
```

### Reason

Method names in Controller and Service were different.

### Fix

Renamed methods consistently.

---

## Error 5

```
Cannot resolve symbol Batch
```

### Reason

Teacher.java contained duplicate declarations and syntax errors.

### Fix

Rewrote Teacher entity correctly.

---

## Error 6

```
405 Method Not Allowed
```

### Reason

BatchController didn't contain:

```java
@GetMapping
```

### Fix

Added GET All endpoint.

---

## Error 7

```
Cannot find symbol
method getAllBatches()
```

### Reason

Method missing in BatchService.

### Fix

Added:

```java
public List<Batch> getAllBatches() {
    return batchRepository.findAll();
}
```

---

## Error 8

```
Missing return statement
```

### Reason

updateBatch() was incomplete.

### Fix

Implemented full update logic.

---

# Important Concepts Learned

## Why use Repository in Service?

Service should interact with the database through Repository.

Controller should never directly access Repository.

---

## Import vs Autowired

Import

```java
import SubjectRepository;
```

Only tells Java that the class exists.

Autowired

```java
@Autowired
private SubjectRepository subjectRepository;
```

Actually creates/injects the object.

---

## Why validate Teacher before saving Subject?

Without validation,

```
teacherId = 99
```

may not exist.

Validation ensures

- Data integrity
- Valid foreign keys
- No invalid relationships

---

## Why fetch entity before Update?

Wrong

```java
repository.save(updatedObject);
```

Correct

```java
Entity entity = repository.findById(id).orElse(null);

entity.setSomething(...);

repository.save(entity);
```

Reason:

- Updates existing record.
- Prevents accidental insertion.
- Ensures record exists.

---

# CRUD Flow Learned

## Create

```
POST
↓
Controller
↓
Service
↓
Repository
↓
Database
```

---

## Read

```
GET
↓
Controller
↓
Service
↓
Repository
↓
Database
```

---

## Update

```
Find Entity
↓
Modify Fields
↓
Save Entity
```

---

## Delete

```
Find by ID
↓
Delete
```

---

# Best Practices Learned

- Always validate foreign key entities.
- Keep Controllers thin.
- Business logic belongs in Service.
- Database access belongs in Repository.
- Match method names across Controller and Service.
- Fetch entity before updating.
- Never trust IDs received from client without validation.

---

# APIs Implemented

## Batch

- POST
- GET All
- GET By Id
- PUT
- DELETE

---

## Teacher

- POST
- GET All
- GET By Id
- PUT
- DELETE

---

## Subject

- POST
- GET All
- GET By Id
- PUT
- DELETE

---

# Key Takeaways

- Understood Many-to-One relationship deeply.
- Learned how foreign keys work in JPA.
- Learned Repository injection using `@Autowired`.
- Learned difference between importing a class and injecting an object.
- Learned why Service validates related entities before saving.
- Learned proper update workflow in Spring Boot.
- Successfully built and tested the complete Subject module.
- Debugged multiple compile-time and runtime errors independently.

---

# Progress Summary

✅ Subject Entity  
✅ Subject Repository  
✅ Subject Service  
✅ Subject Controller  
✅ Teacher Validation  
✅ CRUD Operations  
✅ Postman Testing  
✅ MySQL Verification  
✅ Batch GET API Fixed  
✅ Project Build Successful

---

# Day-08 Completion Status

**Status:** ✅ Successfully Completed

The Timetable Management System now supports complete CRUD operations for:

- Batch
- Teacher
- Subject

with proper entity relationships and validation.

# Day-07: Implementing Teacher-Batch Relationship (Many-to-One)

## Objective
Today, I implemented the relationship between the `Teacher` and `Batch` entities using Spring Boot, Spring Data JPA, and MySQL.

---

## What I Learned

### 1. Many-to-One Relationship
- A single batch can have multiple teachers.
- Each teacher belongs to only one batch.
- This relationship is represented using `@ManyToOne`.

```java
@ManyToOne
@JoinColumn(name = "batch_id")
private Batch batch;

Changes Made
Teacher Entity
Added the Batch object.
Added relationship annotations.
Generated getters and setters.
@ManyToOne
@JoinColumn(name = "batch_id")
private Batch batch;
TeacherService

Updated the saveTeacher() method to validate the batch before saving a teacher.

Long batchId = teacher.getBatch().getBatchId();

Batch batch = batchRepository.findById(batchId).orElse(null);

if(batch == null){
    return null;
}

teacher.setBatch(batch);

return teacherRepository.save(teacher);
Why this validation?

To ensure that a teacher cannot be assigned to a batch that does not exist in the database.

APIs Tested
1. Create Batch

POST

http://localhost:8080/batches

Request Body

{
    "batchName": "Java Full Stack"
}
2. Create Teacher

POST

http://localhost:8080/teachers

Request Body

{
    "name": "Rohit Sharma",
    "email": "rohit@gmail.com",
    "phone": "9876543210",
    "qualification": "M.Tech",
    "specialization": "Java",
    "batch": {
        "batchId": 2
    }
}
Successful Response
{
    "teacherId": 4,
    "name": "Rohit Sharma",
    "email": "rohit@gmail.com",
    "phone": "9876543210",
    "qualification": "M.Tech",
    "specialization": "Java",
    "batch": {
        "batchId": 2,
        "batchName": "Java Full Stack"
    }
}
Database Verification
Batch Table
batch_id	batch_name
2	Java Full Stack
3	Java Full Stack
Teacher Table
teacher_id	name	email	batch_id
4	Rohit Sharma	rohit@gmail.com	2

This confirms that the foreign key relationship is working correctly.

Errors Faced
1. cannot find symbol getBatch()

Cause

The Teacher entity did not contain the Batch object and its getter/setter methods.

Solution

Added:

@ManyToOne
@JoinColumn(name = "batch_id")
private Batch batch;

along with its getters and setters.

2. Teacher not getting saved

Cause

The JSON request contained a batch ID that did not exist in the database.

Solution

Created the batch first and used the correct batchId while creating the teacher.

3. 404 Not Found

Cause

Incorrect endpoint:

/teacher

Solution

Used the correct endpoint:

/teachers
4. ECONNREFUSED

Cause

Spring Boot application was not running.

Solution

Started the application before sending requests from Postman.

Key Concepts Learned
One-to-Many & Many-to-One relationship
@ManyToOne
@JoinColumn
Foreign Keys
Entity Relationship Mapping
Repository Injection
Validating related entities before saving
Nested JSON Requests
Postman API Testing
MySQL Verification
Debugging Spring Boot APIs
Day-07 Outcome

✅ Successfully implemented the relationship between Teacher and Batch.

✅ Teacher records are now stored with a valid foreign key (batch_id).

✅ Verified successful API responses using Postman.

✅ Confirmed data persistence in MySQL.

Next Goal (Day-08)
Implement the next entity (Subject/Classroom as per project).
Create Entity, Repository, Service, and Controller.
Establish relationships with existing entities.
Test APIs using Postman.
Verify data in MySQL.

# Day-04 : Building & Testing the First REST API (POST)

## Topics Covered

### 1. Service Layer
- Created `TeacherService` class.
- Used `@Service` annotation to mark it as the business logic layer.
- Connected `TeacherService` with `TeacherRepository` using `@Autowired`.
- Implemented `saveTeacher()` method to save teacher data into the database.

### 2. Controller Layer
- Created `TeacherController` class.
- Used `@RestController` to make it a REST API controller.
- Used `@RequestMapping("/teachers")` to define the base URL.
- Created the first POST API using `@PostMapping`.
- Used `@RequestBody` to receive JSON data from the client and convert it into a Java object.

### 3. Understanding the Complete Request Flow
Learned how a request travels through the application:

Client (Postman)
→ Controller
→ Service
→ Repository
→ Hibernate
→ MySQL Database

The response follows the reverse path back to the client.

### 4. Testing APIs with Postman
- Installed and configured Postman.
- Sent a POST request to:
  http://localhost:8080/teachers
- Added teacher details in JSON format.
- Successfully received a JSON response from the server.

### 5. Debugging and Error Resolution
- Understood the difference between:
  - 404 Not Found (URL doesn't exist)
  - 405 Method Not Allowed (Wrong HTTP method)
- Fixed package structure issues.
- Resolved Spring bean detection (`@Autowired`) issues.
- Learned why getters and setters are required for JSON serialization/deserialization.

### 6. Database Verification
- Verified data insertion using MySQL Command Line.
- Used:
  - `DESCRIBE teacher;`
  - `SELECT * FROM teacher;`
- Observed auto-generated `teacherId` values using `@GeneratedValue`.

## Key Concepts Learned
- `@Service`
- `@RestController`
- `@RequestMapping`
- `@PostMapping`
- `@RequestBody`
- `@Autowired`
- JSON ↔ Java Object Conversion
- Hibernate `save()` operation
- API Testing using Postman
- HTTP Status Codes (404 & 405)

## Practical Achievement
✅ Built the first working REST API.

Successfully performed the complete flow:
JSON Request → Spring Boot → Hibernate → MySQL → JSON Response

## CRUD Progress
- ✅ Create (POST) – Completed
- ⏳ Read (GET) – Next
- ⏳ Update (PUT) – Pending
- ⏳ Delete (DELETE) – Pending

## Day-04 Outcome
Today I successfully built and tested my first Spring Boot REST API. I understood how different layers (Controller, Service, Repository, Hibernate, and MySQL) work together to process a client request. I also learned to test APIs using Postman, debug common errors, and verify stored data directly in the MySQL database.
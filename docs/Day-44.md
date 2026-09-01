# Day-44: Timetable Validation and Conflict Testing

## Objective

The objective of Day-44 was to test the validation, resource existence checks, and timetable conflict detection implemented in the Timetable Management backend.

The testing focused on:

* Request validation.
* Teacher, Batch and Subject existence validation.
* Teacher scheduling conflicts.
* Batch scheduling conflicts.
* Classroom scheduling conflicts.

---

## 1. Create Valid Timetable

A valid timetable was first created using an ADMIN JWT to establish a base record for the conflict tests.

### Endpoint

```http
POST http://localhost:8080/api/timetables
```

### Request Body

```json
{
    "day": "TUESDAY",
    "startTime": "14:00",
    "endTime": "15:00",
    "classroom": "Room 200",
    "teacherId": 1,
    "batchId": 2,
    "subjectId": 2
}
```

### Result

```text
201 Created
```

The timetable was successfully created.

This timetable was then used as the reference timetable for the conflict tests.

---

## 2. Missing Required Field Validation

The `subjectId` field was intentionally removed from the request body.

### Request Body

```json
{
    "day": "TUESDAY",
    "startTime": "14:00",
    "endTime": "15:00",
    "classroom": "Room 201",
    "teacherId": 1,
    "batchId": 2
}
```

### Result

```text
400 Bad Request
```

The response indicated that `subjectId` was required.

### Outcome

This confirmed that the `@NotNull` validation on `subjectId` is working correctly.

---

## 3. Invalid Teacher ID

A non-existing teacher ID was provided.

### Request Body

```json
{
    "day": "WEDNESDAY",
    "startTime": "10:00",
    "endTime": "11:00",
    "classroom": "Room 202",
    "teacherId": 9999,
    "batchId": 2,
    "subjectId": 2
}
```

### Result

```text
404 Not Found
```

### Outcome

The API correctly detected that the teacher does not exist.

The timetable was not created.

---

## 4. Invalid Batch ID

A non-existing batch ID was provided.

### Request Body

```json
{
    "day": "WEDNESDAY",
    "startTime": "11:00",
    "endTime": "12:00",
    "classroom": "Room 203",
    "teacherId": 1,
    "batchId": 9999,
    "subjectId": 2
}
```

### Result

```text
404 Not Found
```

### Outcome

The API correctly detected that the batch does not exist.

The timetable was not created.

---

## 5. Invalid Subject ID

A non-existing subject ID was provided.

### Request Body

```json
{
    "day": "WEDNESDAY",
    "startTime": "12:00",
    "endTime": "13:00",
    "classroom": "Room 204",
    "teacherId": 1,
    "batchId": 2,
    "subjectId": 9999
}
```

### Result

```text
404 Not Found
```

### Outcome

The API correctly detected that the subject does not exist.

The timetable was not created.

---

## 6. Teacher Conflict Testing

The existing timetable used:

```text
Day       : TUESDAY
Time      : 14:00 - 15:00
Teacher   : 1
Batch     : 2
Classroom : Room 200
```

Another timetable was requested using the same teacher, day and time but with a different batch and classroom.

### Request Body

```json
{
    "day": "TUESDAY",
    "startTime": "14:00",
    "endTime": "15:00",
    "classroom": "Room 205",
    "teacherId": 1,
    "batchId": 3,
    "subjectId": 2
}
```

### Result

```text
409 Conflict
```

The response indicated that the teacher already had a class during that time.

### Outcome

The teacher conflict detection logic is working correctly.

---

## 7. Batch Conflict Testing

The existing timetable used:

```text
Day       : TUESDAY
Time      : 14:00 - 15:00
Batch     : 2
```

A new timetable was requested using the same batch, day and time but a different teacher and classroom.

### Request Body

```json
{
    "day": "TUESDAY",
    "startTime": "14:00",
    "endTime": "15:00",
    "classroom": "Room 206",
    "teacherId": 4,
    "batchId": 2,
    "subjectId": 2
}
```

### Result

```text
409 Conflict
```

The response indicated that the batch already had a class during that time.

### Outcome

The batch conflict detection logic is working correctly.

---

## 8. Classroom Conflict Testing

The existing timetable used:

```text
Day       : TUESDAY
Time      : 14:00 - 15:00
Classroom : Room 200
```

A new timetable was requested using the same classroom, day and time but a different teacher and batch.

### Request Body

```json
{
    "day": "TUESDAY",
    "startTime": "14:00",
    "endTime": "15:00",
    "classroom": "Room 200",
    "teacherId": 4,
    "batchId": 3,
    "subjectId": 2
}
```

### Result

```text
409 Conflict
```

The response indicated that the classroom already had a scheduled class during that time.

### Outcome

The classroom conflict detection logic is working correctly.

---

## 9. Blank Classroom Validation

The `classroom` field was intentionally sent as an empty string.

### Request Body

```json
{
    "day": "THURSDAY",
    "startTime": "10:00",
    "endTime": "11:00",
    "classroom": "",
    "teacherId": 1,
    "batchId": 4,
    "subjectId": 2
}
```

### Result

```text
400 Bad Request
```

The response indicated that the classroom field is required.

### Outcome

This confirmed that the `@NotBlank` validation on the `classroom` field is working correctly.

---

## 10. Test Results Summary

| Test Case              | Expected | Result |
| ---------------------- | -------- | ------ |
| Create valid timetable | `201`    | ✅      |
| Missing `subjectId`    | `400`    | ✅      |
| Invalid `teacherId`    | `404`    | ✅      |
| Invalid `batchId`      | `404`    | ✅      |
| Invalid `subjectId`    | `404`    | ✅      |
| Teacher conflict       | `409`    | ✅      |
| Batch conflict         | `409`    | ✅      |
| Classroom conflict     | `409`    | ✅      |
| Blank classroom        | `400`    | ✅      |

---

## 11. HTTP Status Codes Verified

```text
201 Created
→ Timetable created successfully.

400 Bad Request
→ Request validation failed.

404 Not Found
→ Referenced resource or timetable does not exist.

409 Conflict
→ Timetable violates an existing scheduling conflict rule.
```

---

## 12. Key Learnings

* DTO validation prevents invalid requests from reaching the service layer.
* `@NotNull` ensures required object fields are provided.
* `@NotBlank` prevents empty classroom values.
* Teacher, Batch and Subject IDs are validated before creating a timetable.
* Teacher conflicts are prevented.
* Batch conflicts are prevented.
* Classroom conflicts are prevented.
* Appropriate HTTP status codes make API errors easier for the frontend to handle.

---

## Outcome

The major timetable validation and business-rule checks were successfully tested.

```text
Request Validation       → Working ✅
Resource Validation      → Working ✅
Teacher Conflict         → Working ✅
Batch Conflict           → Working ✅
Classroom Conflict       → Working ✅
Error Handling           → Working ✅
```

### Day-44 Status: Completed ✅

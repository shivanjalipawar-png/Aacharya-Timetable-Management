# Day-41: Timetable Authorization Testing

## Objective

* Test role-based authorization for creating timetable entries.
* Verify that only `ADMIN` can access `POST /api/timetables`.
* Test the endpoint using `ADMIN`, `TEACHER`, and `STUDENT` JWTs.

---

## 1. Endpoint Tested

```http
POST http://localhost:8080/api/timetables
```

### Request Body

```json
{
    "day": "MONDAY",
    "startTime": "09:00",
    "endTime": "10:00",
    "classroom": "Room 101",
    "teacherId": 1,
    "batchId": 2,
    "subjectId": 2
}
```

---

## 2. ADMIN Authorization Test

Initially, the ADMIN request returned:

```text
403 Forbidden
```

Spring Boot logs showed:

```text
Authenticated user: day41admin
Authorities: [ROLE_STUDENT]
```

The database showed that `day41admin` was stored with the `STUDENT` role instead of `ADMIN`.

The role was corrected to `ADMIN` and a new JWT was generated.

After sending the request again:

```text
201 Created
```

The timetable was successfully created.

---

## 3. TEACHER Authorization Test

The same `POST /api/timetables` request was tested using a TEACHER JWT.

### Result

```text
403 Forbidden
```

TEACHER is not allowed to create timetable entries.

---

## 4. STUDENT Authorization Test

The same request was tested using a STUDENT JWT.

### Result

```text
403 Forbidden
```

STUDENT is not allowed to create timetable entries.

---

## 5. Testing Summary

| Role    | POST `/api/timetables` | Result            |
| ------- | ---------------------- | ----------------- |
| ADMIN   | Allowed                | `201 Created` ✅   |
| TEACHER | Not Allowed            | `403 Forbidden` ✅ |
| STUDENT | Not Allowed            | `403 Forbidden` ✅ |

---

## 6. Issues Faced

### Issue 1: ADMIN received 403

**Cause:** `day41admin` was stored as `STUDENT` in the database.

**Fix:** Changed the role to `ADMIN` and generated a new JWT.

### Issue 2: 400 Bad Request

**Cause:** Required fields of `TimetableRequestDTO` were not provided correctly in the JSON request body.

**Fix:** Sent all required fields with valid existing Teacher, Batch, and Subject IDs.

---

## 7. Key Learnings

* JWT authentication and role-based authorization work together.
* `403 Forbidden` occurs when the authenticated user does not have the required role.
* Changing a user's role in the database requires generating a new JWT for testing.
* `@Valid` validates the request body before the service method is executed.
* `201 Created` confirms successful timetable creation.

---

## Outcome

Successfully verified that **only ADMIN users can create timetable entries**.

```text
ADMIN    → 201 Created ✅
TEACHER  → 403 Forbidden ✅
STUDENT  → 403 Forbidden ✅
```

### Day-41 Status: Completed ✅

## Next Goal

Continue testing **role-based authorization for other Timetable APIs**, especially update and delete operations.

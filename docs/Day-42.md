# Day-42: Timetable Update Authorization Testing

## Objective

* Test role-based authorization for updating timetable entries.
* Verify that only `ADMIN` can access `PUT /api/timetables/{id}`.
* Test the endpoint using `ADMIN`, `TEACHER`, and `STUDENT` JWTs.

---

## 1. Endpoint Tested

```http
PUT http://localhost:8080/api/timetables/20
```

The existing timetable with `timetableId = 20` was used for testing.

### Request Body

```json
{
    "day": "MONDAY",
    "startTime": "10:00",
    "endTime": "11:00",
    "classroom": "Room 102",
    "teacherId": 1,
    "batchId": 2,
    "subjectId": 2
}
```

---

## 2. ADMIN Authorization Test

The request was first tested using a valid ADMIN JWT.

### Result

```text
200 OK
```

The timetable was successfully updated.

This confirmed that ADMIN users are authorized to update timetable entries.

---

## 3. TEACHER Authorization Test

The same `PUT /api/timetables/20` request was tested using a TEACHER JWT.

### Result

```text
403 Forbidden
```

The request was correctly rejected because TEACHER does not have permission to update timetable entries.

---

## 4. STUDENT Authorization Test

The same request was tested using a STUDENT JWT.

Initially, the request returned:

```text
401 Unauthorized
```

The JWT had expired, so a new STUDENT JWT was generated through the login endpoint.

After using the fresh JWT:

```text
403 Forbidden
```

The request was correctly rejected because STUDENT does not have permission to update timetable entries.

---

## 5. Issues Faced

### Issue 1: Expired ADMIN JWT

The initial ADMIN request returned:

```text
401 Unauthorized
```

The Spring Boot console showed:

```text
JWT validation failed: JWT expired
```

A new ADMIN JWT was generated and the request was tested again.

---

### Issue 2: Incorrect Endpoint URL

An initial request returned:

```text
404 Not Found
```

The URL contained a spelling mistake:

```text
/api/timetabels/20
```

The correct endpoint is:

```text
/api/timetables/20
```

After correcting the URL, the request successfully reached the controller.

---

### Issue 3: Expired STUDENT JWT

The initial STUDENT request returned:

```text
401 Unauthorized
```

A fresh STUDENT JWT was generated.

After retrying the request, the result was:

```text
403 Forbidden
```

---

## 6. Authorization Test Results

| Role    | PUT `/api/timetables/20` | Result            |
| ------- | ------------------------ | ----------------- |
| ADMIN   | Allowed                  | `200 OK` ✅        |
| TEACHER | Not Allowed              | `403 Forbidden` ✅ |
| STUDENT | Not Allowed              | `403 Forbidden` ✅ |

---

## 7. Key Learnings

* `401 Unauthorized` indicates an authentication problem such as a missing, invalid, or expired JWT.
* `403 Forbidden` indicates that authentication succeeded but the user does not have sufficient permissions.
* A new JWT must be generated after an old JWT expires.
* Endpoint spelling must exactly match the controller's `@RequestMapping`.
* Role-based authorization successfully restricts timetable updates to ADMIN users.

---

## Outcome

Successfully verified that **only ADMIN users can update timetable entries**.

```text
ADMIN    → PUT /api/timetables/20 → 200 OK ✅
TEACHER  → PUT /api/timetables/20 → 403 Forbidden ✅
STUDENT  → PUT /api/timetables/20 → 403 Forbidden ✅
```

### Day-42 Status: Completed ✅

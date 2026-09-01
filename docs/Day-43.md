# Day-43: Timetable Delete Authorization Testing

## Objective

The objective of Day-43 was to test the authorization and functionality of the Timetable DELETE API.

The main goal was to verify that:

* ADMIN can delete a timetable.
* TEACHER cannot delete a timetable.
* STUDENT cannot delete a timetable.
* The API correctly handles deletion of a non-existing timetable.

---

## 1. DELETE Timetable API

### Endpoint

```http
DELETE /api/timetables/{id}
```

The endpoint is implemented in `TimetableController` using:

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteTimetable(@PathVariable Long id) {

    timetableService.deleteTimetable(id);

    return ResponseEntity.noContent().build();
}
```

The endpoint returns `204 No Content` when the timetable is successfully deleted.

---

## 2. ADMIN DELETE Test

The DELETE API was tested using a valid ADMIN JWT.

### Request

```http
DELETE http://localhost:8080/api/timetables/20
```

### Result

```text
204 No Content
```

### Outcome

The timetable with ID `20` was successfully deleted.

This confirmed that an ADMIN user has permission to delete timetable entries.

---

## 3. TEACHER DELETE Test

The same DELETE operation was tested using a valid TEACHER JWT.

### Request

```http
DELETE http://localhost:8080/api/timetables/19
```

### Result

```text
403 Forbidden
```

### Outcome

The request was rejected because the TEACHER role does not have permission to delete timetable entries.

The timetable with ID `19` remained unchanged.

---

## 4. STUDENT DELETE Test

The same DELETE operation was tested using a valid STUDENT JWT.

### Request

```http
DELETE http://localhost:8080/api/timetables/19
```

### Result

```text
403 Forbidden
```

### Outcome

The request was correctly rejected because the STUDENT role does not have permission to delete timetable entries.

---

## 5. Non-Existing Timetable Test

After timetable ID `20` was deleted successfully, another DELETE request was sent for the same ID using an ADMIN JWT.

### Request

```http
DELETE http://localhost:8080/api/timetables/20
```

### Result

```text
404 Not Found
```

### Outcome

The API correctly reported that the timetable no longer exists.

This confirmed that the `ResourceNotFoundException` handling in the timetable service is working correctly.

---

## 6. Issue Encountered

During the initial ADMIN DELETE test, the request returned:

```text
403 Forbidden
```

The Spring Boot console showed:

```text
Authenticated user: day43admin
Authorities: [ROLE_STUDENT]
```

Although the username was intended to represent an ADMIN account, the database contained the user with the `STUDENT` role.

The role was corrected in the database from `STUDENT` to `ADMIN`.

After generating/using the correct ADMIN JWT, the DELETE request returned:

```text
204 No Content
```

This confirmed that the authorization configuration was working correctly and the issue was with the user's stored role.

---

## 7. Authorization Test Results

| Role    | DELETE `/api/timetables/{id}` | Result             |
| ------- | ----------------------------- | ------------------ |
| ADMIN   | Allowed                       | `204 No Content` ✅ |
| TEACHER | Not Allowed                   | `403 Forbidden` ✅  |
| STUDENT | Not Allowed                   | `403 Forbidden` ✅  |

---

## 8. Key Learnings

* `204 No Content` indicates that the DELETE operation was successful.
* `403 Forbidden` indicates that the user is authenticated but does not have sufficient permission.
* `404 Not Found` is returned when the requested timetable does not exist.
* The user's role stored in the database must match the intended role.
* A JWT must contain the correct role for role-based authorization to work as expected.

---

## Outcome

The Timetable DELETE API was successfully tested for all required roles and error conditions.

```text
ADMIN    → DELETE → 204 No Content ✅
TEACHER  → DELETE → 403 Forbidden  ✅
STUDENT  → DELETE → 403 Forbidden  ✅
Invalid ID → DELETE → 404 Not Found ✅
```

### Day-43 Status: Completed ✅

# Role-Based Authorization Testing

## Authentication and Authorization

Verified JWT-based authentication and role-based authorization for the Timetable Management application using Swagger and MySQL.

## Authorization Rules Tested

### Batch APIs

- ADMIN can create batches.
- ADMIN can update batches.
- ADMIN can delete batches.
- ADMIN, TEACHER, and STUDENT can view batches.
- TEACHER cannot create, update, or delete batches.
- STUDENT cannot create, update, or delete batches.

## Test Results

| User Role | Operation | Expected | Actual | Status |
|---|---|---:|---:|---|
| ADMIN | GET /batches | 200 | 200 | PASS |
| STUDENT | GET /batches | 200 | 200 | PASS |
| ADMIN | POST /batches | 201 | 201 | PASS |
| STUDENT | POST /batches | 403 | 403 | PASS |
| TEACHER | POST /batches | 403 | 403 | PASS |
| STUDENT | PUT /batches/{id} | 403 | 403 | PASS |
| ADMIN | PUT /batches/{id} | 200 | 200 | PASS |
| STUDENT | DELETE /batches/{id} | 403 | 403 | PASS |
| ADMIN | DELETE /batches/{id} | 204 | 204 | PASS |
| No JWT | GET /batches | 401 | 401 | PASS |

## 401 vs 403 Verification

- `401 Unauthorized` was verified when accessing a protected endpoint without a JWT.
- `403 Forbidden` was verified when an authenticated STUDENT or TEACHER attempted an ADMIN-only operation.
- Successful responses were verified when an ADMIN performed authorized operations.

## Database Verification

The ADMIN test account was verified in MySQL:

- Username: `day37admin`
- Role: `ADMIN`

The batch deletion test was also verified directly in MySQL to confirm that the batch was successfully removed.

## Conclusion

JWT authentication and role-based authorization are working as expected. The application correctly distinguishes between unauthenticated requests (`401`), authenticated users without sufficient permissions (`403`), and authorized requests.
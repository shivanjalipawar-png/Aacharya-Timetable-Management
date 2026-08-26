# Day-39: Subject Management Authorization Testing

## Objective

The objective of Day-39 was to verify and improve **role-based authorization for Subject Management APIs** using JWT authentication.

The Subject APIs were configured so that:

- ADMIN can create, update, and delete subjects.
- ADMIN, TEACHER, and STUDENT can view subjects.
- TEACHER and STUDENT cannot perform subject write operations.
- Unauthenticated users cannot access protected Subject APIs.

---

## 1. Subject API Authorization Rules

The following authorization rules were configured in Spring Security:

| HTTP Method | Endpoint | Allowed Roles |
|-------------|----------|---------------|
| POST | `/subjects` | ADMIN |
| PUT | `/subjects/**` | ADMIN |
| DELETE | `/subjects/**` | ADMIN |
| GET | `/subjects/**` | ADMIN, TEACHER, STUDENT |

All other requests require authentication.

---

## 2. Subject Management Controller

The `SubjectController` provides the following APIs:

### Create Subject

```http
POST /subjects
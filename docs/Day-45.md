# Day 45 – Backend Regression Testing & API Validation

## Objective

The main objective of Day 45 was to perform regression testing of the implemented backend APIs and verify that the existing functionality continues to work correctly after the previous authorization, timetable CRUD, and conflict-management changes.

The focus was mainly on GET APIs, validation, error handling, and foreign-key validation.

---

## 1. Batch API Regression Testing

The Batch APIs were tested using Postman with a valid ADMIN JWT.

### Tests Performed

- Get all batches
- Get batch by ID
- Get a non-existing batch

### Results

- Get all batches → `200 OK`
- Get batch by ID → `200 OK`
- Non-existing batch → `404 Not Found`

Batch API regression testing was successful.

---

## 2. Subject API Regression Testing

The Subject APIs were tested to verify retrieval and error handling.

### Tests Performed

- Get all subjects
- Get subject by ID
- Get a non-existing subject

### Results

- Get all subjects → `200 OK`
- Get subject by ID → `200 OK`
- Non-existing subject → `404 Not Found`

During testing, it was observed that only some subject records currently contain `subjectCode` and `credits` values. The remaining records return `null` for these fields because the corresponding database values are currently `NULL`.

This was identified as a data completeness issue and not an API failure.

---

## 3. Teacher API Regression Testing

The Teacher APIs were tested using a valid ADMIN JWT.

### Tests Performed

- Get all teachers
- Get teacher by ID
- Get a non-existing teacher
- Filter teachers by batch
- Filter teachers without parameters

### Results

- Get all teachers → `200 OK`
- Get teacher by ID → `200 OK`
- Non-existing teacher → `404 Not Found`
- Filter teachers by batch → `200 OK`
- Filter teachers without parameters → `200 OK`

Teacher API regression testing was successful.

---

## 4. Timetable GET API Regression Testing

The timetable module was tested extensively because it is the core module of the application.

### APIs Tested

- Get all timetables
- Get timetable by ID
- Get timetables by teacher
- Get timetables by batch
- Get timetables by subject
- Get timetables by day
- Get timetables by classroom
- Get timetables by time range
- Get today's timetable
- Get timetables by teacher and day
- Get timetables by batch and day
- Dynamic timetable filtering

### Results

All tested timetable GET APIs returned the expected results for valid existing data.

Most successful requests returned:

`200 OK`

For combinations where no timetable existed, the API correctly returned:

`404 Not Found`

For example, a teacher + day combination that did not exist returned a proper "No timetable found" response.

The same endpoint returned `200 OK` when an actual teacher + day combination from the database was used.

This confirmed that the timetable filtering logic is working according to the available database data.

---

## 5. Timetable Validation Testing

Validation was tested by sending an empty timetable request body.

### Request

```json
{}
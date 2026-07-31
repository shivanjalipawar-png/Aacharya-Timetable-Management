
# Day-19 Notes

## Topics Covered

- Timetable CRUD
- Service Layer
- Controller Layer
- ResponseEntity
- Swagger Documentation
- Logging
- Exception Handling

## CRUD Methods

POST

Create Timetable

GET

Get All Timetables

GET By ID

Retrieve One Timetable

PUT

Update Timetable

DELETE

Delete Timetable

## HTTP Status Codes

201 Created

200 OK

204 No Content

404 Not Found

400 Bad Request

## Logging

logger.info()

Used for

Saving

Updating

Deleting

Fetching

## Exception

ResourceNotFoundException

Thrown when

Teacher not found

Batch not found

Subject not found

Timetable not found

## ResponseEntity

POST → 201

GET → 200

PUT → 200

DELETE → 204

## Swagger

@Tag

@Operation

@ApiResponses

@ApiResponse

## Key Learning

Service contains business logic.

Controller handles HTTP requests.

Repository communicates with database.

DTO communicates with frontend.
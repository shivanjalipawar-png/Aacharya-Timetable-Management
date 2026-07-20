
# 📅 Day-06 Progress Report (18-07-2026)

## 🎯 Goal
Implement the **Batch Module** using Spring Boot and understand relationships between entities.

---

## 📚 Topics Learned

### 1. Created Batch Entity
- Created `Batch.java` as a JPA Entity.
- Used `@Entity` to map the class to a MySQL table.
- Used `@Id` and `@GeneratedValue` for the primary key.

```java
@Entity
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchId;

    private String batchName;
}
```

---

### 2. Getters and Setters
- Learned that private variables cannot be accessed directly.
- Added getters and setters to access and modify object data.
- Understood why `getBatchName()` and `setBatchName()` are required.

---

### 3. Repository Layer
Created `BatchRepository` as an interface.

```java
public interface BatchRepository extends JpaRepository<Batch, Long> {
}
```

**Purpose:**
- Performs database operations without writing SQL queries.
- Spring Data JPA automatically provides CRUD methods.

---

### 4. Service Layer
Implemented all CRUD operations.

Methods:
- `saveBatch()`
- `getBatchById()`
- `updateBatch()`
- `deleteBatch()`

**Responsibilities:**
- Receives requests from Controller.
- Contains business logic.
- Communicates with Repository.

---

### 5. Controller Layer
Created REST APIs for Batch.

Endpoints:

| Method | Endpoint | Purpose |
|---------|----------|---------|
| POST | `/batches` | Create Batch |
| GET | `/batches/{id}` | Get Batch by ID |
| PUT | `/batches/{id}` | Update Batch |
| DELETE | `/batches/{id}` | Delete Batch |

---

### 6. Entity Relationship
Started learning relationships between entities.

Added relationship in Teacher Entity:

```java
@ManyToOne
@JoinColumn(name = "batch_id")
private Batch batch;
```

Learned:
- One Batch can have many Teachers.
- `@JoinColumn` creates a foreign key in the Teacher table.
- Hibernate automatically manages relationships.

---

### 7. Debugging Experience
Faced compilation error:

```
cannot find symbol getBatchName()
```

Reason:
- Getters and setters were missing in `Batch.java`.

Solution:
- Generated getters and setters.
- Project compiled successfully.

This helped me understand how Java accesses private variables through getter and setter methods.

---

## 🔄 Request Flow

Client (Postman)
      ↓
BatchController
      ↓
BatchService
      ↓
BatchRepository
      ↓
MySQL Database

---

## ✅ Outcome

✔ Created Batch Entity

✔ Created Batch Repository

✔ Implemented Batch Service

✔ Implemented Batch Controller

✔ Understood JPA Entity Relationships

✔ Learned Foreign Key Mapping

✔ Successfully debugged a compilation error

---

## 📖 Key Learnings

- Purpose of Entity, Repository, Service, and Controller.
- Difference between getters and setters.
- Why Repository is an interface.
- How Spring Data JPA provides CRUD operations automatically.
- How `@ManyToOne` creates relationships between entities.
- Importance of reading compiler errors carefully while debugging.

---

## 🚀 Next Goal (Day-07)

- Connect Teacher and Batch completely.
- Save Teacher along with Batch.
- Retrieve Teacher with Batch details.
- Understand JSON representation of entity relationships.
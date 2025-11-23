# Posts API - Java Assessment By AspaceLife

Spring Boot REST API that fetches posts from an external API asynchronously and stores them in SQLite database with pagination support.

---

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.8+

### Run Application

```bash
mvn clean install
mvn spring-boot:run
```

Application runs on: `http://localhost:8080`

---

## API Endpoints

### 1. Batch Insert

**POST** `/api/batch_insert`

Fetches N posts from external API and saves to database.

**Request:**

```json
{
  "postNumber": 24
}
```

**Response:**

```json
{
  "success": true,
  "message": "Successfully inserted 24 posts",
  "postsInserted": 24
}
```

---

### 2. Fetch Records (Paginated)

**GET** `/api/fetch_record?page=0&size=10`

Retrieves posts from database with pagination.

**Response:**

```json
{
  "success": true,
  "content": [
    {
      "id": 1,
      "userId": 1,
      "title": "sunt aut facere...",
      "body": "quia et suscipit..."
    }
  ],
  "currentPage": 0,
  "totalPages": 3,
  "totalElements": 24,
  "pageSize": 10,
  "hasNext": true,
  "hasPrevious": false
}
```

---

## Technologies Used

- **Java 17**
- **Spring Boot 3.x** - REST API framework
- **Spring Data JPA** - Database operations
- **SQLite JDBC 3.47.2.0** - Database
- **CompletableFuture** - Async API calls
- **Lombok** - Code simplification
- **Spring Boot DevTools** - LiveReload

---

## Project Structure

```
src/main/java/com/aspacelife/java_assessment/
├── controller/PostController.java    # REST endpoints
├── service/PostService.java          # Business logic + CompletableFuture
├── repository/PostRepository.java    # Database access
├── model/Post.java                   # Entity
└── JavaAssessmentApplication.java    # Main class
```

---

## Key Implementation Details

### CompletableFuture (Async)

```java
public CompletableFuture<List<Post>> fetchPosts(int count) {
    return CompletableFuture.supplyAsync(() -> {
        Post[] posts = restTemplate.getForObject(API_URL, Post[].class);
        return Arrays.stream(posts).limit(count).collect(Collectors.toList());
    });
}
```

### Pagination

```java
Pageable pageable = PageRequest.of(page, size);
return postRepository.findAll(pageable);
```

---

## Testing with Postman

### Test 1: Insert 24 Posts

```
POST http://localhost:8080/api/batch_insert
Body: {"postNumber": 24}
```

### Test 2: Fetch Page 1

```
GET http://localhost:8080/api/batch_insert?page=0&size=10
```

### Test 3: Fetch Page 2

```
GET http://localhost:8080/api/batch_insert?page=1&size=10
```

---

## Requirements Met

✅ CompletableFuture for asynchronous API calls  
✅ SQLite database (sqlite-jdbc 3.47.2.0)  
✅ POST endpoint: batch_insert with custom count  
✅ GET endpoint: fetch_record with pagination  
✅ Clean architecture (Controller → Service → Repository)

---

## Database

SQLite database file (`posts.db`) is auto-created in project root.

**Schema:**

```sql
CREATE TABLE posts (
    user_id INTEGER,
    id INTEGER PRIMARY KEY,
    title VARCHAR(255),
    body VARCHAR(1000)
);
```

---

## Contact

**Abdulmumuni Ajoge Adavize**  
Email: abdulmumuniajoge@gmail.com
Phone: 08068365268  
Submission: November 23th, 2025

---

## 🔗 External API

Data source: [https://jsonplaceholder.typicode.com/posts](https://jsonplaceholder.typicode.com/posts)

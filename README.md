# 📦 Posts API - External API Integration with SQLite

A Spring Boot REST API that asynchronously fetches posts from an external API using CompletableFuture and stores them in an SQLite database with paginated retrieval capabilities.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [API Endpoints](#api-endpoints)
- [Usage Examples](#usage-examples)
- [Database Schema](#database-schema)
- [Technical Highlights](#technical-highlights)
- [Testing](#testing)
- [Screenshots](#screenshots)
- [Author](#author)

---

## 🎯 Overview

This project demonstrates a production-ready Spring Boot application that:

1. **Asynchronously fetches** posts from JSONPlaceholder API using `CompletableFuture`
2. **Batch inserts** specified number of posts into SQLite database
3. **Provides paginated API** to retrieve stored posts
4. Implements **clean architecture** with proper separation of concerns

**Interview Requirements Met:**

- ✅ CompletableFuture for async operations
- ✅ SQLite database integration (sqlite-jdbc 3.47.2.0)
- ✅ Batch insert endpoint
- ✅ Paginated fetch endpoint
- ✅ RESTful API design

---

## 🛠️ Technologies Used

| Technology           | Version  | Purpose               |
| -------------------- | -------- | --------------------- |
| **Java**             | 17+      | Programming Language  |
| **Spring Boot**      | 3.x      | Application Framework |
| **Spring Data JPA**  | 3.x      | Database Abstraction  |
| **SQLite JDBC**      | 3.47.2.0 | Database Driver       |
| **Lombok**           | Latest   | Boilerplate Reduction |
| **Spring Dev Tools** | Latest   |
| **Maven**            | 3.8+     | Dependency Management |
| **Postman**          | Latest   | API Testing           |

---

## ✨ Features

### 1. **Asynchronous API Calls**

- Uses `CompletableFuture.supplyAsync()` for non-blocking external API calls
- Improves application responsiveness and scalability
- Demonstrates modern Java concurrency practices

### 2. **Batch Insert Operation**

- Accepts custom number of posts to fetch (1-100)
- Validates input before processing
- Saves all posts in a single batch operation for efficiency

### 3. **Paginated Retrieval**

- Implements server-side pagination
- Returns metadata (total pages, total elements, navigation flags)
- Supports customizable page size and page number

### 4. **Robust Error Handling**

- Input validation with meaningful error messages
- Proper HTTP status codes (200, 400, 500)
- Exception handling at all layers

### 5. **Clean Architecture**

- **Controller Layer:** HTTP request handling
- **Service Layer:** Business logic
- **Repository Layer:** Database operations
- Easy to test, maintain, and extend

---

## 📁 Project Structure

```
posts-api/
├── src/
│   ├── main/
│   │   ├── java/com/aspacelife/java_assessment/
│   │   │   ├── controller/
│   │   │   │   └── PostController.java          # REST endpoints
│   │   │   ├── service/
│   │   │   │   └── PostService.java             # Business logic +
│   │   │   ├── repository/
│   │   │   │   └── PostRepository.java          # Database access
│   │   │   ├── model/
│   │   │   │   └── Post.java                    # SQLite configuration
│   │   │   └── PostsApplication.java            # Main entry point
│   │   └── resources/
│   │       └── application.properties           # Database configuration
│   └── test/                                     # Unit tests (optional)
├── posts.db                                      # SQLite database file (auto-generated)
├── pom.xml                                       # Maven dependencies
└── README.md                                     # This file
```

---

## ✅ Prerequisites

Before running this project, ensure you have:

- **Java Development Kit (JDK) 17 or higher**

  ```bash
  java -version
  # Should show: java version "17" or higher
  ```

- **Maven 3.8+**

  ```bash
  mvn -version
  # Should show: Apache Maven 3.8.x or higher
  ```

- **Postman** (for API testing)

  - Download from: https://www.postman.com/downloads/

- **Internet Connection** (to fetch from external API)

---

## 🚀 Installation & Setup

### Step 1: Clone the Repository

```bash
git clone <your-repository-url>
cd posts-api
```

### Step 2: Install Dependencies

```bash
mvn clean install
```

### Step 3: Run the Application

```bash
mvn spring-boot:run
```

**Alternative:** Run from IDE

- Open project in IntelliJ IDEA
- Run `PostsApplication.java`

### Step 4: Verify Application Started

You should see:

```
Tomcat started on port(s): 8080 (http)
Started PostsApplication in 3.5 seconds
```

### Step 5: Test Health Check (Optional)

```bash
curl http://localhost:8080/api/health
```

Expected response:

```json
{
  "status": "UP",
  "message": "Posts API is running"
}
```

---

## 🔌 API Endpoints

### Base URL

```
http://localhost:8080/api
```

---

### 1. **Batch Insert** (POST)

**Endpoint:** `POST /api/batch_insert`

**Description:** Fetches specified number of posts from external API and saves to database

**Request Headers:**

```
Content-Type: application/json
```

**Request Body:**

```json
{
  "postNumber": 24
}
```

**Request Body Parameters:**

| Parameter    | Type    | Required | Description              | Valid Range |
| ------------ | ------- | -------- | ------------------------ | ----------- |
| `postNumber` | Integer | Yes      | Number of posts to fetch | 1 - 100     |

**Success Response (200 OK):**

```json
{
  "success": true,
  "message": "Successfully inserted 24 posts",
  "postsInserted": 24
}
```

**Error Responses:**

**400 Bad Request** - Missing field:

```json
{
  "success": false,
  "message": "Missing required field: postNumber"
}
```

**400 Bad Request** - Invalid value:

```json
{
  "success": false,
  "message": "postNumber must be a positive integer"
}
```

**500 Internal Server Error** - Server error:

```json
{
  "success": false,
  "message": "Error inserting posts: ...",
  "error": "ExceptionType"
}
```

---

### 2. **Fetch Records** (GET)

**Endpoint:** `GET /api/fetch_record`

**Description:** Retrieves posts from database with pagination

**Query Parameters:**

| Parameter | Type    | Required | Default | Description              |
| --------- | ------- | -------- | ------- | ------------------------ |
| `page`    | Integer | No       | 0       | Page number (0-indexed)  |
| `size`    | Integer | No       | 10      | Records per page (1-100) |

**Example Requests:**

```bash
# Get first page (default size of 10)
GET /api/fetch_record

# Get first page with custom size
GET /api/fetch_record?page=0&size=5

# Get second page
GET /api/fetch_record?page=1&size=10

# Get third page with size 15
GET /api/fetch_record?page=2&size=15
```

**Success Response (200 OK):**

```json
{
  "success": true,
  "content": [
    {
      "id": 1,
      "userId": 1,
      "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
      "body": "quia et suscipit\nsuscipit recusandae consequuntur..."
    },
    {
      "id": 2,
      "userId": 1,
      "title": "qui est esse",
      "body": "est rerum tempore vitae..."
    }
    // ... more posts
  ],
  "currentPage": 0,
  "totalPages": 3,
  "totalElements": 24,
  "pageSize": 10,
  "hasNext": true,
  "hasPrevious": false,
  "isFirst": true,
  "isLast": false
}
```

**Response Fields:**

| Field           | Type    | Description                       |
| --------------- | ------- | --------------------------------- |
| `content`       | Array   | List of posts for current page    |
| `currentPage`   | Integer | Current page number (0-indexed)   |
| `totalPages`    | Integer | Total number of pages             |
| `totalElements` | Long    | Total number of posts in database |
| `pageSize`      | Integer | Number of posts per page          |
| `hasNext`       | Boolean | Whether next page exists          |
| `hasPrevious`   | Boolean | Whether previous page exists      |
| `isFirst`       | Boolean | Whether this is the first page    |
| `isLast`        | Boolean | Whether this is the last page     |

**Error Response (400 Bad Request):**

```json
{
  "success": false,
  "message": "Page number cannot be negative"
}
```

---

### 3. **Health Check** (GET) - Optional

**Endpoint:** `GET /api/health`

**Description:** Check if API is running

**Response (200 OK):**

```json
{
  "status": "UP",
  "message": "Posts API is running"
}
```

---

## 📝 Usage Examples

### Example 1: Insert 50 Posts

```bash
curl -X POST http://localhost:8080/api/batch_insert \
  -H "Content-Type: application/json" \
  -d '{"postNumber": 50}'
```

**Response:**

```json
{
  "success": true,
  "message": "Successfully inserted 50 posts",
  "postsInserted": 50
}
```

---

### Example 2: Fetch First Page (10 posts)

```bash
curl http://localhost:8080/api/fetch_record
```

**Response:**

```json
{
  "success": true,
  "content": [
    /* 10 posts */
  ],
  "currentPage": 0,
  "totalPages": 5,
  "totalElements": 50,
  "pageSize": 10,
  "hasNext": true,
  "hasPrevious": false
}
```

---

### Example 3: Fetch Specific Page with Custom Size

```bash
curl "http://localhost:8080/api/fetch_record?page=2&size=15"
```

**Response:**

```json
{
  "success": true,
  "content": [
    /* 15 posts */
  ],
  "currentPage": 2,
  "totalPages": 4,
  "totalElements": 50,
  "pageSize": 15,
  "hasNext": true,
  "hasPrevious": true
}
```

---

## 🗄️ Database Schema

### Table: `posts`

| Column    | Type          | Constraints | Description               |
| --------- | ------------- | ----------- | ------------------------- |
| `id`      | INTEGER       | PRIMARY KEY | Unique post identifier    |
| `user_id` | INTEGER       | -           | User who created the post |
| `title`   | VARCHAR(255)  | -           | Post title                |
| `body`    | VARCHAR(1000) | -           | Post content              |

**SQL Schema:**

```sql
CREATE TABLE posts (
    id INTEGER PRIMARY KEY,
    user_id INTEGER,
    title VARCHAR(255),
    body VARCHAR(1000)
);
```

**Auto-generated by JPA/Hibernate** - No manual table creation needed!

---

## 🔧 Technical Highlights

### 1. CompletableFuture Implementation

```java
public CompletableFuture<List<Post>> fetchPostsAsync(int count) {
    return CompletableFuture.supplyAsync(() -> {
        // Runs in separate thread (non-blocking)
        Post[] posts = restTemplate.getForObject(API_URL, Post[].class);
        return Arrays.stream(posts)
                     .limit(count)
                     .collect(Collectors.toList());
    });
}
```

**Benefits:**

- Non-blocking I/O
- Better resource utilization
- Improved application scalability

---

### 2. Batch Insert Efficiency

```java
postRepository.saveAll(posts);  // Single batch operation
```

**Instead of:**

```java
for (Post post : posts) {
    postRepository.save(post);  // Multiple database hits (slow!)
}
```

**Performance:**

- Single transaction
- Reduced database round-trips
- Faster execution

---

### 3. JPA Pagination

```java
Pageable pageable = PageRequest.of(page, size);
Page<Post> result = postRepository.findAll(pageable);
```

**Auto-generates SQL:**

```sql
SELECT * FROM posts LIMIT 10 OFFSET 0;
```

**No manual SQL needed!**

---

## 🧪 Testing

### Test Scenarios

#### Scenario 1: Successful Batch Insert

**Request:**

```json
POST /api/batch_insert
{
  "postNumber": 24
}
```

**Expected Result:**

- ✅ Status: 200 OK
- ✅ 24 posts inserted into database
- ✅ Response confirms successful insertion

---

#### Scenario 2: Invalid Input Handling

**Request:**

```json
POST /api/batch_insert
{
  "postNumber": -5
}
```

**Expected Result:**

- ✅ Status: 400 Bad Request
- ✅ Clear error message returned
- ✅ No database changes

---

#### Scenario 3: Pagination Navigation

**Requests:**

```
1. GET /api/fetch_record?page=0&size=10
2. GET /api/fetch_record?page=1&size=10
3. GET /api/fetch_record?page=2&size=10
```

**Expected Results:**

- ✅ Page 0: Posts 1-10, hasNext=true, hasPrevious=false
- ✅ Page 1: Posts 11-20, hasNext=true, hasPrevious=true
- ✅ Page 2: Posts 21-24, hasNext=false, hasPrevious=true

---

### Postman Collection

You can import this collection for quick testing:

**Create a new Postman collection with these requests:**

1. **Health Check**

   - Method: GET
   - URL: `http://localhost:8080/api/health`

2. **Batch Insert - 24 Posts**

   - Method: POST
   - URL: `http://localhost:8080/api/batch_insert`
   - Body (JSON):
     ```json
     { "postNumber": 24 }
     ```

3. **Fetch Page 1**

   - Method: GET
   - URL: `http://localhost:8080/api/fetch_record?page=0&size=10`

4. **Fetch Page 2**

   - Method: GET
   - URL: `http://localhost:8080/api/fetch_record?page=1&size=10`

5. **Invalid Input Test**
   - Method: POST
   - URL: `http://localhost:8080/api/batch_insert`
   - Body (JSON):
     ```json
     { "postNumber": -5 }
     ```

---

## 📸 Screenshots

### 1. Successful Batch Insert

![Batch Insert Success](path/to/screenshot1.png)

### 2. Paginated Fetch - Page 1

![Fetch Page 1](path/to/screenshot2.png)

### 3. Paginated Fetch - Page 2

![Fetch Page 2](path/to/screenshot3.png)

### 4. Error Handling - Invalid Input

![Error Handling](path/to/screenshot4.png)

### 5. Database File Created

![Database File](path/to/screenshot5.png)

---

## 🔍 Configuration Details

### application.properties

```properties
# SQLite Database Configuration
spring.datasource.url=jdbc:sqlite:posts.db
spring.datasource.driver-class-name=org.sqlite.JDBC

# JPA/Hibernate Configuration
spring.jpa.database-platform=com.example.posts.config.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Logging (Optional)
logging.level.org.hibernate.SQL=DEBUG
```

---

## 🐛 Troubleshooting

### Issue 1: Port 8080 Already in Use

**Solution:**

```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <process_id> /F

# Mac/Linux
lsof -ti:8080 | xargs kill -9
```

---

### Issue 2: Database File Not Created

**Solution:**

- Check `application.properties` configuration
- Ensure `ddl-auto=update` is set
- Verify SQLiteDialect class exists

---

### Issue 3: CompletableFuture Not Working

**Solution:**

- Check internet connection
- Verify API URL is accessible
- Check console logs for exceptions

---

## 📚 External Resources

- **External API Used:** [JSONPlaceholder](https://jsonplaceholder.typicode.com/posts)
- **Spring Boot Docs:** [spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
- **SQLite JDBC:** [github.com/xerial/sqlite-jdbc](https://github.com/xerial/sqlite-jdbc)

---

## 👤 Author

**Abdulmumuni Ajoge Adavize**

- **Email:** adbulmumuniajoge@gmail.com
- **Interview Date:** November 24, 2025
- **Company:** Aspacelife Technology Ltd

---

## 📄 License

This project was created for interview assessment purposes.

---

## 🙏 Acknowledgments

- **Spring Boot Team** - Framework
- **JSONPlaceholder** - Free fake API for testing
- **SQLite Team** - Lightweight database
- **Aspacelife Technology Ltd** - Interview opportunity

---

## 📞 Support

For questions or issues related to this submission:

- Email: adbulmumuniajoge@gmail.com
- Submission Date: November 24, 2025, 8:00 AM

---

**Last Updated:** November 23, 2025

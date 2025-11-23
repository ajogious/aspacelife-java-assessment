package com.aspacelife.java_assessment.controller;

import com.aspacelife.java_assessment.model.Post;
import com.aspacelife.java_assessment.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // POST API to insert
    @PostMapping("/batch_insert")
    public ResponseEntity<Map<String, Object>> batchInsert(@RequestBody Map<String, Integer> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validating input
            if (!request.containsKey("postNumber")) {
                response.put("success", false);
                response.put("message", "Missing required field: postNumber");
                return ResponseEntity.badRequest().body(response);
            }

            Integer postNumber = request.get("postNumber");

            // Validating postNumber is positive
            if (postNumber == null || postNumber <= 0) {
                response.put("success", false);
                response.put("message", "postNumber must be a positive integer");
                return ResponseEntity.badRequest().body(response);
            }

            // Validating postNumber doesn't exceed available posts
            if (postNumber > 100) {
                response.put("success", false);
                response.put("message", "postNumber cannot exceed 100 (API limit)");
                return ResponseEntity.badRequest().body(response);
            }

            // Calling service to fetch and save posts
            postService.batchInsert(postNumber);

            // Success response
            response.put("success", true);
            response.put("message", "Successfully inserted " + postNumber + " posts");
            response.put("postsInserted", postNumber);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Error response
            response.put("success", false);
            response.put("message", "Error inserting posts: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // GET API to get records
    @GetMapping("/fetch_record")
    public ResponseEntity<Map<String, Object>> fetchRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Validating pagination parameters
            if (page < 0) {
                response.put("success", false);
                response.put("message", "Page number cannot be negative");
                return ResponseEntity.badRequest().body(response);
            }

            if (size <= 0 || size > 100) {
                response.put("success", false);
                response.put("message", "Size must be between 1 and 100");
                return ResponseEntity.badRequest().body(response);
            }

            // Fetching paginated records from database
            Page<Post> postsPage = postService.fetchRecords(page, size);

            // Building response with pagination metadata
            response.put("success", true);
            response.put("content", postsPage.getContent());
            response.put("currentPage", postsPage.getNumber());
            response.put("totalPages", postsPage.getTotalPages());
            response.put("totalElements", postsPage.getTotalElements());
            response.put("pageSize", postsPage.getSize());
            response.put("hasNext", postsPage.hasNext());
            response.put("hasPrevious", postsPage.hasPrevious());
            response.put("isFirst", postsPage.isFirst());
            response.put("isLast", postsPage.isLast());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching records: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // GET /api/health
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Posts API is running");
        return ResponseEntity.ok(response);
    }
}
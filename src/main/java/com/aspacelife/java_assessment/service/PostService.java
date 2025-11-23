package com.aspacelife.java_assessment.service;

import com.aspacelife.java_assessment.model.Post;
import com.aspacelife.java_assessment.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final String API_URL = "https://jsonplaceholder.typicode.com/posts";

    private final PostRepository postRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    // It fetch posts from external API asynchronously
    public CompletableFuture<List<Post>> fetchPosts(int count) {
        return CompletableFuture.supplyAsync(() -> {
            // This runs in a separate thread
            System.out.println("Fetching posts from API in thread: " +
                    Thread.currentThread().getName());

            // Calling external API
            Post[] posts = restTemplate.getForObject(API_URL, Post[].class);

            // Limited to requested number needed
            return Arrays.stream(posts)
                    .limit(count)
                    .collect(Collectors.toList());
        });
    }

    // Fetching posts and save to database
    public void batchInsert(int postNumber) throws Exception {
        System.out.println("Starting batch insert for " + postNumber + " posts");

        // Fetching posts asynchronously
        CompletableFuture<List<Post>> future = fetchPosts(postNumber);

        // Waiting for the result and then save to database
        List<Post> posts = future.get();

        System.out.println("Fetched " + posts.size() + " posts, saving to database...");

        // Saving all posts at once
        postRepository.saveAll(posts);

        System.out.println("Successfully saved " + posts.size() + " posts!");
    }


    /// Fetching records from database in pagination
    public Page<Post> fetchRecords(int page, int size) {
        System.out.println("Fetching page " + page + " with size " + size);

        // Create pageable object
        Pageable pageable = PageRequest.of(page, size);

        // Fetch from database
        return postRepository.findAll(pageable);
    }
}
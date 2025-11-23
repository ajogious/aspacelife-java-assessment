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

    // This fetch posts from external API asynchronously
    public CompletableFuture<List<Post>> fetchPosts(int count) {
        return CompletableFuture.supplyAsync(() -> {
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
        // Fetching posts asynchronously
        CompletableFuture<List<Post>> future = fetchPosts(postNumber);

        // Waiting for the result and then save to database
        List<Post> posts = future.get();

        // Saving all posts at once
        postRepository.saveAll(posts);

    }

    /// Fetching records from database in pagination
    public Page<Post> fetchRecords(int page, int size) {
        // Create pageable object
        Pageable pageable = PageRequest.of(page, size);

        // Fetch from database
        return postRepository.findAll(pageable);
    }
}
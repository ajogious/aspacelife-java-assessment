package com.aspacelife.java_assessment;

import com.aspacelife.java_assessment.model.Post;
import com.aspacelife.java_assessment.service.PostService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;

@SpringBootApplication
public class JavaAssessmentApplication {

	public static void main(String[] args) throws Exception {

        ApplicationContext context = SpringApplication.run(JavaAssessmentApplication.class, args);

        PostService service = context.getBean(PostService.class);

        // Test 1: Batch inserting
        System.out.println("\n=== TEST 1: Batch Insert ===");
        service.batchInsert(5);

        // Test 2: Fetching records with pagination
        System.out.println("\n=== TEST 2: Fetch Records ===");
        Page<Post> page = service.fetchRecords(0, 2);
        System.out.println("Total posts: " + page.getTotalElements());
        System.out.println("Total pages: " + page.getTotalPages());
        System.out.println("Current page: " + page.getNumber());
        System.out.println("Posts on this page:");
        page.getContent().forEach(post ->
                System.out.println("  - " + post.getId() + ": " + post.getTitle())
        );
	}

}

package com.aspacelife.java_assessment;

import com.aspacelife.java_assessment.model.Post;
import com.aspacelife.java_assessment.repository.PostRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class JavaAssessmentApplication {

	public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(JavaAssessmentApplication.class, args);

        PostRepository repo = context.getBean(PostRepository.class);

        // this will create and save a test post
        Post testPost = new Post(999, 1, "Test Title", "Test Body");
        repo.save(testPost);

        // this is to verify it was saved
        System.out.println("Total posts in DB: " + repo.count());
        System.out.println("Saved post: " + repo.findById(999).orElse(null));
	}

}

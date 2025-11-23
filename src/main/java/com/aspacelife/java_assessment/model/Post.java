package com.aspacelife.java_assessment.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private Integer userId;

    @Id
    private Integer id;

    private String title;

    @Column(length = 1000)
    private String body;
}

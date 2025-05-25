// src/main/java/com/example/loginboard/repository/PostRepository.java
package com.example.login_board.repository;

import com.example.login_board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

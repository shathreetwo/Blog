package com.example.login_board.repository;

import com.example.login_board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 필요 시 사용자 정의 쿼리 추가 가능
}

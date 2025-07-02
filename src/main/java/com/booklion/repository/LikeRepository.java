package com.booklion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booklion.model.entity.Like;
import com.booklion.model.entity.Post;
import com.booklion.model.entity.Questions;
import com.booklion.model.entity.Users;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // Q&A용
    boolean existsByUserAndQuestion(Users user, Questions question);
    int countByQuestion(Questions question);

    // 게시글용
    boolean existsByUserAndPost(Users user, Post post);
    int countByPost(Post post);
}


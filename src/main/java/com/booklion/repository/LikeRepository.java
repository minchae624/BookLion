package com.booklion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    
    // 마이페이지 - like로 post찾기 like로 question찾기
    @Query("SELECT l.post FROM Like l WHERE l.user = :user AND l.post IS NOT NULL")
    List<Post> findLikedPostsByUser(@Param("user") Users user);

    @Query("SELECT l.question FROM Like l WHERE l.user = :user AND l.question IS NOT NULL")
    List<Questions> findLikedQuestionsByUser(@Param("user") Users user);

    // 회원탈퇴 - user로 like 삭제
    void deleteLikesByUser(Users user);
}


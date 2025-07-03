package com.booklion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booklion.model.entity.Reply;
import com.booklion.model.entity.Users;
import com.booklion.model.entity.Post;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
	
    List<Reply> findByPost(Post post);
    
    // mypage - 내가 작성한 댓글(유저네임으로 찾기) 
    List<Reply> findByUser(Users user);
    
    int countByPost(Post post);

    void deleteByPost(Post post);
    
    void deleteByUser(Users user);
}
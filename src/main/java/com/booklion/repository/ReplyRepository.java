package com.booklion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.booklion.model.entity.Reply;
import com.booklion.model.entity.Post;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
	
    List<Reply> findByPost(Post post);
    
    int countByPost(Post post);

    void deleteByPost(Post post);
}
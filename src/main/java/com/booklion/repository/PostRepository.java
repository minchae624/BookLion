package com.booklion.repository;

import com.booklion.model.entity.Post;
import com.booklion.model.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	Page<Post> findByTitleContaining(String title, Pageable pageable);

	Page<Post> findByContentContaining(String content, Pageable pageable);

	Page<Post> findByBooktitleContaining(String bookTitle, Pageable pageable);

	Page<Post> findByAuthorContaining(String author, Pageable pageable);

	List<Post> findByUser(Users user);
}

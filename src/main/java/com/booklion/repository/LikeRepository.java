package com.booklion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booklion.model.entity.Like;
import com.booklion.model.entity.Questions;
import com.booklion.model.entity.Users;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {

	 boolean existsByUserAndQuestion(Users user, Questions question);

	    Optional<Like> findByUserAndQuestion(Users user, Questions question);
}

package com.booklion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booklion.model.entity.Answers;
import com.booklion.model.entity.Users;

public interface AnswerRepositoty extends JpaRepository<Answers, Long>{

	List<Answers> findByUser(Users user);
	
}

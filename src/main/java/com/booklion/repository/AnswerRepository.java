package com.booklion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booklion.model.entity.Answers;

@Repository
public interface AnswerRepository extends JpaRepository<Answers, Integer> {

	List<Answers> findByQuestion_QuestId(Integer questId);
	List<Answers> findByUser_UserId(Integer userId);

	
}

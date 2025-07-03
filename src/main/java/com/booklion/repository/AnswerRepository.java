package com.booklion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booklion.model.entity.Answers;
import com.booklion.model.entity.Questions;

@Repository
public interface AnswerRepository extends JpaRepository<Answers, Long>{
	List<Answers> findByQuestion(Questions question);

    int countByQuestion(Questions question);

    void deleteByQuestion(Questions question);
}

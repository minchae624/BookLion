package com.booklion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.booklion.model.entity.Questions;
import com.booklion.repository.QuestionRepository;

import jakarta.transaction.Transactional;

@Service
public class QuestionService {

	private final QuestionRepository questionRepository;

	@Autowired
	public QuestionService(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}

	public List<Questions> getAllQuestions() {
		return questionRepository.findAllByOrderByQuestIdDesc();
	}

	@Transactional
	public Questions saveQuestion(Questions question) {
		return questionRepository.save(question);
	}

	public void deleteQuestion(Integer id) {
		questionRepository.deleteById(id);
	}

	public Questions getQuestionById(Integer id) {
		return questionRepository.findById(id).orElse(null);
	}

	@Transactional
	public Questions getQuestionView(Integer id) {
		Questions question = questionRepository.findById(id).orElse(null);
		if (question == null) {
			return null;
		}
		question.recordView();
		return question;
	}
}

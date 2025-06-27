package com.booklion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.booklion.model.entity.Answers;
import com.booklion.repository.AnswerRepository;

@Service
public class AnswerService {

	private final AnswerRepository answerRepository;

	@Autowired
	public AnswerService(AnswerRepository answerRepository) {
		this.answerRepository = answerRepository;
	}

	public List<Answers> getAnswersByQuestionId(Integer questionId) {
		return answerRepository.findByQuestion_QuestId(questionId);
	}

	public Answers saveAnswer(Answers answer) {
		return answerRepository.save(answer);
	}

	public void deleteAnswer(Integer id) {
		answerRepository.deleteById(id);
	}

	public Answers getAnswerById(Integer id) {
		return answerRepository.findById(id).orElse(null);
	}

	public List<Answers> getAnswersByUserId(Integer userId) {
		return answerRepository.findByUser_UserId(userId);
	}

}

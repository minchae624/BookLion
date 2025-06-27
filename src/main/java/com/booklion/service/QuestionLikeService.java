package com.booklion.service;

import org.springframework.stereotype.Service;

import com.booklion.model.entity.Like;
import com.booklion.model.entity.Questions;
import com.booklion.model.entity.Users;
import com.booklion.repository.LikeRepository;
import com.booklion.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionLikeService {

	private final QuestionRepository questionRepository;
	private final LikeRepository likeRepository;

	public void likeQuestion(Integer questionId, Users user) {
		Questions question = questionRepository.findById(questionId).orElse(null);
		if (question == null) {
			throw new IllegalArgumentException("없는 질문입니다.");
		}

		if (likeRepository.existsByUserAndQuestion(user, question)) {
	        return;
	    }

		Like like = Like.forQuestion(user, question);
		question.increaseLike();
		likeRepository.save(like);
	}
}

package com.booklion.service;

import java.util.List;

import com.booklion.dto.QuestionsResponseDto;
import com.booklion.model.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.booklion.model.entity.Category;
import com.booklion.model.entity.Like;
import com.booklion.model.entity.Questions;
import com.booklion.model.entity.Users;

import com.booklion.repository.CategoryRepository;
import com.booklion.repository.LikeRepository;
import com.booklion.repository.QuestionRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

	private final QuestionRepository questionRepository;
	private final LikeRepository likeRepository;
	private final CategoryRepository categoryRepository;

	private final CategoryService categoryService;

	public List<Questions> getAllQuestions() {
		return questionRepository.findAllByOrderByQuestIdDesc();
	}

	@Transactional
	public Questions saveQuestion(Questions question) {
		return questionRepository.save(question);
	}

	@Transactional
	public void deleteQuestion(Integer id) {
		likeRepository.deleteByQuestion_QuestId(id);
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
	
	public void update(Integer id, Questions updated) {
	    Questions existing = questionRepository.findById(id).orElseThrow();

	    if (updated.getCategoryId() != null) {
	    	 existing.setCategoryId(updated.getCategoryId()); 
	    }
	    existing.setTitle(updated.getTitle());
	    existing.setContent(updated.getContent());
	    existing.setStatus(updated.getStatus());

	    questionRepository.save(existing);
	}



	public void delete(Integer id) {
	    questionRepository.deleteById(id);
	}

	public Questions findById(Integer id) {
	    return questionRepository.findById(id).orElseThrow();
	}

	public boolean likeQuestion(Integer questionId, Users user) {
	    Questions question = questionRepository.findById(questionId)
	        .orElseThrow(() -> new EntityNotFoundException("질문이 존재하지 않습니다."));

	    if (likeRepository.existsByUserAndQuestion(user, question)) {
	        return false;
	    }

	    Like like = Like.forQuestion(user, question);
	    likeRepository.save(like);
	    question.increaseLike();
	    questionRepository.save(question);
	    return true;
	}


	public Page<Questions> getPageQuestions(Pageable pageable, Integer categoryId, String input) {
	    boolean hasCategory = categoryId != null;
	    boolean hasInput = input != null && !input.trim().isBlank(); // ← 수정

	    if (hasCategory && hasInput) {
	        return questionRepository.findByCategoryIdAndInput(categoryId, "%" + input + "%", pageable);
	    } else if (hasCategory) {
	        return questionRepository.findByCategoryId(pageable, categoryId);
	    } else if (hasInput) {
	        return questionRepository.findByInput("%" + input + "%", pageable);
	    } else {
	        return questionRepository.findAll(pageable); // <-- 진짜 전체 조회
	    }
	}

	/* 안형준 페이징 추가 */
	public Page<QuestionsResponseDto> search(Pageable pageable, Integer categoryId, String input) {
		Page<Questions> questions;

		// 검색어가 null일 경우 빈 문자열로 처리 (NPE 방지)
		if (input == null) input = "";

		if (categoryId != null && categoryId > 0) {
			// 카테고리 ID가 있을 경우
			Category category = categoryService.getCategoryById(categoryId);
			questions = questionRepository.findByCategoryIdAndTitleContaining(categoryId, input, pageable);

		} else {
			// 카테고리 ID가 없을 경우 제목만 검색
			questions = questionRepository.findByTitleContaining(input, pageable);

		}
		return questions.map(quest -> new QuestionsResponseDto(

				quest.getQuestId(),
				quest.getCategoryId(),
				quest.getTitle(),
				quest.getUser().getUsername(),
				quest.getWritingtime(),
				quest.getStatus() == QuestionStatus.solved,
				quest.getViewCount(),
				quest.getLikeCount()
		));

	}

	@Transactional
	public void deleteAllByuser(Users user) {
		questionRepository.deleteAllByUser(user);
	}

}

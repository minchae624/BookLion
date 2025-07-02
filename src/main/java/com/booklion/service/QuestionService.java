package com.booklion.service;

import java.util.List;

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

	public List<Questions> searchByCategoryAndKeyword(Integer categoryId, String keyword) {
	    return questionRepository.searchByCategoryAndKeyword(categoryId,keyword);
	}


	public Page<Questions> getPageQuestions(String keyword, String input, Pageable pageable) {
	    if (keyword != null && input != null && !input.trim().isEmpty()) {
	        return questionRepository.searchWithPaging(input, pageable);
	    }
	    return questionRepository.findAllWithCategoryAndUser(pageable);
	}




}

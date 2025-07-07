package com.booklion.service;

import java.util.ArrayList;
import java.util.List;

import com.booklion.dto.QuestionsResponseDto;
import com.booklion.model.entity.*;
import com.booklion.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
		if (question == null) return null;
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

	public Page<Questions> getPageQuestions(String input, Integer categoryId, String status, Pageable pageable) {
		Specification<Questions> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (input != null && !input.isBlank()) {
				Predicate title = cb.like(root.get("title"), "%" + input + "%");
				Predicate content = cb.like(root.get("content"), "%" + input + "%");
				Predicate writer = cb.like(root.get("user").get("username"), "%" + input + "%");
				predicates.add(cb.or(title, content, writer));
			}
			if (categoryId != null) {
				predicates.add(cb.equal(root.get("categoryId"), categoryId));
			}
			if (status != null && !status.isBlank()) {
				predicates.add(cb.equal(root.get("status"), QuestionStatus.valueOf(status)));
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};

		return questionRepository.findAll(spec, pageable);
	}

	public Page<QuestionsResponseDto> search(Pageable pageable, Integer categoryId, String input, String status) {
		Page<Questions> questions = getPageQuestions(input, categoryId, status, pageable);

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
		List<Questions> questions = questionRepository.findAllByUser(user);
		if (!questions.isEmpty()) {
			likeRepository.deleteAllByQuestionIn(questions);  // 질문에 달린 좋아요 먼저 삭제
			questionRepository.deleteAll(questions);           // 질문 삭제
		}
	}
}

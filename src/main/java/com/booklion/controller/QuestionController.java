package com.booklion.controller;

import com.booklion.model.entity.Category;
import com.booklion.model.entity.Questions;
import com.booklion.model.entity.Users;
import com.booklion.model.entity.QuestionStatus;
import com.booklion.repository.CategoryRepository;
import com.booklion.repository.QuestionRepository;
import com.booklion.service.QuestionService;
import com.booklion.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionService questionService;
	private final QuestionRepository questionRepository;
	private final CategoryRepository categoryRepository;
	private final UserService userService;

	@GetMapping("/questions/write")
	public String showWriteForm(Model model) {
		model.addAttribute("question", new Questions());
		model.addAttribute("categories", categoryRepository.findAll());
		return "qna/qna_write";
	}

	@PostMapping("/questions")
	public ResponseEntity<?> createQuestion(@RequestBody Map<String, Object> payload,
			@RequestHeader("Authorization") String token) {
		String title = (String) payload.get("title");
		String content = (String) payload.get("content");
		Long categoryId = Long.parseLong(payload.get("categoryId").toString());

		Users user = userService.getUserInfoFromToken(token.replace("Bearer ", ""));
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));

		Questions question = Questions.builder()
				.title(title)
				.content(content)
				.category(category)
				.user(user)
				.status(QuestionStatus.unsolved)
				.viewCount(0)
				.likeCount(0)
				.writingtime(LocalDateTime.now())
				.build();

		Questions saved = questionService.saveQuestion(question);

		return ResponseEntity.ok(Map.of("id", saved.getQuestId()));
	}

	@GetMapping("/questions")
	@ResponseBody
	public List<Questions> listQuestions() {
	    return questionRepository.findAllWithCategoryAndUser();
	}
	
	@PutMapping("/questions/{id}")
	@ResponseBody
	public Questions getQuestionAndIncreaseView(@PathVariable Integer id) {
	    Questions question = questionRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("질문이 존재하지 않습니다."));
	    question.recordView();
	    return questionRepository.save(question);
	}
	
	@GetMapping("/qna")
	public String showQuestionList(Model model) {
	    List<Questions> questions = questionRepository.findAllWithCategoryAndUser();
	    model.addAttribute("questions", questions);
	    return "qna/qna"; 
	}


	@GetMapping("/qna_detail")
	public String showQnaDetail(@RequestParam("id") Integer id, Model model) {
	    Questions question = questionRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("질문이 존재하지 않습니다."));
	    question.recordView(); // 조회수 증가
	    questionRepository.save(question);
	    model.addAttribute("question", question);
	    return "qna/qna_detail";
	}





}

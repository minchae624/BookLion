package com.booklion.controller;

import com.booklion.dto.QuestionsResponseDto;
import com.booklion.model.entity.*;
import com.booklion.repository.*;
import com.booklion.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionService questionService;
	private final QuestionRepository questionRepository;
	private final CategoryRepository categoryRepository;
	private final AnswerService answerService;
	private final AnswerRepository answerRepository;

	// 질문 작성 폼
	@GetMapping("/questions/write")
	public String showWriteForm(Model model) {
		model.addAttribute("question", new Questions());
		model.addAttribute("categories", categoryRepository.findAll());
		return "qna/qna_write";
	}

	// 질문 작성 처리
	@PostMapping("/questions/write")
	public String createQuestion(@RequestParam String title, @RequestParam String content,
			@RequestParam Integer categoryId, @SessionAttribute("loginUser") Users loginUser) {

		// Category를 categoryId로 찾기
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리입니다."));

		Questions question = new Questions();
		question.setTitle(title);
		question.setContent(content);
		question.setCategoryId(categoryId);
		question.setUser(loginUser);
		question.setWritingtime(LocalDateTime.now());
		question.setStatus(QuestionStatus.unsolved);
		question.setViewCount(0);
		question.setLikeCount(0);

		questionRepository.save(question);

		return "redirect:/qna_detail?id=" + question.getQuestId();
	}

	/* 질문 목록 페이징 */
	@GetMapping("/api/qna")
	public String getQuest(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Integer categoryId,
			@RequestParam(required = false) String input) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "questId")); // id 내림차순 정렬
		Page<QuestionsResponseDto> dto = questionService.search(pageable, categoryId, input);
		model.addAttribute("dto", dto.getContent());
		model.addAttribute("page", dto); // 페이지 정보
		model.addAttribute("input", input); // 검색어 유지
		model.addAttribute("categoryId", categoryId);
		return "qna/qna";
	}

	@GetMapping("/questions")
	@ResponseBody
	public Page<Questions> getQuestions(@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String input, @RequestParam(required = false) Integer categoryId) {

		Pageable pageable = PageRequest.of(page, 10, Sort.by("questId").descending());
		return questionService.getPageQuestions(pageable, categoryId, input);
	}

	// 질문 상세
	@GetMapping("/qna_detail")
	public String showQnaDetail(@RequestParam("id") Integer id,
			@RequestParam(value = "view", defaultValue = "true") boolean shouldIncreaseView, HttpSession session,
			Model model) {

		Users loginUser = (Users) session.getAttribute("loginUser");

		Questions question = questionRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("질문이 존재하지 않습니다."));

		Map<Integer, String> categoryMap = Map.of(1, "책 추천", 2, "이벤트", 3, "기타");
		String categoryName = categoryMap.getOrDefault(question.getCategoryId(), "알 수 없음");

		List<Answers> answers = answerService.getAnswersByQuestion(id);

	    String viewKey = "viewed_" + id;
	    if (session.getAttribute(viewKey) == null) {
	        question.recordView();
	        questionRepository.save(question);
	        session.setAttribute(viewKey, true);
	    }

		model.addAttribute("question", question);
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("answers", answerService.getAnswersByQuestion(id));
		return "qna/qna_detail";
	}

	// 좋아요
	@PostMapping("/questions/{id}/like")
	public String likeQuestion(@PathVariable Integer id, @SessionAttribute("loginUser") Users loginUser,
			RedirectAttributes redirectAttributes) {

		boolean liked = questionService.likeQuestion(id, loginUser);
		if (liked) {
			redirectAttributes.addFlashAttribute("message", "좋아요를 눌렀습니다.");
		} else {
			redirectAttributes.addFlashAttribute("message", "이미 좋아요를 누르셨습니다.");
		}
		return "redirect:/qna_detail?id=" + id + "&view=false";
	}

	// 수정 폼
	@GetMapping("/questions/edit/{id}")
	public String showEditForm(@PathVariable Integer id, Model model, @SessionAttribute("loginUser") Users loginUser) {
		Questions question = questionService.findById(id);
		model.addAttribute("question", question);
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("loginUser", loginUser);
		return "qna/qna_edit";
	}

	// 수정 처리
	@PostMapping("/questions/edit/{id}")
	public String updateQuestion(@PathVariable Integer id, @RequestParam String title, @RequestParam Integer categoryId,
			@RequestParam String content, @SessionAttribute("loginUser") Users loginUser) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리입니다."));

		Questions question = questionRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));

		question.setTitle(title);
		question.setContent(content);
		question.setCategoryId(categoryId);
		question.setUser(loginUser);

		questionRepository.save(question);

		return "redirect:/qna_detail?id=" + id + "&view=false";
	}

	// 삭제
	@PostMapping("/questions/delete/{id}")
	public String deleteQuestion(@PathVariable Integer id) {
		questionService.deleteQuestion(id);

		return "redirect:/api/qna";
	}
}

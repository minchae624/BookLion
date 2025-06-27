package com.booklion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.booklion.dto.LikeCountDto;
import com.booklion.dto.QuestionRequestDto;
import com.booklion.model.entity.Questions;
import com.booklion.service.QuestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody QuestionRequestDto dto) {
        Questions saved = questionService.saveQuestion(dto.toEntity());
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer categoryId,
                                    @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(questionService.searchQuestions(categoryId, keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Questions question = questionService.getQuestionView(id);
        if (question == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(question);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody QuestionRequestDto dto) {
        Questions updated = questionService.updateQuestion(id, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/likes")
    public ResponseEntity<?> getLikeCount(@PathVariable Integer id) {
        Questions question = questionService.getQuestionById(id);
        if (question == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new LikeCountDto(question.getLikeCount()));
    }

}

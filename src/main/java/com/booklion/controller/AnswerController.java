package com.booklion.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.booklion.dto.AnswerRequestDto;
import com.booklion.dto.AnswerResponseDto;
import com.booklion.model.entity.Answers;
import com.booklion.service.AnswerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AnswerController {

    private final AnswerService answerService;

    // 답변 등록
    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<AnswerResponseDto> createAnswer(@PathVariable Integer questionId,
                                                          @RequestBody AnswerRequestDto dto) {
        Answers answer = answerService.createAnswer(questionId, dto);
        return ResponseEntity.ok(new AnswerResponseDto(
                answer.getAnswerId().longValue(),
                questionId,
                answer.getUser().getUserId(),
                answer.getUser().getUsername(),
                answer.getContent(),
                answer.getWritingtime()
        ));
    }

    // 답변 목록
    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<List<AnswerResponseDto>> getAnswers(@PathVariable Integer questionId) {
        List<Answers> list = answerService.getAnswersByQuestion(questionId);
        List<AnswerResponseDto> responseList = list.stream()
                .map(a -> new AnswerResponseDto(
                        a.getAnswerId().longValue(),
                        questionId.intValue(),
                        a.getUser().getUserId(),
                        a.getUser().getUsername(),
                        a.getContent(),
                        a.getWritingtime()))
                .toList();

        return ResponseEntity.ok(responseList);
    }

    // 답변 수정
    @PutMapping("/answers/{answerId}")
    public ResponseEntity<AnswerResponseDto> update(@PathVariable Long answerId,
                                                    @RequestBody Map<String, String> body) {
        Answers updated = answerService.updateAnswer(answerId, body.get("content"));
        return ResponseEntity.ok(new AnswerResponseDto(
                updated.getAnswerId().longValue(),
                updated.getQuestion().getQuestId(),
                updated.getUser().getUserId(),
                updated.getUser().getUsername(),
                updated.getContent(),
                updated.getWritingtime()
        ));
    }

    // 답변 삭제
    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<Void> delete(@PathVariable Long answerId) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.noContent().build();
    }
}

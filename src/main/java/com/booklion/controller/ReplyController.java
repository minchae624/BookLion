package com.booklion.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.booklion.dto.ReplyRequestDto;
import com.booklion.dto.ReplyResponseDto;
import com.booklion.model.entity.Reply;
import com.booklion.service.ReplyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplyController {

    private final ReplyService replyService;
    
    // 댓글 작성
    @PostMapping("/posts/{postId}/replies")
    public ResponseEntity<ReplyResponseDto> createReply(@PathVariable Long postId,
                                                        @RequestBody ReplyRequestDto replyRequestDto) {
        Reply reply = replyService.createReply(postId, replyRequestDto);
        ReplyResponseDto responseDto = new ReplyResponseDto(
            reply.getReply_id(),
            reply.getPost().getPostId(),
            reply.getUser().getUserId().longValue(),
            reply.getContent(),
            reply.getWritingtime()
        );
        return ResponseEntity.ok(responseDto);
    }
    
    // 댓글 목록 조회
    @GetMapping("/posts/{postId}/replies")
    public ResponseEntity<List<ReplyResponseDto>> getReplies(@PathVariable Long postId) {
        List<Reply> replies = replyService.getRepliesByPost(postId);

        List<ReplyResponseDto> responseDtos = replies.stream()
            .map(reply -> new ReplyResponseDto(
                reply.getReply_id(),
                reply.getPost().getPostId(),
                reply.getUser().getUserId().longValue(),
                reply.getUser().getUsername(),
                reply.getContent(),
                reply.getWritingtime()
            ))
            .toList();

        return ResponseEntity.ok(responseDtos);
    }

    // 댓글 개수 조회
    @GetMapping("/posts/{postId}/replies_count")
    public ResponseEntity<Integer> getReplyCount(@PathVariable Long postId) {
        return ResponseEntity.ok(replyService.getReplyCount(postId));
    }

    // 댓글 수정
    @PutMapping("/replies/{replyId}")
    public ResponseEntity<ReplyResponseDto> updateReply(@PathVariable Long replyId,
                                                        @RequestBody Map<String, String> requestBody) {
    	String content = requestBody.get("content");

        Reply updatedReply = replyService.updateReply(replyId, content);

        ReplyResponseDto responseDto = new ReplyResponseDto(
            updatedReply.getReply_id(),
            updatedReply.getPost().getPostId(),
            updatedReply.getUser().getUserId().longValue(),
            updatedReply.getUser().getUsername(),
            updatedReply.getContent(),
            updatedReply.getWritingtime()
        );
        return ResponseEntity.ok(responseDto);
    }


    // 댓글 삭제
    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId) {
        replyService.deleteReply(replyId);
        return ResponseEntity.noContent().build();
    }

}
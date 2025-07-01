package com.booklion.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 댓글, 댓글 목록 조회 응답용 Dto
public class ReplyResponseDto {
	
    private Long replyId;
    
    private Long postId;
    
    private Long userId;
    
    private String content;
    
    private LocalDateTime writingtime;
    
}
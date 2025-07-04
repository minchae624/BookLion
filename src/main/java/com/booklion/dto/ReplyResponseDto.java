package com.booklion.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
// 댓글, 댓글 목록 조회 응답용 Dto
public class ReplyResponseDto {
	
    private Long replyId;
    
    private Long postId;
    
    private Long userId;
    
    private String username;
    
    private String content;
    
    private LocalDateTime writingtime;
    
    public ReplyResponseDto(Long replyId, Long postId, Long userId, String content, LocalDateTime writingtime) {
        this.replyId = replyId;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.writingtime = writingtime;
    }
}

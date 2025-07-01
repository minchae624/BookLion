package com.booklion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
// 댓글 작성/수정 요청용 Dto
public class ReplyRequestDto {
	
    private Integer userId;
    
    private String content;
    
}
package com.booklion.dto.mypage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageCommentResponseDto {

    private String content;     // 댓글 내용
    private LocalDateTime date; // 작성일 (정렬용 내부 값)
    private String type;        // "독후감" or "QnA"
    private Long parentId;      // postId or questId

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    public static MyPageCommentResponseDto of(String content, LocalDateTime date, String type, Long parentId) {
        return MyPageCommentResponseDto.builder()
                .content(content)
                .date(date)
                .type(type)
                .parentId(parentId)
                .build();
    }
}
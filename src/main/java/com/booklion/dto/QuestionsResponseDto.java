package com.booklion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class QuestionsResponseDto {

    private Integer id;

    private Integer category;

    private String title;

    private String writer;

    private LocalDateTime writingTime;

    private Boolean status;

    private Integer viewCount;

    private Integer likeCount;

    public String getCategoryName() {
        return switch (category) {
            case 1 -> "책 추천";
            case 2 -> "이벤트";
            case 3 -> "기타";
            default -> "알 수 없음";
        };
    }
}

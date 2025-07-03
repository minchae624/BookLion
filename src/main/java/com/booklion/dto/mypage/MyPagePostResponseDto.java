package com.booklion.dto.mypage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPagePostResponseDto {

    private String type;              // 글 종류 (독후감 / QnA)
    private Integer id;                  // postId 또는 questId
    private String title;
    private LocalDateTime date;       // 작성 시간 (정렬용으로 LocalDateTime 유지)

    // yyyy-MM-dd 형식으로 날짜를 반환 (프론트 표시용)
    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // 정적 팩토리 메서드: DTO 생성 시 내부적으로 LocalDateTime을 유지하고 반환
    public static MyPagePostResponseDto of(String type, Integer id, String title, LocalDateTime writingTime) {
        return MyPagePostResponseDto.builder()
                .type(type)
                .id(id)
                .title(title)
                .date(writingTime)
                .build();
    }
}

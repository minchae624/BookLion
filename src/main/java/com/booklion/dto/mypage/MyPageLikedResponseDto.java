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
public class MyPageLikedResponseDto {

    private String type;        // "독후감" 또는 "QnA"
    private Long id;            // post_id 또는 question_id
    private String title;       // 게시글 제목
    private LocalDateTime date; // 작성일 (정렬용)

    /**
     * 날짜를 yyyy-MM-dd 형식의 문자열로 반환
     * @return 포맷된 날짜 문자열
     */
    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * DTO 생성 헬퍼 메서드
     * @param type 게시글 유형 ("독후감", "QnA")
     * @param id 게시글 ID
     * @param title 게시글 제목
     * @param writingTime 게시글 작성 시간
     * @return 생성된 DTO 객체
     */
    public static MyPageLikedResponseDto of(String type, Long id, String title, LocalDateTime writingTime) {
        return MyPageLikedResponseDto.builder()
                .type(type)
                .id(id)
                .title(title)
                .date(writingTime)
                .build();
    }
}

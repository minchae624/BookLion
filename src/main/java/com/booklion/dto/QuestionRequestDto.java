package com.booklion.dto;

import java.time.LocalDateTime;

import com.booklion.model.entity.Category;
import com.booklion.model.entity.QuestionStatus;
import com.booklion.model.entity.Questions;

import lombok.Getter;

@Getter
public class QuestionRequestDto {
    private String title;
    private String content;
    private Category category;

    public Questions toEntity() {
        return Questions.builder()
                .title(title)
                .content(content)
                .category(category)
                .status(QuestionStatus.UNSOLVED)
                .viewCount(0)
                .writingtime(LocalDateTime.now())
                .build();
    }
}


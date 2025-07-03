package com.booklion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PostResponseDto {

    private Long postId;

    private String genre;

    private String title;

    private String writer;

    private LocalDateTime writingtime;

    private Integer viewCount;

    private Integer replyCount;

    private Integer likeCount;

}

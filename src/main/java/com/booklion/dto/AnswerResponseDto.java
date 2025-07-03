package com.booklion.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerResponseDto {

	private Long answerId;
	private Integer questionId;
	private Integer userId;
	private String username;
	private String content;
	private LocalDateTime writingtime;
	private String questionWriter; 
	private String answerStatus;    


	public AnswerResponseDto(Long answerId, Integer questionId, Integer userId, String username, String content,
			LocalDateTime writingtime,String answerStatus) {
		this.answerId = answerId;
		this.questionId = questionId;
		this.userId = userId;
		this.username = username;
		this.content = content;
		this.writingtime = writingtime;
		this.answerStatus=answerStatus;
	}
}

package com.booklion.model.entity;

import java.time.LocalDateTime;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Answers")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Answers {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer answerId;
	
	private String content;
	
	 private LocalDateTime writingtime = LocalDateTime.now();
	
	@Enumerated(EnumType.STRING)
	private AnswerStatus isAccepted;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private Users user;
	
	@ManyToOne
	@JoinColumn(name="quest_id")
	private Questions question;

	public void setUser(Users user) {
		this.user = user;
	}
	
	public void setQuestion(Questions question) {
		this.question = question;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setIsAccepted(AnswerStatus isAccepted) {
		this.isAccepted = isAccepted;
	}

	
}
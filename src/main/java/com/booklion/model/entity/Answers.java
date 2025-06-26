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
import lombok.NoArgsConstructor;

@Entity
@Table(name="Answers")
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public AnswerStatus getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(AnswerStatus isAccepted) {
		this.isAccepted = isAccepted;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Questions getQuestion() {
		return question;
	}

	public void setQuestion(Questions question) {
		this.question = question;
	}

	public Integer getAnswerId() {
		return answerId;
	}

	public LocalDateTime getWritingtime() {
		return writingtime;
	}

	
}
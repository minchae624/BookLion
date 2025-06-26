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
import lombok.Setter;

@Entity
@Table(name="Answers")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Answers {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer answerId;
	
	private String content;
	
	private LocalDateTime writingtime;
	
	@Enumerated(EnumType.STRING)
	private AnswerStatus isAccepted;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private Users user;
	
	@ManyToOne
	@JoinColumn(name="quest_id")
	private Questions question;
	

}

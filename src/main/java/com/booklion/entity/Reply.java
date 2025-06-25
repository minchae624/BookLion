package com.booklion.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Reply")
@Getter
@Setter
public class Reply {

	@Id
	@GeneratedValue
	@Column(name = "reply_id")
	private long replyId;
	
	@ManyToOne
	@JoinColumn(name="post_id", referencedColumnName="post_id")
	@Column(name = "post_id")
	private Post postId;
	
	@Column(name = "user_Id")
	private Users userId;
	
	private String password;
	
	@CreationTimestamp
	private LocalDateTime created_at;
	
	private String email;
	
	private String comment;
}

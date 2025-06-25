package com.booklion.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class Users {

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private long userId;
	
	private String username;
	
	private String password;
	
	@CreationTimestamp
	private LocalDateTime created_at;
	
	private String email;
	
	private String comment;
	
	
	public Users() {

	}

}

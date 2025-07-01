package com.booklion.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;



@Entity
@Table(name="Questions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Questions {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer questId;
	
	private String title;
	private String content;
	private LocalDateTime writingtime = LocalDateTime.now();
	
	@Enumerated(EnumType.STRING)
	private QuestionStatus status;
	
	private Integer viewCount;
	
	public void recordView() {
		if (viewCount==null) viewCount=0;
		this.viewCount++;
	}
	
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private Users user;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;

	
	@OneToMany(mappedBy="question",cascade=CascadeType.ALL)
	private List<Answers> answer=new ArrayList<>();

	@Column(nullable = false)
	private Integer likeCount = 0; 
	@PrePersist
	public void prePersist() {
	    if (this.likeCount == null) this.likeCount = 0;
	}


	public Integer getQuestId() {
		return questId;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setStatus(QuestionStatus status) {
		this.status = status;
	}


	public void setCategory(Category category) {
		this.category = category;
	}

	public void setWritingtime(LocalDateTime writingtime) {
		this.writingtime = writingtime;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}


	
}

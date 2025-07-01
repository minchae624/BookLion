package com.booklion.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="post")
@Getter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private String title;

    private String content;

    private LocalDateTime writingtime = LocalDateTime.now();

    private String booktitle;

    private String author;

    private String genre;

    private Double rating;

    private Integer viewCount;

    private Integer replyCount;

    // 조회수 증가 로직
    public void recordView() {
        if (viewCount==null) viewCount=0;
        this.viewCount++;
    }

    // 댓글 수 증가 로직
    public void recordReply() {
        if (replyCount==null) replyCount=0;
        this.replyCount++;
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

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<Like>();
}
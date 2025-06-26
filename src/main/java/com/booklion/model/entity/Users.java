package com.booklion.model.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    private LocalDateTime createdAt;

    private String comment;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}

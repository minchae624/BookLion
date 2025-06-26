CREATE DATABASE BookLion;
USE BookLion;
-- 1. Users 테이블
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    comment VARCHAR(255)
);

-- 2. Category 테이블
CREATE TABLE category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(100) UNIQUE NOT NULL
);

-- 3. Questions 테이블
CREATE TABLE questions (
    quest_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content VARCHAR(255) NOT NULL,
    status ENUM('unsolved', 'solved') DEFAULT 'unsolved',
    view_count INT DEFAULT 0,
    writingtime DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- 4. Answers 테이블
CREATE TABLE answers (
    answer_id INT AUTO_INCREMENT PRIMARY KEY,
    quest_id INT NOT NULL,
    user_id INT NOT NULL,
    content VARCHAR(255) NOT NULL,
    writingtime DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_accepted ENUM('Y', 'N') DEFAULT 'N',
    FOREIGN KEY (quest_id) REFERENCES questions(quest_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 5. Post 테이블 (서평 게시글)
CREATE TABLE post (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    writingtime DATETIME DEFAULT CURRENT_TIMESTAMP,
    booktitle VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    rating DECIMAL(2,1) NOT NULL,
    view_count INT DEFAULT 0,
    reply_count INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 6. Reply 테이블 (댓글)
CREATE TABLE reply (
    reply_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    content VARCHAR(255) NOT NULL,
    writingtime DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES post(post_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 7. Like 테이블 (좋아요)
CREATE TABLE `like` (
    like_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES post(post_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    UNIQUE KEY uq_like_post_user (post_id, user_id)
);
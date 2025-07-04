package com.booklion.service;

import com.booklion.dto.PostResponseDto;
import com.booklion.model.entity.Like;
import com.booklion.model.entity.Post;
import com.booklion.model.entity.Users;
import com.booklion.repository.LikeRepository;
import com.booklion.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final ReplyService replyService;
    /* 게시판 작성 */
    public Post create(Post post) {
        return postRepository.save(post);
    }

    /* 게시판 조회 */
    public Page<PostResponseDto> findAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);

        return posts.map(post -> new PostResponseDto(
                post.getPostId(),
                post.getGenre(),
                post.getTitle(),
                post.getUser().getUsername(), // writer
                post.getWritingtime(),
                post.getViewCount(),
                post.getReplyCount(),
                post.getLikes().size()
        ));
    }
    /* 게시판 제목 검색 */
    public Page<PostResponseDto> searchByTitle(String title, Pageable pageable) {
        Page<Post> posts = postRepository.findByTitleContaining(title, pageable);
        return posts.map(post -> new PostResponseDto(
                post.getPostId(),
                post.getGenre(),
                post.getTitle(),
                post.getUser().getUsername(), // writer
                post.getWritingtime(),
                post.getViewCount(),
                post.getReplyCount(),
                post.getLikes().size()
        ));
    }
    /* 게시판 내용 검색 */
    public Page<PostResponseDto> searchByContent(String content, Pageable pageable) {
        Page<Post> posts = postRepository.findByContentContaining(content, pageable);
        return posts.map(post -> new PostResponseDto(
                post.getPostId(),
                post.getGenre(),
                post.getTitle(),
                post.getUser().getUsername(), // writer
                post.getWritingtime(),
                post.getViewCount(),
                post.getReplyCount(),
                post.getLikes().size()
        ));
    }
    /* 책 제목 검색 */
    public Page<PostResponseDto> searchByBooktitle(String title, Pageable pageable) {
        Page<Post> posts = postRepository.findByBooktitleContaining(title, pageable);
        return posts.map(post -> new PostResponseDto(
                post.getPostId(),
                post.getGenre(),
                post.getTitle(),
                post.getUser().getUsername(), // writer
                post.getWritingtime(),
                post.getViewCount(),
                post.getReplyCount(),
                post.getLikes().size()
        ));
    }
    /* 책 저자 검색 */
    public Page<PostResponseDto> searchByAuthor(String author, Pageable pageable) {
        Page<Post> posts = postRepository.findByAuthorContaining(author, pageable);
        return posts.map(post -> new PostResponseDto(
                post.getPostId(),
                post.getGenre(),
                post.getTitle(),
                post.getUser().getUsername(), // writer
                post.getWritingtime(),
                post.getViewCount(),
                post.getReplyCount(),
                post.getLikes().size()
        ));
    }

    /* 상세 게시판 조회 */
    public Post findById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()) {
            return post.get();
        }
        else {
            throw new EntityNotFoundException("일치하는 post가 없습니다");
        }
    }

    /* 게시판 수정 */
    public Post update(Long id, Post new_post) {
        Post ex_post = findById(id);
        ex_post.setTitle(new_post.getTitle());
        ex_post.setContent(new_post.getContent());
        ex_post.setBooktitle(new_post.getBooktitle());
        ex_post.setAuthor(new_post.getAuthor());
        ex_post.setRating(new_post.getRating());
        return postRepository.save(ex_post);
    }

    /* 게시판 삭제 */
    public void delete(Post post) {
        replyService.deleteByPost(post);
        postRepository.deleteById(post.getPostId());
    }

    /* 특정 회원이 작성한 게시판 일괄 삭제 (회원 삭제 시 호출 필요) */
    public void deleteAllUsersPost(Users users) {
        List<Post> posts = postRepository.findByUser(users);
        for (Post post : posts) {

            delete(post);
        }
    }
    /* 좋아요 */
    public boolean likePost(Long id, Users user) {
        Post post = findById(id);

        if(likeRepository.existsByUserAndPost(user,post)) {
            return false;
        }
        Like like = Like.forPost(user, post);
        likeRepository.save(like);
        post.increaseLike(like);
        postRepository.save(post);
        return true;
    }
}

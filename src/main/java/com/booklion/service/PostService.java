package com.booklion.service;

import com.booklion.model.entity.Post;
import com.booklion.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    /* 게시판 작성 */
    public Post create(Post post) {
        return postRepository.save(post);
    }

    /* 게시판 조회 */
    public List<Post> findAll() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "postId"));
    }

    /* 상세 게시판 조회 */
    public Post findById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()) {
            return post.get();
        }
        else {
            System.out.println(getClass()+": "+ getClass().getName() + "일치하는 post_id가 없음!!");
            return null;
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
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}

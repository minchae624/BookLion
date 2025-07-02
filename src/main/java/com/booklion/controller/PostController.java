package com.booklion.controller;


import com.booklion.model.entity.Post;
import com.booklion.model.entity.Users;
import com.booklion.repository.UserRepository;
import com.booklion.service.PostService;
import com.booklion.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final UserRepository userRepository;

    /* 글 작성 페이지 */
    @GetMapping("/write")
    public String writePost(Model model) {
        model.addAttribute("post", new Post());
        return "review/review_write";
    }
    /* 글 수정 페이지 */
    @GetMapping("/update/{id}")
    public String updatePost(@PathVariable Long id ,Model model) {
        Post ex_post = postService.findById(id);
        model.addAttribute("post", ex_post);
        return "review/review_update";
    }
    /* 글 수정 로직 */
    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable Long id, Post post) {
        postService.update(id, post);
        return "redirect:/api/posts";
    }
    /* 글 삭제 로직 */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        Post post = postService.findById(id);
        postService.delete(post);
        return ResponseEntity.ok().build();
    }
    /* 글 작성 로직 */
    @PostMapping
    public String write(@ModelAttribute Post post, @RequestParam String username) {

        Users user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없음")
        );
        post.setUser(user);
        postService.create(post);
        return "redirect:/api/posts";
    }

    /* 필터 검색 */
    @GetMapping
    public String search(Model model,
                         @RequestParam(required = false) String keyword,
                         @RequestParam(required = false) String input,
                         @RequestParam(defaultValue = "0") int page,       // 기본 0페이지
                         @RequestParam(defaultValue = "10") int size){     // 기본 10개씩)

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "postId")); // id 내림차순 정렬
        Page<Post> posts;
        if(keyword != null && input != null && !input.isBlank()) {
            switch (keyword) {
                case "t":   //title 검색
                    posts = postService.searchByTitle(input,pageable);
                    break;
                case "c":   // content 검색
                    posts = postService.searchByContent(input, pageable);
                    break;
                case "b":   // booktitle 검색
                    posts = postService.searchByBooktitle(input, pageable);
                    break;
                case "a":   // author 검색
                    posts = postService.searchByAuthor(input, pageable);
                    break;
                default:
                    posts = postService.findAll(pageable);
                    break;
            }

        } else {
            //검색 필터 없음
            posts = postService.findAll(pageable);
        }
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("input", input == null ? "" : input);
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("page", posts); // 페이징 정보 전체 전달
        return "review/reviews";
    }

    /* 상세 게시글 조회 */
    @GetMapping("/{id}")
    public String detail(Model model, @PathVariable Long id) {
        Post post = postService.findById(id);
        post.recordView();
        postService.update(id, post);
        model.addAttribute("posts", post);
        return "review/review_detail";
    }

    /* 좋아요 */
    @PostMapping("/{id}/like")
    public ResponseEntity<String> like(@PathVariable Long id, @RequestParam String username){
        Users loginuser = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없음")
        );

        boolean liked = postService.likePost(id, loginuser);
        if(liked){
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().body("이미 좋아요 눌렀습니다.");
        }
    }

    /* 회원 삭제 시 */
    /* 회원 삭제 테스트용
    @GetMapping("/deleteall/{userId}")
    public String deleteAll(@PathVariable int userId){
        Optional<Users> res = userRepository.findById(userId);
        Users user = res.get();
        postService.deleteAllUsersPost(user);
        return "redirect:/api/posts";
    }
    */

}

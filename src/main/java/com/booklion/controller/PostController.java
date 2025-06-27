package com.booklion.controller;


import com.booklion.model.entity.Post;
import com.booklion.model.entity.Users;
import com.booklion.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    /* 글 작성 페이지 */
    @GetMapping("/write")
    public String writePost(Model model) {
        model.addAttribute("post", new Post());
        return "review/review_write";
    }
    /* 글 작성 로직 */
    @PostMapping
    public String write(@ModelAttribute Post post, @ModelAttribute Users user) {

        /* 유저 객체를 받아야 함
        Users user = new Users();
        user.setUserId(1);
        user.setUsername("test");
        user.setPassword("test");
        user.setEmail("test@test");
        */
        post.setUser(user);
        postService.create(post);
        return "redirect:/api/posts";
    }
    /* 필터 검색 */
    @GetMapping
    public String search(Model model, @RequestParam(required = false) String keyword, String input) {
        if(keyword != null) {
            switch (keyword) {
                case "t":   //title 검색
                    System.out.println("제목 검색함!!!!!!!!!!!!!!!!!!");
                    break;
                case "c":   // content 검색
                    System.out.println("내용 검색함!!!!!!!!!!!!!!!!!!");
                    break;
                case "b":   // booktitle 검색
                    System.out.println("책 제목 검색함!!!!!!!!!!!!!!!!!!");
                    break;
                case "a":   // author 검색
                    System.out.println("저자 검색함!!!!!!!!!!!!!!!!!!");
                    break;
                default:
                    break;
            }
            model.addAttribute("posts",postService.findAll());
            return "review/reviews";
        }
        else{
            //검색 필터 없음
            model.addAttribute("posts",postService.findAll());
            return "review/reviews";
        }

    }
    /* 상세 게시글 조회 */
    @GetMapping("/{id}")
    public String detail(Model model, @PathVariable Long id) {
        return "review/review_detail";
    }
}

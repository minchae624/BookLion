package com.booklion.controller;

import com.booklion.dto.mypage.MyPageCommentResponseDto;
import com.booklion.dto.mypage.MyPagePostResponseDto;
import com.booklion.service.MyPageService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/posts")
    public List<MyPagePostResponseDto> getMyPosts(@RequestHeader("Authorization") String authHeader) {
        return myPageService.getWrittenPosts(authHeader);
    }

    @GetMapping("/comments")
    public List<MyPageCommentResponseDto> getMyComments(@RequestHeader("Authorization") String authHeader) {
        return myPageService.getWrittenComments(authHeader);
    }
    
    @GetMapping("/likes")
    public List<MyPagePostResponseDto> getLikedPosts(@RequestHeader("Authorization") String authHeader) {
        return myPageService.getLikedPosts(authHeader);
    }
}
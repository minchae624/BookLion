package com.booklion.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.booklion.dto.ReplyRequestDto;
import com.booklion.model.entity.Post;
import com.booklion.model.entity.Reply;
import com.booklion.model.entity.Users;
import com.booklion.repository.PostRepository;
import com.booklion.repository.ReplyRepository;
import com.booklion.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Reply createReply(Long postId, ReplyRequestDto replyDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Integer userId = Math.toIntExact(replyDto.getUserId());
        
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Reply reply = new Reply();
        reply.setPost(post);
        reply.setUser(user);
        reply.setContent(replyDto.getContent());
        reply.setWritingtime(LocalDateTime.now());

        return replyRepository.save(reply);
    }

    public List<Reply> getRepliesByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return replyRepository.findByPost(post);
    }

    public int getReplyCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return replyRepository.countByPost(post);
    }

    @Transactional
    public Reply updateReply(Long replyId, String content) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        reply.setContent(content);
        reply.setWritingtime(LocalDateTime.now());
        return reply;
    }

    public void deleteReply(Long replyId) {
    	if (!replyRepository.existsById(replyId)) {
            throw new IllegalArgumentException("삭제할 댓글이 없습니다.");
        }
        replyRepository.deleteById(replyId);
    }

}


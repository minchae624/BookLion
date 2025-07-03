package com.booklion.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.booklion.dto.mypage.MyPageCommentResponseDto;
import com.booklion.dto.mypage.MyPagePostResponseDto;
import com.booklion.model.entity.Answers;
import com.booklion.model.entity.Post;
import com.booklion.model.entity.Questions;
import com.booklion.model.entity.Reply;
import com.booklion.model.entity.Users;
import com.booklion.repository.LikeRepository;
import com.booklion.repository.PostRepository;
import com.booklion.repository.QuestionRepository;
import com.booklion.repository.ReplyRepository;
import com.booklion.repository.UserRepository;
import com.booklion.repository.AnswerRepositoty;
import com.booklion.util.JwtUtil;

import lombok.RequiredArgsConstructor;

/**
 * 서비스 클래스: 마이페이지 관련 기능을 처리
 * - 작성한 글 목록 조회
 * - 작성한 댓글 목록 조회
 * - 좋아요한 글 목록 조회
 */
@Service
@RequiredArgsConstructor
public class MyPageService {

    private final PostRepository postRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final LikeRepository likeRepository;
    private final AnswerRepositoty answerRepositoty;
    private final JwtUtil jwtUtil;

    /**
     * 작성한 글 목록 조회 (독후감 + QnA)
     * @param authHeader Authorization 헤더값 ("Bearer 토큰")
     * @return 글 응답 DTO 목록 (작성일 내림차순 정렬)
     */
    public List<MyPagePostResponseDto> getWrittenPosts(String authHeader) {
        Users user = getUserFromAuthHeader(authHeader);

        List<MyPagePostResponseDto> writtenPosts = new ArrayList<>();
        writtenPosts.addAll(mapPostsToDto(postRepository.findByUser(user), "독후감"));
        writtenPosts.addAll(mapQuestionsToDto(questionRepository.findByUser_UserId(user.getUserId()), "QnA"));

        // 작성일 기준 내림차순 정렬
        writtenPosts.sort(Comparator.comparing(MyPagePostResponseDto::getDate).reversed());
        return writtenPosts;
    }

    /**
     * 작성한 댓글 목록 조회
     * @param authHeader Authorization 헤더값 ("Bearer 토큰")
     * @return 댓글 응답 DTO 목록 (작성일 내림차순 정렬)
     */
    public List<MyPageCommentResponseDto> getWrittenComments(String authHeader) {
        Users user = getUserFromAuthHeader(authHeader);

        List<MyPageCommentResponseDto> comments = new ArrayList<>();

        // 🟡 독후감(Post)에 단 댓글 (Reply)
        List<Reply> replies = replyRepository.findByUser(user);
        List<MyPageCommentResponseDto> replyDtos = replies.stream()
                .map(reply -> MyPageCommentResponseDto.builder()
                        .type("독후감")
                        .parentId(reply.getPost().getPostId())
                        .content(reply.getContent())
                        .date(reply.getWritingtime())  // LocalDateTime
                        .build())
                .collect(Collectors.toList());

        // 🟡 QnA에 단 답변 (Answers)
        List<Answers> answers = answerRepositoty.findByUser(user);
        List<MyPageCommentResponseDto> answerDtos = answers.stream()
                .map(answer -> MyPageCommentResponseDto.builder()
                        .type("QnA")
                        .parentId(answer.getQuestion().getQuestId().longValue())
                        .content(answer.getContent())
                        .date(answer.getWritingtime())  // LocalDateTime
                        .build())
                .collect(Collectors.toList());

        // 통합 후 작성일 기준 내림차순 정렬
        comments.addAll(replyDtos);
        comments.addAll(answerDtos);
        comments.sort(Comparator.comparing(MyPageCommentResponseDto::getDate).reversed());

        return comments;
    }

    /**
     * 좋아요한 글 목록 조회 (독후감 + QnA)
     * @param authHeader Authorization 헤더값 ("Bearer 토큰")
     * @return 좋아요한 글 응답 DTO 목록 (작성일 내림차순 정렬)
     */
    public List<MyPagePostResponseDto> getLikedPosts(String authHeader) {
        Users user = getUserFromAuthHeader(authHeader);

        List<MyPagePostResponseDto> likedPosts = new ArrayList<>();
        likedPosts.addAll(mapPostsToDto(likeRepository.findLikedPostsByUser(user), "독후감"));
        likedPosts.addAll(mapQuestionsToDto(likeRepository.findLikedQuestionsByUser(user), "QnA"));

        // 작성일 기준 내림차순 정렬
        likedPosts.sort(Comparator.comparing(MyPagePostResponseDto::getDate).reversed());
        return likedPosts;
    }


    /**
     * Authorization 헤더로부터 사용자 정보 추출 및 조회
     * @param authHeader "Bearer 토큰" 형태의 인증 헤더
     * @return 조회된 Users 객체
     */
    private Users getUserFromAuthHeader(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    }

    /**
     * Post 리스트를 MyPagePostResponseDto 리스트로 변환
     * @param posts 게시글 리스트
     * @param type "독후감" 구분 문자열
     * @return 변환된 DTO 리스트
     */
    private List<MyPagePostResponseDto> mapPostsToDto(List<Post> posts, String type) {
        return posts.stream()
                .map(post -> MyPagePostResponseDto.of(
                        type,
                        post.getPostId(),
                        post.getTitle(),
                        post.getWritingtime()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Questions 리스트를 MyPagePostResponseDto 리스트로 변환
     * @param questions QnA 리스트
     * @param type "QnA" 구분 문자열
     * @return 변환된 DTO 리스트
     */
    private List<MyPagePostResponseDto> mapQuestionsToDto(List<Questions> questions, String type) {
        return questions.stream()
                .map(q -> MyPagePostResponseDto.of(
                        type,
                        q.getQuestId(),
                        q.getTitle(),
                        q.getWritingtime()
                ))
                .collect(Collectors.toList());
    }
}
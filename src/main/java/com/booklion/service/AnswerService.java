package com.booklion.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.booklion.dto.AnswerRequestDto;
import com.booklion.model.entity.AnswerStatus;
import com.booklion.model.entity.Answers;
import com.booklion.model.entity.QuestionStatus;
import com.booklion.model.entity.Questions;
import com.booklion.model.entity.Users;
import com.booklion.repository.AnswerRepository;
import com.booklion.repository.QuestionRepository;
import com.booklion.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public Answers createAnswer(Integer questionId, AnswerRequestDto dto) {
        Questions question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));
        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Answers answer = new Answers();
        answer.setQuestion(question);
        answer.setUser(user);
        answer.setContent(dto.getContent());
        answer.setWritingtime(LocalDateTime.now());
        answer.setIsAccepted(AnswerStatus.N);

        return answerRepository.save(answer);
    }

    public List<Answers> getAnswersByQuestion(Integer questionId) {
        Questions question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));
        return answerRepository.findByQuestion(question);
    }

    public int getAnswerCount(Integer questionId) {
        Questions question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));
        return answerRepository.countByQuestion(question);
    }

    @Transactional
    public Answers updateAnswer(Long answerId, String content) {
        Answers answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변이 존재하지 않습니다."));
        answer.setContent(content);
        answer.setWritingtime(LocalDateTime.now());
        return answer;
    }

    public void deleteAnswer(Long answerId) {
        if (!answerRepository.existsById(answerId)) {
            throw new IllegalArgumentException("삭제할 답변이 없습니다.");
        }
        answerRepository.deleteById(answerId);
    }
    
    @Transactional
    public void acceptAnswer(Long answerId) {
        Answers answer = answerRepository.findById(answerId)
            .orElseThrow(() -> new IllegalArgumentException("답변이 존재하지 않습니다."));

        List<Answers> answers = answerRepository.findByQuestion(answer.getQuestion());
        for (Answers a : answers) {
            a.setIsAccepted(AnswerStatus.N);  
        }

        answer.setIsAccepted(AnswerStatus.Y);

        Questions question = answer.getQuestion();
        question.setStatus(QuestionStatus.solved); 

        answerRepository.saveAll(answers);  
        questionRepository.save(question);  
    }

    public Answers findById(Long answerId) {
        return answerRepository.findById(answerId)
            .orElseThrow(() -> new IllegalArgumentException("답변이 존재하지 않습니다."));
    }


}

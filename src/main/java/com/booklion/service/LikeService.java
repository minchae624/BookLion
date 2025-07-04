package com.booklion.service;

import com.booklion.model.entity.Users;
import com.booklion.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    //회원 탈퇴 시 좋아요 삭제
    @Transactional
    public void deleteAllbyUser(Users user) {
        likeRepository.deleteLikesByUser(user);
    }
}

package com.booklion.service;

import com.booklion.model.entity.Category;
import com.booklion.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class categoryService {

    private final CategoryRepository categoryRepository;

    // 모든 카테고리 목록 가져오기
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ID로 카테고리 찾기
    public Category getCategoryById(Integer categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category.orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
    }
}


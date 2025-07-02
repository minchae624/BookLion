package com.booklion.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.booklion.model.entity.Questions;

@Repository
public interface QuestionRepository extends JpaRepository<Questions, Integer> {

	@Query("SELECT q FROM Questions q WHERE q.categoryId = :categoryId AND (q.title LIKE %:keyword% OR q.content LIKE %:keyword%)")
	List<Questions> searchByCategoryAndKeyword(@Param("categoryId") Integer categoryId,
			@Param("keyword") String keyword);

	@Query("SELECT q FROM Questions q")
	Page<Questions> findAllWithCategoryAndUser(Pageable pageable);

	@Query("SELECT q FROM Questions q WHERE q.title LIKE %:input% OR q.user.username LIKE %:input%")
	Page<Questions> searchWithPaging(@Param("input") String input, Pageable pageable);

	List<Questions> findByUser_UserId(Integer userId);

	List<Questions> findAllByOrderByQuestIdDesc();

}

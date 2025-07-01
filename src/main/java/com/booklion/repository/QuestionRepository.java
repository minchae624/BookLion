package com.booklion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.booklion.model.entity.QuestionStatus;
import com.booklion.model.entity.Questions;

@Repository
public interface QuestionRepository extends JpaRepository<Questions, Integer> {

	@Query("SELECT q FROM Questions q " + "WHERE q.category.category_id = :categoryId "
			+ "AND (q.title LIKE %:keyword% OR q.content LIKE %:keyword%)")
	List<Questions> searchByCategoryAndKeyword(@Param("categoryId") Integer categoryId,
			@Param("keyword") String keyword);
	
	@Query("SELECT q FROM Questions q JOIN FETCH q.category JOIN FETCH q.user")
	List<Questions> findAllWithCategoryAndUser();

	List<Questions> findByUser_UserId(Integer userId);
	List<Questions> findAllByOrderByQuestIdDesc();


}

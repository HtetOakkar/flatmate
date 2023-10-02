package com.lotus.flatmate.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.post.entity.Post;
import com.lotus.flatmate.post.entity.SavedPost;

@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, Long>{

	@Query(value = "SELECT Count(s) FROM SavedPost s WHERE s.post.id = :id AND s.user.id = :userId")
	Long existsByPostIdAndUserId(Long id, Long userId);

	@Query(value = "SELECT s.post FROM SavedPost s WHERE s.user.id = :userId ORDER BY s.id DESC")
	List<Post> findbyUserId(Long userId);

	@Modifying
	@Query(value = "DELETE FROM SavedPost s WHERE s.post.id = :id AND s.user.id = :userId")
	void deleteByPostIdAndUserId(Long id, Long userId);

	@Query(value = "SELECT s FROM SavedPost s WHERE s.post.id = :id AND s.user.id = :userId")
	SavedPost findByPostAndUser(Long id, Long userId);

}

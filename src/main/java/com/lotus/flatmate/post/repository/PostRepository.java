package com.lotus.flatmate.post.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.post.dto.AllPostDto;
import com.lotus.flatmate.post.entity.Post;
import com.lotus.flatmate.user.entity.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	@Query(value = "SELECT p FROM Post p WHERE p.user.id != :userId AND p.tenants > 0 AND p.updatedAt < :cursor ORDER BY p.updatedAt DESC")
	Page<AllPostDto> findAllPageDto(Instant cursor, Pageable pageble, Long userId);

	@Query(value = "SELECT p.user FROM Post p WHERE p.id = :id")
	User getUserByPostId(Long id);

	@Query(value = "SELECT MAX(p.id) FROM Post p")
	Optional<Long> getLargestId();

}

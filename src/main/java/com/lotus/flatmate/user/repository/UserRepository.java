package com.lotus.flatmate.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	@Query(value = "SELECT u FROM User u WHERE u.email = :email")
	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	@Query(value = "SELECT u FROM User u WHERE u.username LIKE %:key% AND u.id != :currentUserId ORDER BY u.username ASC")
	List<User> findByLike(String key, Long currentUserId);

}

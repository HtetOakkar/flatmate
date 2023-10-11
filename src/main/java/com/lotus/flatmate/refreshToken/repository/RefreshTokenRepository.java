package com.lotus.flatmate.refreshToken.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.refreshToken.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{

	@Query(value = "SELECT r FROM RefreshToken r WHERE r.refreshToken = :token")
	Optional<RefreshToken> findByToken(String token);

	Optional<RefreshToken> findByDeviceId(String deviceId);

	
}

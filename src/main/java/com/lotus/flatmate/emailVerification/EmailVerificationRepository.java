package com.lotus.flatmate.emailVerification;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long>{

	Optional<EmailVerification> findByEmail(String email);

}

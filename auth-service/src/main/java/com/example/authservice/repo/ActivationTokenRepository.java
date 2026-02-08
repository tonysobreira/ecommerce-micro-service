package com.example.authservice.repo;

import com.example.authservice.domain.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, UUID> {

	Optional<ActivationToken> findByTokenHash(String tokenHash);

	void deleteByUserId(UUID userId);
}

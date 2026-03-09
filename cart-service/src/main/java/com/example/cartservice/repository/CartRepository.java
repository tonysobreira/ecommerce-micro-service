package com.example.cartservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.cartservice.model.CartDocument;

public interface CartRepository extends MongoRepository<CartDocument, String> {

	Optional<CartDocument> findByUserId(UUID userId);

	void deleteByUserId(UUID userId);

}

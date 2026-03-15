package com.example.authservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.authservice.model.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {

	Optional<Role> findByName(String name);

	@Query(value = """
			SELECT COUNT(1)
			FROM user_account_roles uar
			JOIN user_accounts ua ON ua.id = uar.user_id
			WHERE uar.role_id = :roleId
			""", nativeQuery = true)
	long countUserAssociations(UUID roleId);

}

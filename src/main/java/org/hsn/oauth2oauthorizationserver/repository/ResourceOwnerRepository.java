package org.hsn.oauth2oauthorizationserver.repository;

import org.hsn.oauth2oauthorizationserver.entity.ResourceOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface ResourceOwnerRepository extends JpaRepository<ResourceOwner,Long> {
   Optional< UserDetails> findByUsername(String username);
}

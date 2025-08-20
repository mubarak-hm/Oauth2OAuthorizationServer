package org.hsn.oauth2oauthorizationserver.repository;

import org.hsn.oauth2oauthorizationserver.entity.ResourceOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceOwnerRepository extends JpaRepository<ResourceOwner,Long> {
}

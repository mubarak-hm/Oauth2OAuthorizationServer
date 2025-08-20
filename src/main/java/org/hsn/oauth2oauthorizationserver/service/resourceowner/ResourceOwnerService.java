package org.hsn.oauth2oauthorizationserver.service.resourceowner;

import lombok.RequiredArgsConstructor;
import org.hsn.oauth2oauthorizationserver.entity.ResourceOwner;
import org.hsn.oauth2oauthorizationserver.repository.ResourceOwnerRepository;
import org.hsn.oauth2oauthorizationserver.requests.ResourceOwnerRegistrationRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class ResourceOwnerService {
    private final ResourceOwnerRepository resourceOwnerRepository;
    private final PasswordEncoder passwordEncoder;

    public void createResourceOwner(ResourceOwnerRegistrationRequest request) {
        ResourceOwner resourceOwner = ResourceOwner.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .password(passwordEncoder.encode(request.password()))
                .username(request.userName())
                .birthDate(request.birthDate())
                .build();
        resourceOwnerRepository.save(resourceOwner);

    }


}

package org.hsn.oauth2oauthorizationserver.service.resourceowner;

import lombok.RequiredArgsConstructor;
import org.hsn.oauth2oauthorizationserver.repository.ResourceOwnerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final ResourceOwnerRepository resourceOwnerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return resourceOwnerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username:" + username));

    }
}

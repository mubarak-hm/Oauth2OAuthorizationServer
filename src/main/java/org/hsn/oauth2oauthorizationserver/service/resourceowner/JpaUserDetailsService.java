package org.hsn.oauth2oauthorizationserver.service.resourceowner;

import lombok.RequiredArgsConstructor;
import org.hsn.oauth2oauthorizationserver.adapter.UserDetailsAdapter;
import org.hsn.oauth2oauthorizationserver.entity.ResourceOwner;
import org.hsn.oauth2oauthorizationserver.repository.ResourceOwnerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final ResourceOwnerRepository resourceOwnerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResourceOwner user = resourceOwnerRepository.findByUsername(username);
        if(user==null){

            throw  new UsernameNotFoundException("user not found");
        }
        return  new UserDetailsAdapter(user);

    }
}



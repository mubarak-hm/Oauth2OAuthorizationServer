package org.hsn.oauth2oauthorizationserver.adapter;
import org.hsn.oauth2oauthorizationserver.entity.ResourceOwner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;


public record UserDetailsAdapter(ResourceOwner resourceOwner) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(resourceOwner.getRole()));
    }

    @Override
    public String getPassword() {
        return resourceOwner.getPassword();
    }

    @Override
    public String getUsername() {
        return resourceOwner.getUsername();
    }
}

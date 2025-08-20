package org.hsn.oauth2oauthorizationserver.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        OAuth2AuthorizationServerConfigurer   auth2AuthorizationServerConfigurer=
                OAuth2AuthorizationServerConfigurer.authorizationServer().oidc(Customizer.withDefaults());

        httpSecurity.
                with(auth2AuthorizationServerConfigurer,Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return
                httpSecurity.formLogin(Customizer.withDefaults())
                        .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                        .build();
    }




}

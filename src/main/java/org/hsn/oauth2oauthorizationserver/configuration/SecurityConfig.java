package org.hsn.oauth2oauthorizationserver.configuration;
import org.hsn.oauth2oauthorizationserver.service.resourceowner.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;


@Configuration
public class SecurityConfig {
    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfigurer auth2AuthorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();
        httpSecurity
                .securityMatcher(auth2AuthorizationServerConfigurer.getEndpointsMatcher())
                .with(auth2AuthorizationServerConfigurer, authorizationServer->
                        authorizationServer.oidc(Customizer.withDefaults())
                        )
                .exceptionHandling(e->
                        e.defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)

                        ));
        return httpSecurity.build();
    }


    @Order(2)
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity, JpaUserDetailsService userDetailsService) throws Exception {
         httpSecurity
                .userDetailsService(userDetailsService)
                .csrf((csrf-> csrf.
                        ignoringRequestMatchers("/registration/**","/login")))
                .authorizeHttpRequests(
                        authorize ->{
                            authorize.requestMatchers("/registration/**").permitAll();
                            authorize.anyRequest().authenticated();
                        })
                .formLogin(Customizer.withDefaults());
                 return httpSecurity.build();

    }
}

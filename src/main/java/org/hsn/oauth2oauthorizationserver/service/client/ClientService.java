package org.hsn.oauth2oauthorizationserver.service.client;

import lombok.RequiredArgsConstructor;
import org.hsn.oauth2oauthorizationserver.requests.ClientRegistrationRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ClientService {
    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerClient(ClientRegistrationRequest request) {
        RegisteredClient.Builder clientBuilder = RegisteredClient
                .withId(String.valueOf(UUID.randomUUID()))
                .clientId(request.clientId())
                .clientSecret(passwordEncoder.encode(request.clientSecret()))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        request.redirectUris().forEach(clientBuilder::redirectUri);
        request.scopes().forEach(clientBuilder::scope);
        request.grantTypes().forEach(grant -> clientBuilder
                .authorizationGrantType(new AuthorizationGrantType(grant)));
        clientBuilder.clientSettings(ClientSettings.builder().requireProofKey(true).build());
        registeredClientRepository.save(clientBuilder.build());

    }


}

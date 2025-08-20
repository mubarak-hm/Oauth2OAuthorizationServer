package org.hsn.oauth2oauthorizationserver.requests;
import java.util.Set;
public record ClientRegistrationRequest(
   String clientId,
   String clientSecret,
   Set<String> redirectUris,
   Set<String> scopes,
   Set<String> grantTypes
) {}


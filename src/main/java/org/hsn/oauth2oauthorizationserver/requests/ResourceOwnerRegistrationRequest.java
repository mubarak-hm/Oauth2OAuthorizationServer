package org.hsn.oauth2oauthorizationserver.requests;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ResourceOwnerRegistrationRequest(
        String firstName,
        String lastName,
        String password ,
        LocalDate birthDate,
        String userName
) {}

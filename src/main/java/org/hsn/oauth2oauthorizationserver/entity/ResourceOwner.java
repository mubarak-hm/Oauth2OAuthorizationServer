package org.hsn.oauth2oauthorizationserver.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ResourceOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String  firstName;
    private String lastName;
    private  String username;
    private String password;
    private LocalDate birthDate;
}

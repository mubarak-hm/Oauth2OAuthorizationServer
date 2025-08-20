package org.hsn.oauth2oauthorizationserver.utils;
import lombok.Getter;
import org.hsn.oauth2oauthorizationserver.exception.RSAException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Getter
@Component
@ConfigurationProperties(prefix = "rsa")
public class RSAUtil {

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public void setPublicKey(String publicKeyPem) {
        try {
            this.publicKey = (RSAPublicKey) parseKeyFromString(publicKeyPem, true);
        } catch (Exception e) {
            throw new RSAException("Failed to parse public key" + e.getMessage());
        }
    }

    public void setPrivateKey(String privateKeyPem) {
        try {
            this.privateKey = (RSAPrivateKey) parseKeyFromString(privateKeyPem, false);
        } catch (Exception e) {
            throw new RSAException("Failed to parse private key" + e.getMessage());
        }
    }

    private java.security.Key parseKeyFromString(String key, boolean isPublic) throws Exception {
        String keyPem = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(keyPem);

        KeyFactory kf = KeyFactory.getInstance("RSA");

        if (isPublic) {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            return kf.generatePublic(spec);
        } else {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            return kf.generatePrivate(spec);
        }
    }
}
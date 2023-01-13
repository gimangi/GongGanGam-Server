package site.gonggangam.gonggangam_server.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtConfig {

    @Value("${jwt.secret}")
    public String secretKey;

    @Value("${jwt.issuer}")
    public String issuer;

    @Bean
    public JWTVerifier tokenValidator() {
        return JWT.require(getSigningKey(secretKey))
                .withIssuer(issuer)
                .build();
    }

    public Algorithm getSigningKey(String secretKey) {
        return Algorithm.HMAC256(secretKey);
    }

}

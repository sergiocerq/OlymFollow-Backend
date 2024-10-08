package com.OlymFollow.Backend.Services;

import com.OlymFollow.Backend.Entitys.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class JWTokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(String nome){
        try {
            var algorithm = getAlgorithm();
            return JWT.create()
                    .withIssuer("Haunted API")
                    .withSubject(nome)
                    .withExpiresAt(dataExpiration())
                    .sign(algorithm);
        }catch (JWTCreationException e){
            throw new RuntimeException("JWT generation failed", e);
        }
    }

    public String verifyToken(String tokenJWT){
        var algorithm = getAlgorithm();
        return JWT.require(algorithm)
                .withIssuer("Haunted API")
                .build()
                .verify(tokenJWT)
                .getSubject();
    }



    private Algorithm getAlgorithm(){
        return Algorithm.HMAC256("secret");
    }

    private static Instant dataExpiration() {
        return LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-03:00"));
    }
}

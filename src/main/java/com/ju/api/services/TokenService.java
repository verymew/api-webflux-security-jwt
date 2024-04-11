package com.ju.api.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ju.api.models.UserModel;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    private String chaveSecreta = "chave-secreta"; //não adequado para prod
    public String gerarToken(UserModel user){
        try {
            Algorithm algoritmo = Algorithm.HMAC256(chaveSecreta); //Tipo de assinatura. Algoritmo recomendado: RS256
            String token = JWT.create()
                    .withSubject(user.getEmail()) //assina uma informação adicional ao token.
                    .withIssuer("auth0")
                    .withExpiresAt(dataExpiracao())//define o tempo de vida
                    .sign(algoritmo);
            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException("ERROR WHILE GENERATING TOKEN", exception);
        }
    }
    public String validarToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(chaveSecreta);
            return JWT.require(algorithm)//Checa a assinatura
                    .withIssuer("auth")
                    .build()
                    .verify(token)
                    .getSubject();
        }
        catch (JWTVerificationException exception) {
            return "";
        }
    }
    private Instant dataExpiracao(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}

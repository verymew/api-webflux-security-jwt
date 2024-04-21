package com.ju.api.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ju.api.models.UserModel;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;

@Service
public class TokenService {
    private final String secret = "nao-prod";
    public String gerarToken(Authentication authentication){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(authentication.getName()) //Cria uma claim que assina o nome do usuario
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error while authenticating");
        }
    }
    public String validarToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
    public Authentication pegarAutorizacao(String token) {
        //Essa classe investiga o token para resgatar claims/roles
        DecodedJWT jwt = JWT.decode(token);
        //Resgata claim que contém roles
        Claim roles = jwt.getClaim("roles");
        //transforma as roles em uma coleção de roles com comma separando elas.
        Collection<? extends GrantedAuthority> authorities = roles == null ? AuthorityUtils.NO_AUTHORITIES : AuthorityUtils.commaSeparatedStringToAuthorityList(roles.toString());
        User principal = new User(jwt.getSubject(), "", authorities); //Esse é um userDetails, para ser instanciado precisa de: username, password e roles.
        //Retorna um objeto que indica que o usuario é autenticado
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}

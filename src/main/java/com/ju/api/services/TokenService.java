package com.ju.api.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ju.api.config.JwtFilter;
import com.ju.api.models.UserModel;
import com.ju.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Service
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    @Autowired
    private UserRepository userRepository;
    private final String secret = "nao-prod";
    public String gerarToken(Authentication authentication){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> authoritiesList = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(authentication.getName()) //Cria uma claim que assina o nome do usuario
                    .withExpiresAt(this.generateExpirationDate())
                    .withClaim("authorities", authoritiesList)
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
        // Essa classe investiga o token para resgatar claims/roles
        DecodedJWT jwt = JWT.decode(token);
        logger.info("Token nivel 2 : {}", token);
        // Obtém as claims do token
        Map<String, Claim> claims = jwt.getClaims();
        logger.info("permissoes: {} ", claims);
        // Verifica se a claim "authorities" está presente e não é nula
        if (claims.containsKey("authorities") && claims.get("authorities") != null) {
            // Extrai as autoridades do claim "authorities" do token
            Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("authorities").asArray(String.class))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            // Cria um UserDetails com base nas informações do token
            UserDetails userDetails = new User(jwt.getSubject(), "", authorities);
            return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        } else {
            // Se a claim "authorities" não estiver presente ou for nula, retorna uma autenticação vazia
            return null;
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}

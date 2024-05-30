package com.ju.api.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ju.api.dtos.TokenDto;
import com.ju.api.dtos.UserDto;
import com.ju.api.dtos.UserLoginDto;
import com.ju.api.models.UserModel;
import com.ju.api.services.TokenService;
import com.ju.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ReactiveAuthenticationManager authenticationManager;
    @GetMapping("/csrf")
    public Mono<CsrfToken> csrfToken(ServerWebExchange exchange) {
        Mono<CsrfToken> csrfToken = exchange.getAttribute(CsrfToken.class.getName());
        return csrfToken != null ? csrfToken : Mono.empty();
    }
    @GetMapping("/pq")
    public Mono<String> oi(){
        return Mono.just("OIIIIIIIIIIIIIII");
    }
    @PostMapping("/registrar")
    public Mono<UserModel> salvarUsuario(@RequestBody UserDto user){
        return userService.salvarUsuario(user);
    }
    @GetMapping("/ver")
    public Mono<String> teste(){
        return Mono.just("Bem-vinda garota!!");
    }
    @PostMapping("/login")
    public Mono<ResponseEntity> login(@RequestBody Mono<UserLoginDto> usuario){
        return usuario
                .flatMap(login -> this.authenticationManager
                        //Método .authenticate está ligado com o ReactiveAuthenticationManager, configurado no securityConfig
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                login.username, login.password))
                        .map(this.tokenService::gerarToken)) //Retorna um token
                .map(jwt -> { //Token retornado
                    //Adiciona headers no token
                    ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
                            .httpOnly(true)
                            .secure(true)
                            .path("/")
                            .maxAge(30000)
                            .build();
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
                    return new ResponseEntity<>(httpHeaders, HttpStatus.OK); //retorna um token(jwt) e um httpheaders(cookie)
                });
    }
}

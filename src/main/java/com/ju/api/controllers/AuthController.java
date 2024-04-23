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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
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
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
                    var tokenBody = Map.of("access_token", jwt);
                    return new ResponseEntity<>(tokenBody, httpHeaders, HttpStatus.OK);
                });
    }
}

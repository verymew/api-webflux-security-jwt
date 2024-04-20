package com.ju.api.controllers;

import com.ju.api.dtos.UserDto;
import com.ju.api.dtos.UserLoginDto;
import com.ju.api.models.UserModel;
import com.ju.api.services.TokenService;
import com.ju.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/registrar")
    public Mono<UserModel> salvarUsuario(@RequestBody UserDto user){
        return userService.salvarUsuario(user);
    }
    @GetMapping("/ver")
    public Mono<String> teste(){
        return Mono.just("cu");
    }
    @PostMapping("/login")
    public Mono<ResponseEntity> login(@RequestBody UserLoginDto user){
        //verifica se usuario existe
        Mono<UserModel> usuario = this.userService.procurarUsuario(user);
        if(usuario != null){
            String token = tokenService.gerarToken(user.email);
            return new ResponseEntity.ok()
        }
        return Mono.just(HttpStatus.BAD_REQUEST);
    }
}

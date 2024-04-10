package com.ju.api.controllers;

import com.ju.api.dtos.UserDto;
import com.ju.api.models.UserModel;
import com.ju.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private UserService userService;
    @PostMapping("/salvar")
    public Mono<UserModel> salvarUsuario(@RequestBody UserDto user){
        return userService.salvarUsuario(user);
    }
    @GetMapping("/ver")
    public Mono<String> teste(){
        return Mono.just("cu");
    }
}

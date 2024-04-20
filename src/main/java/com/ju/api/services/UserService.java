package com.ju.api.services;

import com.ju.api.dtos.UserDto;
import com.ju.api.dtos.UserLoginDto;
import com.ju.api.models.UserModel;
import com.ju.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Service
public class UserService {
    private PasswordEncoder passwordEncoder;
    @Autowired
    private  UserRepository userRepository;

    public Mono<UserModel> salvarUsuario(UserDto user){
        try{
            UserModel novoUsuario = new UserModel();
            novoUsuario.setEmail(user.getEmail());
            novoUsuario.setNome(user.getNome());
            novoUsuario.setCpf(user.getCpf());
            novoUsuario.setSenha(user.getSenha());
            return userRepository.save(novoUsuario);
        }catch (Exception e){
            throw new RuntimeException("Não foi possível registrar");
        }
    }
    public Mono<UserModel> procurarUsuario(UserLoginDto user){
        //procura usuário através do seu e-mail
        Mono<UserModel> usuario = this.userRepository.findByEmail(user.email);
        //Para acessar um Mono<>: usuario.map(user -> user.metodos)
        String senha = usuario.map(UserModel::getSenha).toString();
        //Verifica a senha e, se estiver correta, retorna o usuario.
        if(passwordEncoder.matches(user.senha, senha)){
            return usuario;
        }
        return null;
    }
}

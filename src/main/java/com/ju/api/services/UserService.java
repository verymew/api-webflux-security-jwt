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
            novoUsuario.setUsername(user.getNome());
            novoUsuario.setCpf(user.getCpf());
            novoUsuario.setPassword(user.getSenha());
            return userRepository.save(novoUsuario);
        }catch (Exception e){
            throw new RuntimeException("Não foi possível registrar");
        }
    }
    public Mono<UserModel> procurarUsuario(UserLoginDto usuario){
        //procura usuário através do username
        Mono<UserModel> user = this.userRepository.findByUsername(usuario.getUsername());
        //Para acessar um Mono<>: usuario.map(user -> user.metodos)
        String senha = user.map(UserModel::getPassword).toString();
        //Verifica a senha e, se estiver correta, retorna o usuario.
        if(passwordEncoder.matches(usuario.password, senha)){
            return user;
        }
        return null;
    }
}

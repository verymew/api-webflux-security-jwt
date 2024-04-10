package com.ju.api.services;

import com.ju.api.dtos.UserDto;
import com.ju.api.models.UserModel;
import com.ju.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    UserRepository userRepository;
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
}

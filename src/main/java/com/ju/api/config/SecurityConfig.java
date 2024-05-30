package com.ju.api.config;


import com.ju.api.config.JwtFilter;
import com.ju.api.models.UserModel;
import com.ju.api.repository.UserRepository;
import com.ju.api.services.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import reactor.core.publisher.Mono;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, TokenService tokenService, ReactiveAuthenticationManager reactiveAuthenticationManager) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/pq").hasAuthority("USER")
                        .pathMatchers("/api/login").permitAll()
                        .pathMatchers("/api/salvar", "/api/ver").authenticated()
                        .pathMatchers("/api/registrar", "/api/csrf").permitAll()
                        .anyExchange().authenticated()
                ).addFilterAt(new JwtFilter(tokenService), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication,
                                                               AuthorizationContext context) {

        return authentication
                .map(a -> context.getVariables().get("user").equals(a.getName()))
                .map(AuthorizationDecision::new);

    }
    @Bean
    public ReactiveUserDetailsService userDetailsService(UserRepository users) {
        return username -> users.findByUsername(username) //busca um USERMODEL através do repositorio
                .map(u -> User //transforma em um USERDETAILS < Muito importante para autenticação
                        .withUsername(u.getUsername())
                        .password(u.getPassword())
                        .authorities(u.getAuthorities())
                        .build());
    }
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder) {
        //Utiliza o userdetails acima para autenticar as requisições
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder); //Utiliza o BCRYPT para fazer a comparação de senha
        return authenticationManager;
    }
}
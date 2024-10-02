package com.ju.api.config;

import com.ju.api.repository.UserRepository;
import com.ju.api.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.awt.*;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter implements WebFilter {
    @Autowired
    private TokenService tokenService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = resgatarToken(exchange.getRequest()); //resgata o código via HttpRequest
        //Verifica se existe token e se ele é um token válido
        var login = this.tokenService.validarToken(token);
        //Se for correto, o filtro vai assinar o token.
        if(login != null){
            return Mono.fromCallable(() -> this.tokenService.pegarAutorizacao(token))
                    .subscribeOn(Schedulers.boundedElastic())
                    .flatMap(authentication -> chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)));
        }
        return chain.filter(exchange);
    }
    private String resgatarToken(ServerHttpRequest request) {
        List<String> cookies = request.getHeaders().get(HttpHeaders.COOKIE);
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.startsWith("access_token=")) {
                    // Extraia o token diretamente após "access_token="
                    String token = cookie.substring("access_token=".length());
                    if (token.contains(";")) {
                        token = token.substring(0, token.indexOf(';'));
                    }
                    token = token.trim();
                    return token;
                }
            }
        }
        return null;
    }
}

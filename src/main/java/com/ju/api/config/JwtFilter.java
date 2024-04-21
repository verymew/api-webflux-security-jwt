package com.ju.api.config;

import com.ju.api.repository.UserRepository;
import com.ju.api.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {
    public static final String HEADER_PREFIX = "Bearer ";
    private final TokenService tokenService;
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
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION); //resgata o headers.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

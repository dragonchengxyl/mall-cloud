package com.mall.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter, Ordered {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        
        // 白名单：不需要token的接口
        if (path.startsWith("/user/list")) {
            return chain.filter(exchange);
        }
        
        // 获取token
        String token = request.getHeaders().getFirst("Authorization");
        
        if (token == null || token.isEmpty()) {
            System.out.println("❌ [鉴权失败] 缺少token: " + path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        // 简单验证（实际项目应该调用认证服务）
        if (!token.equals("Bearer mall-token-2024")) {
            System.out.println("❌ [鉴权失败] token无效: " + path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        System.out.println("🔐 [鉴权通过] " + path);
        return chain.filter(exchange);
    }
    
    @Override
    public int getOrder() {
        return 0; // 日志过滤器之后执行
    }
}

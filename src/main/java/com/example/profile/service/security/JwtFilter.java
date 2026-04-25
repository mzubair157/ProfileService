package com.example.profile.service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final TokenRevocationService tokenRevocationService;

    public JwtFilter(JwtUtils jwtUtils, TokenRevocationService tokenRevocationService) {
        this.jwtUtils = jwtUtils;
        this.tokenRevocationService = tokenRevocationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtils.validate(token)) {
                String tokenId = jwtUtils.getTokenId(token);
                if (!tokenRevocationService.isRevoked(tokenId)) {
                    SecurityContextHolder.getContext().setAuthentication(jwtUtils.getAuthentication(token));
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

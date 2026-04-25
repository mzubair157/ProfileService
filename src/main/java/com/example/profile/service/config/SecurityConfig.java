package com.example.profile.service.config;

import com.example.profile.service.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                // 1. Disable Basic Auth to stop the browser popup
                .httpBasic(AbstractHttpConfigurer::disable)
                // 2. Enable Form Login with the default Spring Security UI
                .formLogin(Customizer.withDefaults())
                // 3. Update Session Policy: Standard formLogin requires a session to store the
                // auth result. Change to IF_REQUIRED so the form works, but JWT remains stateless.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints from your 10K RPM scenario
                        .requestMatchers(HttpMethod.GET, "/profiles/**", "/discounts/**", "/favorites/**",
                                "/recommendations/**", "/media/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/profiles/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/favorites/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/favorites/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/favorites/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").permitAll()
                        // Management and dev tools
                        .requestMatchers("/actuator/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                // 4. Headers needed for the H2 console used in your properties
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // 5. Keep the custom JwtFilter for your 10K RPM stateless auth
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

package com.eventhub.event_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()  // ✅ Permite TUDO sem autenticação (apenas para teste/dev)
            )
            .csrf(csrf -> csrf.disable())   // ✅ Desativa CSRF
            .httpBasic(basic -> basic.disable());  // ✅ Desativa autenticação básica

        return http.build();
    }
}


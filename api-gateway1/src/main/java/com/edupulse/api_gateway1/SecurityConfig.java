package com.edupulse.api_gateway1;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("role");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(
                authoritiesConverter
        );

        return converter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // =========================
                // AUTH
                // =========================
                .requestMatchers("/auth/**")
                .permitAll()


                // =========================
                // STUDENT ATTENDANCE
                // =========================
                .requestMatchers("/api/attendance/student/**")
                .hasAnyRole("ADMIN", "FACULTY", "STUDENT")


                // =========================
                // STUDENT RESULTS
                // =========================
                .requestMatchers("/api/results/student/**")
                .hasAnyRole("ADMIN", "FACULTY", "STUDENT")


                // STUDENT can view individual result
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/results/*"
                )
                .hasAnyRole("ADMIN", "FACULTY", "STUDENT")


                // =========================
                // ADMIN ONLY
                // =========================
                .requestMatchers("/api/admin/**")
                .hasRole("ADMIN")


                // =========================
                // ATTENDANCE
                // =========================
                .requestMatchers("/api/attendance/**")
                .hasAnyRole("ADMIN", "FACULTY")


                // =========================
                // RESULTS
                // =========================
                .requestMatchers("/api/results/**")
                .hasAnyRole("ADMIN", "FACULTY")


                // =========================
                // STUDENT PROFILE
                // =========================
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/students/*"
                )
                .hasAnyRole("ADMIN", "FACULTY", "STUDENT")


                // =========================
                // STUDENT MANAGEMENT
                // =========================
                .requestMatchers("/api/students/**")
                .hasAnyRole("ADMIN", "FACULTY")


                // =========================
                // EVERYTHING ELSE
                // =========================
                .anyRequest()
                .authenticated()
            )

            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(
                        jwtAuthenticationConverter()
                    )
                )
            );

        return http.build();
    }
}
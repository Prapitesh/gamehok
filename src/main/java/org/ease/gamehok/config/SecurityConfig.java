package org.ease.gamehok.config;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC APIs
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/ws/**"
                        )
                        .permitAll()

                        // ADMIN ONLY
                        .requestMatchers(
                                "/api/tournaments/**",
                                "/api/brackets/**"
                        )
                        .hasAuthority("ADMIN")

                        // ADMIN + PLAYER
                        .requestMatchers(
                                "/api/matches/**",
                                "/api/teams/**"
                        )
                        .hasAnyAuthority(
                                "ADMIN",
                                "PLAYER"
                        )

                        // ALL OTHER APIs
                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
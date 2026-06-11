package org.ease.gamehok.config;



import lombok.RequiredArgsConstructor;

import org.ease.gamehok.filter.JwtFilter;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.CorsConfigurationSource;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



import java.util.Arrays;



@Configuration

@RequiredArgsConstructor

public class SecurityConfig {



    private final JwtFilter jwtFilter;



    @Bean

    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:8887",
                "https://gamehok-frontend-q9x3ds7o6.vercel.app",
                "https://gamehok-frontend-beige.vercel.app",
                "https://gamehok-frontend-q9x3ds7o6.vercel.app"
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        configuration.setAllowedHeaders(Arrays.asList("*"));

        configuration.setAllowCredentials(true);

        

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;

    }



    @Bean

    public SecurityFilterChain securityFilterChain(

            HttpSecurity http

    ) throws Exception {



        http

                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))



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

                                "/api/teams/**",

                                "/api/disputes/**",

                                "/api/leaderboards/**"

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
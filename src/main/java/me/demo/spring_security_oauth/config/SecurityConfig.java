package me.demo.spring_security_oauth.config;

import lombok.RequiredArgsConstructor;
import me.demo.spring_security_oauth.service.CustomOauth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOauth2UserService customOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.csrf((csrf)->csrf.disable());

        http.formLogin((login)->login.disable());

        http.httpBasic((basic)->basic.disable());

        http.oauth2Login(
                (oauth2-> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(
                        userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOauth2UserService)
                        )
                )
        );

        http.
                authorizeHttpRequests((auth)->auth
                        .requestMatchers("/","oauth2/**","/login/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

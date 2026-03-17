package com.ic.security.config;

import com.ic.security.filter.HeaderAuthenticationFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Central security configuration.
 *
 * <p>Registers the header-based pre-authentication filter, applies stateless
 * session management, and exposes configuration-driven public endpoints.</p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    @Bean
    public HeaderAuthenticationFilter headerAuthenticationFilter() {
        return new HeaderAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HeaderAuthenticationFilter headerAuthenticationFilter, SecurityProperties securityProperties) throws Exception {

        String[] publicPaths = securityProperties.getPublicPaths().toArray(new String[0]);

        http.csrf(AbstractHttpConfigurer::disable)

                .formLogin(AbstractHttpConfigurer::disable)

                .httpBasic(AbstractHttpConfigurer::disable)

                
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth ->

                        auth.requestMatchers(publicPaths).permitAll()

                                .anyRequest().authenticated())

                .addFilterBefore(headerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}

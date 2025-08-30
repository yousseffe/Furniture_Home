package com.furniturehome.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public routes
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/orders/**").permitAll()
                .requestMatchers("api/payment/**").permitAll()
                .requestMatchers("api/review/**").permitAll()
                .requestMatchers("api/storesetting/**").permitAll()
//              .requestMatchers("/api/users/**").permitAll()
//              .requestMatchers("/api/cart/**").permitAll()

                // Products
                .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                // Categories (ADMIN only for all methods)
                .requestMatchers("/api/categories/**").hasRole("ADMIN")

                //payment
                .requestMatchers(HttpMethod.GET,"api/payment/**").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers(HttpMethod.POST,"api/payment/**").hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.PUT,"api/payment/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"api/payment/**").hasRole("CUSTOMER")

                    //review
                .requestMatchers(HttpMethod.GET,"api/review/**").hasAnyRole("CUSTOMER", "ADMIN")
                            .requestMatchers(HttpMethod.POST,"api/review/**").hasRole("CUSTOMER")
//                            .requestMatchers(HttpMethod.PUT,"api/review/**").hasRole("CUSTOMER")
                            .requestMatchers(HttpMethod.DELETE,"api/review/delete/**").hasRole("CUSTOMER")

                    //store setting
                            .requestMatchers(HttpMethod.GET,"api/storesetting/**").hasAnyRole("CUSTOMER", "ADMIN")
                            .requestMatchers(HttpMethod.PUT,"api/storesetting/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE,"api/storesetting/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT,"api/storesetting/**").hasRole("ADMIN")
                // Everything else requires authentication
                .anyRequest().authenticated()
            );
        return http.build();
    }
}

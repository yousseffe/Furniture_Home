package com.furniturehome.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password("{noop}admin123") 
                .roles("ADMIN")
                .build();

        UserDetails customer = User.withUsername("customer")
                .password("{noop}customer123")
                .roles("CUSTOMER")
                .build();

        return new InMemoryUserDetailsManager(admin, customer);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // ===== AuthController =====
                .requestMatchers("/api/auth/signup", "/api/auth/login").permitAll()
                .requestMatchers("/api/auth/update/**").hasAnyRole("ADMIN", "CUSTOMER")
                .requestMatchers("/api/auth/reset-password/**").hasAnyRole("ADMIN", "CUSTOMER")
                .requestMatchers("/api/auth/logout").hasAnyRole("ADMIN", "CUSTOMER")

                // ===== UserController =====
                // .requestMatchers( "/api/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN") // only admin can list all users
                .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN") // only admin can create users
                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "CUSTOMER")
                .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN", "CUSTOMER")
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAnyRole("ADMIN", "CUSTOMER")
                // .requestMatchers("/api/users/**").permitAll()

                // ===== EnquiryController =====
                // Admin: can only list all enquiries
                .requestMatchers(HttpMethod.GET, "/api/enquiries").hasRole("ADMIN")
                // Admin & Customer: can view specific enquiry
                .requestMatchers(HttpMethod.GET, "/api/enquiries/{id}").hasAnyRole("ADMIN", "CUSTOMER")
                // Customer: manage their own enquiries
                .requestMatchers(HttpMethod.GET, "/api/enquiries/user/**").hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.POST, "/api/enquiries/**").hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.PUT, "/api/enquiries/**").hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.DELETE, "/api/enquiries/**").hasRole("CUSTOMER")

                // ===== Products =====
                .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                // ===== Categories (Admin only) =====
                .requestMatchers("/api/categories/**").hasRole("ADMIN")

                // ===== Payment =====
                .requestMatchers(HttpMethod.GET,"/api/payment/**").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/payment/**").hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.PUT,"/api/payment/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/payment/**").hasRole("CUSTOMER")

                // ===== Review =====
                .requestMatchers(HttpMethod.GET,"/api/review/**").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/review/**").hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.DELETE,"/api/review/delete/**").hasRole("CUSTOMER")

                // ===== Store Settings =====
                .requestMatchers(HttpMethod.GET,"/api/storesetting/**").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/storesetting/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/storesetting/**").hasRole("ADMIN")

                // ===== Default =====
                .anyRequest().authenticated()
            );
        return http.build();
    }
}

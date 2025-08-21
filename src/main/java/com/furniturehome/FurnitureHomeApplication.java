package com.furniturehome;

import com.furniturehome.model.User;
import com.furniturehome.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FurnitureHomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FurnitureHomeApplication.class, args);
    }

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(User.builder()
                        .name("Ali Hassan")
                        .email("ali.hassan@example.com")
                        .phone("01012345678")
                        .password("hashed_password1")
                        .role("customer")
                        .build());

                userRepository.save(User.builder()
                        .name("Mona Youssef")
                        .email("mona.youssef@example.com")
                        .phone("01087654321")
                        .password("hashed_password2")
                        .role("customer")
                        .build());

                userRepository.save(User.builder()
                        .name("Omar Khaled")
                        .email("omar.khaled@example.com")
                        .phone("01123456789")
                        .password("hashed_password3")
                        .role("admin")
                        .build());

                userRepository.save(User.builder()
                        .name("Sara Adel")
                        .email("sara.adel@example.com")
                        .phone("01298765432")
                        .password("hashed_password4")
                        .role("customer")
                        .build());

                userRepository.save(User.builder()
                        .name("Ahmed Nabil")
                        .email("ahmed.nabil@example.com")
                        .phone("01555555555")
                        .password("hashed_password5")
                        .role("seller")
                        .build());
            }
        };
    }
}

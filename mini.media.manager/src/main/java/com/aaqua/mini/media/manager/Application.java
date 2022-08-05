package com.aaqua.mini.media.manager;

import com.aaqua.mini.media.manager.entities.User;
import com.aaqua.mini.media.manager.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            List<User> users = List.of(User.builder()
                            .email("silviu.butnaru@gmail.com")
                            .password("silviu")
                            .build(),
                    User.builder()
                            .email("marian-ilie.butnaru@gmail.com")
                            .password("marian-ilie")
                            .build(),
                    User.builder()
                            .email("cosmin-alexandru.geman@gmail.com")
                            .password("cosmin-alexandru")
                            .build()
            );

            userRepository.deleteAll();
            userRepository.saveAll(users);
        };
    }
}

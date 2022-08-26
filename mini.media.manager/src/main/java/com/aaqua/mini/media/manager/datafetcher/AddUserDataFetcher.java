package com.aaqua.mini.media.manager.datafetcher;

import com.aaqua.mini.media.manager.entity.User;
import com.aaqua.mini.media.manager.model.Credentials;
import com.aaqua.mini.media.manager.repository.UserRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class AddUserDataFetcher {

    private final UserRepository userRepository;

    @DgsMutation
    public User addUser(@InputArgument Credentials credentials) {
        log.info("addUser, {}", credentials);

        UUID id = UUID.randomUUID();

        User newUser = User.builder()
                .id(id.toString())
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .build();

        return userRepository.save(newUser);
    }
}

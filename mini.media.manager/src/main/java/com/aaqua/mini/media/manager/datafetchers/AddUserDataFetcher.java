package com.aaqua.mini.media.manager.datafetchers;

import com.aaqua.mini.media.manager.entities.User;
import com.aaqua.mini.media.manager.models.Credentials;
import com.aaqua.mini.media.manager.repositories.UserRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class AddUserDataFetcher {

    private UserRepository userRepository;

    @DgsMutation
    public User addUser(@InputArgument String id, @InputArgument Credentials credentials) {
        log.info("addUser, id: {}, {}", id, credentials);

        User newUser = User.builder()
                .id(id)
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .build();

        return userRepository.save(newUser);
    }
}

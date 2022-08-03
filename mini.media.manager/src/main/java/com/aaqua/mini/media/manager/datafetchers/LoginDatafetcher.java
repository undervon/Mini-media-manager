package com.aaqua.mini.media.manager.datafetchers;

import com.aaqua.mini.media.manager.exceptions.BadCredentialsException;
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
public class LoginDatafetcher {

    private final UserRepository userRepository;

    @DgsMutation
    public String login(@InputArgument Credentials credentials) {
        log.info("login, Credentials: {}", credentials);

        if (!userRepository.existsUserByEmailAndPassword(credentials.getEmail(), credentials.getPassword())) {
            throw new BadCredentialsException();
        }

        return "Login successful!";
    }
}

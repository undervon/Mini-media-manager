package com.aaqua.mini.media.manager.datafetcher;

import com.aaqua.mini.media.manager.exception.BadCredentialsException;
import com.aaqua.mini.media.manager.model.Credentials;
import com.aaqua.mini.media.manager.repository.UserRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class LoginDataFetcher {

    private UserRepository userRepository;

    @DgsMutation
    public String login(@InputArgument Credentials credentials) {
        log.info("login, {}", credentials);

        if (!userRepository.existsUserByEmailAndPassword(credentials.getEmail(), credentials.getPassword())) {
            throw new BadCredentialsException();
        }

        return "Login successful!";
    }
}

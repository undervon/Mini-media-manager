package com.aaqua.mini.media.manager.datafetcher;

import com.aaqua.mini.media.manager.exception.PostNotFoundException;
import com.aaqua.mini.media.manager.repository.PostRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class DeletePostDataFetcher {

    private final PostRepository postRepository;

    @DgsMutation
    public String deletePosts() {
        log.info("deletePosts");

        postRepository.deleteAll();

        return "Deleting all posts successfully!";
    }

    @DgsMutation
    public String deletePostById(@InputArgument String id) {
        log.info("deletePostById, id: {}", id);

        postRepository.deleteById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        return String.format("Deleting post '%s' successfully!", id);
    }
}

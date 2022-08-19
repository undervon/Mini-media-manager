package com.aaqua.mini.media.manager.datafetcher;

import com.aaqua.mini.media.manager.entity.Post;
import com.aaqua.mini.media.manager.exception.PostNotFoundException;
import com.aaqua.mini.media.manager.repository.PostRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class GetPostDataFetcher {

    private final PostRepository postRepository;

    @DgsQuery
    public List<Post> getPosts() {
        log.info("getPosts");

        return postRepository.findAll();
    }

    @DgsQuery
    public Post getPost(@InputArgument String id) {
        log.info("getPost, id: {}", id);

        return postRepository.findPostById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }
}

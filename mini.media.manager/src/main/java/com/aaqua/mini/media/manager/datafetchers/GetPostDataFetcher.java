package com.aaqua.mini.media.manager.datafetchers;

import com.aaqua.mini.media.manager.entities.Post;
import com.aaqua.mini.media.manager.exceptions.PostNotFoundException;
import com.aaqua.mini.media.manager.repositories.PostRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@DgsComponent
@RequiredArgsConstructor
public class GetPostDataFetcher {

    private PostRepository postRepository;

    @DgsQuery
    public List<Post> getPosts() {
        log.info("getPosts");

        return Lists.newArrayList(postRepository.findAll());
    }

    @DgsQuery
    public Post getPost(@InputArgument String id) {
        log.info("getPost, id: {}", id);

        return postRepository.findPostById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }
}

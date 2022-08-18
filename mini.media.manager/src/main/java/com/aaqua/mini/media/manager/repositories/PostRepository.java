package com.aaqua.mini.media.manager.repositories;

import com.aaqua.mini.media.manager.entities.Post;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface PostRepository extends CrudRepository<Post, String> {

    Optional<Post> findPostById(String id);
}

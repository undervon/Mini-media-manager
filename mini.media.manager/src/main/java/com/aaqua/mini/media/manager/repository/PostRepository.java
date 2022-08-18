package com.aaqua.mini.media.manager.repository;

import com.aaqua.mini.media.manager.entity.Post;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableScan
public interface PostRepository extends CrudRepository<Post, String> {

    Optional<Post> findPostById(String id);
}

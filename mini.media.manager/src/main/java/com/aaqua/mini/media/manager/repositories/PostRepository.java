package com.aaqua.mini.media.manager.repositories;

import com.aaqua.mini.media.manager.entities.Post;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
public interface PostRepository extends CrudRepository<Post, String> {

}

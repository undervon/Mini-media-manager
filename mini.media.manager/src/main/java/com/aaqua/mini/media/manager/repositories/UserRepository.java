package com.aaqua.mini.media.manager.repositories;

import com.aaqua.mini.media.manager.entities.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UserRepository extends CrudRepository<User, String> {

    Boolean existsUserByEmailAndPassword(String email, String password);
}

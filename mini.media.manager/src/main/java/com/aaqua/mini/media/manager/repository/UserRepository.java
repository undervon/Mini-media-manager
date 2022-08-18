package com.aaqua.mini.media.manager.repository;

import com.aaqua.mini.media.manager.entity.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
public interface UserRepository extends CrudRepository<User, String> {

    Boolean existsUserByEmailAndPassword(String email, String password);
}

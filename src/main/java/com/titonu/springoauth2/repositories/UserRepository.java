package com.titonu.springoauth2.repositories;

import com.titonu.springoauth2.model.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User,Long> {

    User findByUsername(String username);
    User findByUsernameAndEmail(String username, String email);
    User findByEmail(String email);
    User findTopByEmail(String email);

}

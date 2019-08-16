package com.titonu.springoauth2.repositories;

import com.titonu.springoauth2.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface RoleRepository extends CrudRepository<Role, Long> {
//    Collection<Role> findByName(String name);
    Role findByName(String role_name);
    List<Role> findByNameIn(List<String> roles);
}

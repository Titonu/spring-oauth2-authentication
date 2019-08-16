package com.titonu.springoauth2.config;

import com.titonu.springoauth2.model.Role;
import com.titonu.springoauth2.model.User;
import com.titonu.springoauth2.repositories.RoleRepository;
import com.titonu.springoauth2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SeedData implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SeedData(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        Role roleAdmin = createRoleIfNotFound("ROLE_ADMIN");
        Role roleUser = createRoleIfNotFound("ROLE_USER");

        User userAdmin = new User();
        userAdmin.setUsername("admin");
        userAdmin.setEmail("admin@test.com");
        userAdmin.setPassword(passwordEncoder.encode("admin"));
        List<Role> rolesUserAdmin = Stream.of(roleAdmin).collect(Collectors.toList());
        userAdmin.setRoles(rolesUserAdmin);
        userAdmin.setEnabled(true);
        userRepository.save(userAdmin);

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@test.com");
        user.setPassword(passwordEncoder.encode("user123"));
        List<Role> rolesUser = Stream.of(roleUser).collect(Collectors.toList());
        user.setRoles(rolesUser);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            return  roleRepository.save(role);
        }
        return role;
    }

}

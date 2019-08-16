package com.titonu.springoauth2.services.security;

import com.titonu.springoauth2.model.Role;
import com.titonu.springoauth2.model.User;
import com.titonu.springoauth2.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("userDetailsService")
@Transactional
public class UserDetailService implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(UserDetailService.class);

    @Autowired
    private UserRepository userRepository;

    public UserDetailService() {
        super();
    }

    // API

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            logger.debug("Username : {}",username);
            User userByUsername;
            try {
                userByUsername = userRepository.findByUsername(username);
            } catch (NullPointerException e) {
                throw new UsernameNotFoundException("No user found with username: " + username);
            }

            Collection<Role> listRole = new ArrayList<>(userByUsername.getRoles());
            return new org.springframework.security.core.userdetails.User(username,
                    userByUsername.getPassword(),
                    userByUsername.isEnabled(), true, true, true,
                    getAuthorities(listRole));
        } catch (final Exception e) {
            logger.error("Error creating user detail :{}",e.getMessage());
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
    }

    // UTIL
    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (Role role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

}
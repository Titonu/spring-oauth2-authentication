package com.titonu.springoauth2.services;


import com.titonu.springoauth2.model.Role;

public interface IUserRegister {
    boolean isUsernameExist(String username);
    boolean isEmailExist(String email);
    boolean isRoleExist(String rolename);
    Role createRoleifNotFound(String role_user);
}

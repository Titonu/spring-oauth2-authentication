package com.titonu.springoauth2.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 *The @EnableResourceServer annotation adds a filter of type OAuth2AuthenticationProcessingFilter automatically
 *to the Spring Security filter chain.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .headers()
                .frameOptions()
                .disable().and()
            .authorizeRequests()
                .antMatchers("/login", "/forgot_password","/verification", "/reset_password","/company/register",
                        "/company/add_apps", "/company/register_user", "/cek_login/**", "private/kitaBela/*/register", "/public/**").permitAll()
                .antMatchers("/private/surveyor/api/admin/**").access("hasRole('ADMIN') && hasAuthority('SURVEYOR')")
                .antMatchers("/private/surveyor/api/**").hasAuthority("SURVEYOR")
                .antMatchers("/private/surveyor/admin/*").hasRole("ADMIN")
                .antMatchers("/private/lead/api/**").hasAuthority("LEAD")
                .antMatchers("/register/").hasRole("ADMIN")
                .antMatchers("/admin_zone").hasRole("ADMIN")
                .antMatchers("/catchaauth").hasRole("REGISTER")
                .anyRequest().authenticated();
        //TODO : get from database
    }


}

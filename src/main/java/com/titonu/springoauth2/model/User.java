package com.titonu.springoauth2.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
//@JsonIgnoreProperties
@Table(name = "j_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    @Column(length = 60)
    private String password;
    @ManyToMany
    private List<Role> roles = new ArrayList<>();
    private boolean enabled;
    private boolean mustChangePassword;
    @Column(name = "created_date")
    @CreatedDate
    private Date createdDate;
    @Column(name = "last_login")
    private Date lastLogin;
}
package com.titonu.springoauth2.services;

import com.titonu.springoauth2.model.Role;
import com.titonu.springoauth2.model.User;
import com.titonu.springoauth2.model.VerificationToken;
import com.titonu.springoauth2.repositories.RoleRepository;
import com.titonu.springoauth2.repositories.UserRepository;
import com.titonu.springoauth2.repositories.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Service
public class UserService implements IUserRegister, MailService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, VerificationTokenRepository verificationTokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isUsernameExist(String name) {
        System.out.println("username = " + name);
        User user = userRepository.findByUsername(name);
        if (user == null){
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmailExist(String email) {
        System.out.println("username = " + email);
        User user = userRepository.findTopByEmail(email);
        if (user == null){
            return false;
        }
        return true;
    }

    @Override
    public boolean isRoleExist(String rolename) {
        System.out.println("rolename = " + rolename);
        Role role = roleRepository.findByName(rolename);
        if (role == null){
            return false;
        }
        return true;
    }

    @Override
    public Role createRoleifNotFound(String name) {
        Role roleExist = roleRepository.findByName(name);
        if (roleExist == null) {
            Role newRole = new Role();
            newRole.setName(name);
            return roleRepository.save(newRole);
        }
        return roleExist;
    }

    public Date currentTimeStamp(){
        Date date = new Date();
        Long time = date.getTime();
        System.out.println("time = " + time);
        Timestamp timestamp = new Timestamp(time);
        System.out.println("timestamp = " + timestamp);
        return timestamp;
    }

    public void getVerificationToken(final User user, final String token, final String tempPassword) {
        final VerificationToken myToken = new VerificationToken(token, user);
        if (tempPassword != null) {
            myToken.setTempPassword(passwordEncoder.encode(tempPassword));
        }
        verificationTokenRepository.save(myToken);
    }

    public String validatePasswordResetToken(long id, String token) {
        final VerificationToken passToken = verificationTokenRepository.findByToken(token);
        if ((passToken == null) || (passToken.getUser().getId() != id)) {
            return "invalidToken";
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "expired";
        }
        final User user = passToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        return null;
    }

    public SimpleMailMessage constructVerificationTokenEmail(final String contextPath, final String password,
                                                             final String token, final User user,
                                                             String compName, String appsName) {
        final String url = contextPath + "/verification?id=" + user.getId() + "&token=" + token;
//        user.setPassword(password);
        logger.debug("url token forgot password: {}", url);
        return constructEmail("Verify Account", "Click link below to verified your account" + " \r\n"
                + url + " \r\n" + " \r\n"
                + "Apps name "+"\t"+": " + appsName + " \r\n"
                + "Company name "+"\t"+": " + compName + " \r\n" + " \r\n"
                + "username "+"\t"+": " + user.getUsername() + " \r\n"
                + "password "+"\t"+": "+ password, user);
    }

    public SimpleMailMessage constructResetPasswordTokenEmail(final String contextPath, final String password,
                                                             final String token, final User user) {
        final String url = contextPath + "/reset_password?id=" + user.getId() + "&token=" + token;
        System.out.println("url = " + url);
        return constructEmail("Reset Password", "Click link below to reset your password" + " \r\n"
                + url + " \r\n" + " \r\n"
                + "username "+"\t"+": " + user.getUsername() + " \r\n"
                + "new password "+"\t"+": "+ password, user);
    }

    public SimpleMailMessage constructResetTokenEmail(final String contextPath, final String token, final User user) {
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        System.out.println("url = " + url);
        return constructEmail("Reset Password", "Click link below to reset your password" + " \r\n" + url, user);
    }

    public SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        /*email.setFrom(env.getProperty("support.email"));*/
        return email;
    }

    public String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}

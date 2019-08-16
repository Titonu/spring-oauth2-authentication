package com.titonu.springoauth2.services;

import com.titonu.springoauth2.model.User;
import org.springframework.mail.SimpleMailMessage;

public interface MailService {

    SimpleMailMessage constructVerificationTokenEmail(final String contextPath, final String password,
                                                      final String token, final User user,
                                                      String compName, String appsName);
    SimpleMailMessage constructResetTokenEmail(final String contextPath, final String token, final User user);
    SimpleMailMessage constructEmail(String subject, String body, User user);

}

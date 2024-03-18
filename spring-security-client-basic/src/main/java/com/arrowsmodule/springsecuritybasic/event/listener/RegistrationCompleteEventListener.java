package com.arrowsmodule.springsecuritybasic.event.listener;

import com.arrowsmodule.springsecuritybasic.entity.User;
import com.arrowsmodule.springsecuritybasic.entity.VerificationToken;
import com.arrowsmodule.springsecuritybasic.event.RegistrationCompleteEvent;
import com.arrowsmodule.springsecuritybasic.service.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent even) {
        User user = even.getUser();
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token,user);
        verificationTokenService.saveVerificationToken(verificationToken);

        //creating verifying url
        String verifyingURL = even.getApplicationUrl()+"/verifyRegistration?token="+token;
        log.info("click on link to verify : {}",verifyingURL);
    }
}

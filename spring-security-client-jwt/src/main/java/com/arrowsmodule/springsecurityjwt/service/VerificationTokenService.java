package com.arrowsmodule.springsecurityjwt.service;

import com.arrowsmodule.springsecurityjwt.entity.User;
import com.arrowsmodule.springsecurityjwt.entity.VerificationToken;
import com.arrowsmodule.springsecurityjwt.repository.UserDao;
import com.arrowsmodule.springsecurityjwt.repository.VerificationTokenDao;
import com.arrowsmodule.springsecurityjwt.service.common.UtilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class VerificationTokenService {
    @Autowired
    private VerificationTokenDao verificationTokenDao;
    @Autowired
    private UserDao userDao;

    public void saveVerificationToken(VerificationToken verificationToken){
        verificationTokenDao.save(verificationToken);
    }

    public boolean validateRegistrationToken(String token){
        VerificationToken verificationToken = verificationTokenDao.findByToken(token);
        if(verificationToken != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(new Date().getTime());
            if(verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
                verificationTokenDao.delete(verificationToken);
                return false;
            }else {
                User user = verificationToken.getUser();
                user.setEnable(true);
                userDao.save(user);
                verificationTokenDao.delete(verificationToken);
                return true;
            }
        }
        return false;
    }

    public boolean resendVerificationToken(String email,String applicationUrl){

        Optional<User> user = userDao.findByEmail(email);

        if(user.isPresent()){
            if(!user.get().isEnable()){
                VerificationToken verificationToken = verificationTokenDao.findByUser(user.get().getId()).get(0);
                String token = UUID.randomUUID().toString();

                verificationToken.setToken(token);
                verificationToken.setExpirationTime(UtilityService.calculateExpirationTime());
                saveVerificationToken(verificationToken);

                //creating verifying url
                String verifyingURL = applicationUrl+"/verifyRegistration?token="+token;
                log.info("click on link to verify : {}",verifyingURL);
                return true;
            }
            log.info("{} is already enabled",email);
            return false;
        }
        log.info("{} is not found",email);
        return false;
    }
}

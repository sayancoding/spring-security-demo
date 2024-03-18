package com.arrowsmodule.springsecurityjwt.controller;

import com.arrowsmodule.springsecurityjwt.component.JwtService;
import com.arrowsmodule.springsecurityjwt.entity.User;
import com.arrowsmodule.springsecurityjwt.event.RegistrationCompleteEvent;
import com.arrowsmodule.springsecurityjwt.model.LoginModel;
import com.arrowsmodule.springsecurityjwt.model.ResponseModel;
import com.arrowsmodule.springsecurityjwt.model.UserModel;
import com.arrowsmodule.springsecurityjwt.service.UserService;
import com.arrowsmodule.springsecurityjwt.service.VerificationTokenService;
import com.arrowsmodule.springsecurityjwt.service.common.UtilityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationTokenService tokenService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/generateToken")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody LoginModel loginModel) {
        String accessToken = null;
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginModel.getUsername(), loginModel.getPassword()));
        if (authentication.isAuthenticated()) {
            accessToken = jwtService.generateToken(loginModel.getUsername());
            return new ResponseEntity<>(accessToken,
                    HttpStatus.OK);
        } else {
//            throw new UsernameNotFoundException("invalid user request !");
            return new ResponseEntity<>("Unauthorized user request !",
                    HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        if(userModel.getPassword().equals(userModel.getConfirmPassword())){
            User user = userService.save(userModel);
            publisher.publishEvent(new RegistrationCompleteEvent(user, UtilityService.getApplicationURL(request)));
            return new ResponseEntity<>(ResponseModel.builder().message("New User is created").build(),
                                        HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(ResponseModel.builder().message("Both Password is not matched").build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<?> verifyRegistrationToken(@RequestParam("token") String token){
        if(tokenService.validateRegistrationToken(token)){
            return new ResponseEntity<>("Registration token is verified",
                    HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid token",
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/resendToken")
    public ResponseEntity<?> resendRegistrationToken(@RequestParam("email") String email,final HttpServletRequest request){
        if(tokenService.resendVerificationToken(email,UtilityService.getApplicationURL(request))){
            return new ResponseEntity<>("Verification link has been re send",
                    HttpStatus.OK);
        }
        return new ResponseEntity<>("something is wrong",
                HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/user")
    public ResponseEntity<?> findAllUser(){
        return new ResponseEntity<>(userService.findAllUsers(),HttpStatus.OK);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        UserModel userModel = userService.findById(id);
        if(userModel != null){
            return new ResponseEntity<>(userModel,HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseModel.builder().message("No Such User found").build(),
                HttpStatus.NOT_FOUND);
    }
    @GetMapping("/user/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable("email") String email){
        UserModel userModel = userService.findByEmail(email);
        if(userModel != null){
            return new ResponseEntity<>(userModel,HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseModel.builder().message("No Such User found").build(),
                HttpStatus.NOT_FOUND);
    }



}

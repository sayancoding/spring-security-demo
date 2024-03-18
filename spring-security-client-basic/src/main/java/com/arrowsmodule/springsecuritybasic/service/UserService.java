package com.arrowsmodule.springsecuritybasic.service;

import com.arrowsmodule.springsecuritybasic.entity.User;
import com.arrowsmodule.springsecuritybasic.model.UserModel;
import com.arrowsmodule.springsecuritybasic.repository.UserDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(UserModel userModel){
        User user = new User();
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getFirstName());
        user.setEmail(userModel.getEmail());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        return userDao.save(user);
    }

    public List<UserModel> findAllUsers(){
        return userDao.findAll().stream().map(el -> {
           UserModel userModel = new UserModel();
           BeanUtils.copyProperties(el,userModel);
           return userModel;
        }).collect(Collectors.toList());
    }
    public UserModel findById(Long id){
        UserModel userModel = new UserModel();
        Optional<User> user = userDao.findById(id);
        if(user.isPresent()) {
            BeanUtils.copyProperties(user.get(),userModel);
            return userModel;
        }
        return null;
    }
    public UserModel findByEmail(String email){
        UserModel userModel = new UserModel();
        Optional<User> user = userDao.findByEmail(email);
        if(user.isPresent()) {
            BeanUtils.copyProperties(user.get(),userModel);
            return userModel;
        }
        return null;
    }
}

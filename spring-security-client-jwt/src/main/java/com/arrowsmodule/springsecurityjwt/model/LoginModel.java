package com.arrowsmodule.springsecurityjwt.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginModel {
    private String username;
    private String password;
}

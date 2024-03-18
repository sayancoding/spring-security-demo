package com.arrowsmodule.springsecurityjwt.service.common;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Calendar;
import java.util.Date;

public class UtilityService {
    private final static int Expiration_Period = 10;
    public static Date calculateExpirationTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,Expiration_Period);
        return calendar.getTime();
    }
    public static String getApplicationURL(HttpServletRequest request){
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}

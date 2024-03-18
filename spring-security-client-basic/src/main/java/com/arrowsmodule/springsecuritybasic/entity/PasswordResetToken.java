package com.arrowsmodule.springsecuritybasic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class PasswordResetToken {
    //in minutes
    private static final int Expiration_Period = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_PWD_RESET_TOKEN"))
    private User user;

    public PasswordResetToken(String token, User user) {
        super();
        this.token = token;
        this.expirationTime = calculateTime();
        this.user = user;
    }
    private Date calculateTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,Expiration_Period);
        return calendar.getTime();
    }
}

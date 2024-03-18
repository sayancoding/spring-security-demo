package com.arrowsmodule.springsecurityjwt.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseModel {
    private String message;
    private Object body;
}

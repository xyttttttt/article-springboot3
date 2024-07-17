package com.xyt.articlespringboot3.entity.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class RefreshTokenRequest implements Serializable {


    private static final long serialVersionUID = 1L;
    private String refreshToken;

}

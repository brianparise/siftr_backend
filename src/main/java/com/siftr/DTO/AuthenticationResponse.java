package com.siftr.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {
    private String accessToken;
    private String error;
}

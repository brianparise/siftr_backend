package com.siftr.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateRequest
{
    private String username;
    private String password;
}

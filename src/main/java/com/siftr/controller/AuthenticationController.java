package com.siftr.controller;

import com.siftr.DTO.AuthenticateRequest;
import com.siftr.DTO.AuthenticationResponse;
import com.siftr.Repository.AppUserRepository;
import com.siftr.Repository.LogRepository;
import com.siftr.authentication.AuthenticationService;
import com.siftr.authentication.LDAPAuthenticationExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.siftr.constants.Macros.kApplicationAuthorizationSuccess;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    @Value("${spring.ldap.server}")
    private String ldapServer;

    @Value("${spring.ldap.port}")
    private int ldapPort;
    @Autowired
    private ApplicationContext applicationContext;
    private AppUserRepository appUserRepository = null;
    private LogRepository logRepository = null;

    @Autowired
    private AuthenticationService authenticationService;

    public AuthenticationController()
    {
        System.out.println("+++++++++++++++ Authentication Controller" + ldapPort);

    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticateRequest request, @RequestHeader HttpHeaders headers)
    {
        try
        {
            // get the Application token that gets passed
            String applicationToken = headers.getFirst("ApplicationKey");
            System.out.println("application token " + applicationToken);
            if(Objects.equals(authenticationService.ApplicationAuthorize(applicationToken), kApplicationAuthorizationSuccess)) {
                if (appUserRepository == null) {
                    appUserRepository = applicationContext.getBean(AppUserRepository.class);
                    authenticationService.setAppUserRepository(appUserRepository);
                }
                if (logRepository == null) {
                    logRepository = applicationContext.getBean(LogRepository.class);
                    authenticationService.setLogRepository(logRepository);
                }
                return authenticationService.Authenticate(ldapServer, ldapPort, request.getUsername(), request.getPassword());
            }else
            {
                AuthenticationResponse response = new AuthenticationResponse();
                response.setError("Invalid! Request");
                return response;
            }
        }catch(LDAPAuthenticationExeption ex)
        {
            AuthenticationResponse response = new AuthenticationResponse();
            response.setError(ex.getMessage());

            // return a useful exception message
            return response;
        }

    }
}

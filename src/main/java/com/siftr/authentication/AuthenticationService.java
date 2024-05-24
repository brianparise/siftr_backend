package com.siftr.authentication;

import com.siftr.DTO.AuthenticationResponse;

import com.siftr.Repository.AppUserRepository;
import com.siftr.Repository.LogRepository;
import com.siftr.entity.AccessLog;
import com.siftr.entity.AppUser;
import com.unboundid.ldap.sdk.*;
import com.unboundid.util.ssl.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.sql.Timestamp;
import java.util.Objects;

import static com.siftr.constants.Macros.*;
import static com.siftr.macros.Macros.*;

@Component
@Configuration
@Getter
@Setter
public class AuthenticationService
{
    @Value("${spring.application.token}")
    private String applicationToken;
    @Value("${spring.ldap.login.timeout}")
    private int loginTimeout;

    private String bearerHeader = "Bearer ";
    private AppUserRepository appUserRepository;
    private LogRepository logRepository;
    private final SecretKey secretKey;
    private final SignatureAlgorithm signatureAlgorithm;
    public AuthenticationService() {
        this.signatureAlgorithm = SignatureAlgorithm.HS512;
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    private String generateToken(String username)
    {
        String bearerToken = TokenGenerator.generateToken(username, secretKey, signatureAlgorithm);
        String decodedUser = TokenGenerator.decodeToken(bearerToken,secretKey);
        System.out.println("AuthenticationService generateToken " + decodedUser);
        return bearerToken;
    }

    private boolean authenticate(String ldapServer, int ldapPort, String username, String password)
    {
        try {

            TrustAllTrustManager tm = new TrustAllTrustManager();
            TrustManager[] trustManagers = new TrustManager[] {tm};
            SSLUtil sslUtil = new SSLUtil(trustManagers);
            SSLSocketFactory socketFactory = sslUtil.createSSLSocketFactory();
            LDAPConnection connection = new LDAPConnection(socketFactory,ldapServer, ldapPort);

            if(connection.isConnected())
            {
                BindResult bindResult = connection.bind(username, password);
                if(bindResult.getResultString().toLowerCase().contains("success"))
                {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public AuthenticationResponse Authenticate(String ldapServer, int ldapPort, String username, String password) throws LDAPAuthenticationExeption
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        AuthenticationResponse response = new AuthenticationResponse();
        // create a log
        AccessLog log = new AccessLog();

        if(authenticate(ldapServer, ldapPort, username, password))
        {
            // do database things
            AppUser user = appUserRepository.findByEmailAddress(username);
            if(user != null)
            {
                String token = generateToken(username);
                System.out.println("THE TOKEN IS: " + token);
                // add token and date
                user.setAuthenticationToken(token);
                user.setLastLogin(now);
                user.setLastAccess(now);
                appUserRepository.save(user);

                // write access log
                String logMessage = String.format( kLoginSuccess, user.getEmail(), now);
                log.setLog(logMessage);
                log.setLogTime(now);
                logRepository.save(log);
                response.setAccessToken(token);
                response.setError(kSuccess);

            }else
            {
                String logMessage = String.format( kLoginFailed, username,  now);
                log.setLog(logMessage);
                log.setLogTime(now);
                logRepository.save(log);
                response.setError(kInvalidLogin);
            }
        }else
        {
            String logMessage = String.format( kLoginFailed, username,  now);
            log.setLog(logMessage);
            log.setLogTime(now);
            logRepository.save(log);
            response.setError(kInvalidLogin);
        }

        // handle exception
        return response;
    }

    public String ApplicationAuthorize(String applicationToken)
    {
        boolean success = Objects.equals(this.applicationToken, applicationToken);
        if(success)
        {
            return kApplicationAuthorizationSuccess;
        }
        return kAuthenticaotionFailed;
    }

    public String Authorize(String applicationToken, String bearerToken)
    {
        try
        {
            // pull the username out of the token
            boolean userAuthentication = false;
            boolean applicationAuthorization =  Objects.equals(this.applicationToken, applicationToken);
            String strippedBearerToken = "";

            // by convention the bearer token from the header starts with the word Bearer, so strip it before we
            // do anything
            if(bearerToken.contains(bearerHeader))
            {
                strippedBearerToken = bearerToken.replace(bearerHeader,"");
            }else
            {
                // This means the Authorization header wasn't formatted right
                return kAuthenticaotionFailed;
            }
            String username = TokenGenerator.decodeToken(strippedBearerToken, secretKey);
            // look up user;
            if(username != null) {
                AppUser user = appUserRepository.findByEmailAddress(username);
                if (user != null) {
                    if (applicationAuthorization) {
                        // we check the timeout
                        Timestamp now = new Timestamp(System.currentTimeMillis());
                        Timestamp lastAccess = user.getLastAccess();
                        long diff = (now.getTime() - lastAccess.getTime()) / 1000;
                        if (diff > loginTimeout) {
                            // is this enough
                            return kAuthenticaotionTimeout;
                        }
                        user.setLastAccess(new Timestamp(System.currentTimeMillis()));
                        appUserRepository.save(user);
                    }
                    if (applicationAuthorization)
                    {
                        return kApplicationAuthorizationSuccess;
                    }

                }
            }
            // TODO: put in an error
            return kApplicationAuthorizationFailed;
        }catch(Exception e)
        {
            e.printStackTrace();
            // we swallow the exception.  kinda doesn't matter what went wrong.
            // it could be the bearer token is malformed,  the user isn't found
            // there's a database error, etc.....
            return kUnKnownError;
        }

    }
}

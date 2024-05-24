package com.siftr.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Random;

public class TokenGenerator {
    private static int length = 10;

    public TokenGenerator(){
        System.out.println(length);

    }
    public static String generateToken(String username, SecretKey key, SignatureAlgorithm signatureAlgorithm) {

        Timestamp now = new Timestamp(System.currentTimeMillis());
        // generate access token
        // TODO:  Change all api endpoints to use the authentication

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public static String decodeToken(String bearerToken, SecretKey key)
    {
        String username;
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(bearerToken);
            username = claims.getBody().getSubject();
        }catch (Exception e)
        {
            username = null;
        }
        return username;
    }
}

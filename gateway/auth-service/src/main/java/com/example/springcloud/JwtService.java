package com.example.springcloud;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class JwtService {

    public static final String KEY = "changeIt";
    //（秘钥）发行方
    public static final String ISSUER = "bai";
    private static final long TOKEN_EXP_TIME = 60000;
    public static final String USER_NAME = "username";

    /**
     * 生产token
     * @param account
     * @return
     */
    public String token(Account account) {
        Date now = new Date();
        Algorithm algorithm = Algorithm.HMAC256(KEY);

        String token = JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + TOKEN_EXP_TIME))
                .withClaim(USER_NAME, account.getUsername())
                //.withClaim("ROLE", "");
                .sign(algorithm);
        log.info("jwt generated user={}", account.getUsername());
        return token;
    }

    /**
     * 校验token
     * @param token
     * @param username
     * @return
     */
    public boolean verify(String token, String username) {
        log.info("verify jwt username={}", username);
        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .withClaim(USER_NAME, username)
                    .build();
            //验证不通过会抛异常
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error("auth failed", e);
            return false;
        }
    }
}

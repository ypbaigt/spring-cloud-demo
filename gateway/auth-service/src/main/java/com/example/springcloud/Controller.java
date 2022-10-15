package com.example.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
public class Controller {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    public AuthResponse login(@RequestParam String username,
                              @RequestParam String password) {

        Account account = Account.builder()
                .username(username)
                .build();

        //TODO 验证username和密码匹配

        String token = jwtService.token(account);
        account.setToken(token);
        account.setRefreshToken(UUID.randomUUID().toString());

        redisTemplate.opsForValue().set(account.getRefreshToken(), account);

        return AuthResponse.builder()
                .account(account)
                .code(ErrorCode.SUCCESS)
                .build();
    }

    @PostMapping("/refreshToken")
    public AuthResponse refresh(@RequestParam String refreshToken) {
        Account account = (Account) redisTemplate.opsForValue().get(refreshToken);
        if (account == null) {
            return AuthResponse.builder()
                    .code(ErrorCode.USER_NOT_FOUND)
                    .build();
        }
        String jwt = jwtService.token(account);
        account.setToken(jwt);
        account.setRefreshToken(UUID.randomUUID().toString());
        redisTemplate.delete(refreshToken);
        redisTemplate.opsForValue().set(account.getRefreshToken(), account);

        return AuthResponse.builder()
                .account(account)
                .code(ErrorCode.SUCCESS)
                .build();
    }

    @GetMapping("/verify")
    public AuthResponse verify(@RequestParam String token,
                                @RequestParam String username) {
        boolean success = jwtService.verify(token, username);
        return AuthResponse.builder()
                .code(success ? ErrorCode.SUCCESS : ErrorCode.USER_NOT_FOUND)
                .build();
    }

}

package com.example.springcloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Serializable redis存储需要
public class Account implements Serializable {

    private String username;

    private String token;

    private String refreshToken;

}

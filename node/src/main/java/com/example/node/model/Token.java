package com.example.node.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Token {
    private String token;
    private User user;
    private LocalDateTime createTime;
    private LocalDateTime expireTime;
}

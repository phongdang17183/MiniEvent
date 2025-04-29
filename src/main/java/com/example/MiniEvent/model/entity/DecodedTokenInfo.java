package com.example.MiniEvent.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DecodedTokenInfo {
    private String uid;
    private String email;
    private String provider;
}

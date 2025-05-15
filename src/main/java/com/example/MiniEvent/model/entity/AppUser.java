package com.example.MiniEvent.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser {
    private String id;
    private String username;
    private String email;
    private Instant createDay;
    private Integer eventCreate;
    private Integer eventJoin;
    private String phone;
    private String image;
}

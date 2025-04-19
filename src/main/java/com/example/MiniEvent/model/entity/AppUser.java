package com.example.MiniEvent.model.entity;


import com.google.cloud.Date;
import com.google.cloud.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser {
    private String id;
    private String username;
    private String email;
    private Timestamp createDay;
    private Integer eventCreate;
    private Integer eventJoin;
    private String phone;
    private String image;
}

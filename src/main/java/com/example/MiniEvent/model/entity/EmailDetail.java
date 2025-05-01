package com.example.MiniEvent.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailDetail {
    private String to;
    private String from;
    private String subject;
    private String msgBody;
}

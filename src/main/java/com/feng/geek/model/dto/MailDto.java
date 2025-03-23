package com.feng.geek.model.dto;

import lombok.Data;

@Data
public class MailDto {
    private String toEmail;
    private String title;
    private String content;
}

package com.ic.employee_service.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailNotificationEvent {

    private String to;
    private String subject;
    private String body;


}

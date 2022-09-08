package com.endava.hackaton.worldmap.exception;

import lombok.Data;

@Data
public class ApiError {

    private String message;
    private Integer statusCode;
    private long timestamp;

}

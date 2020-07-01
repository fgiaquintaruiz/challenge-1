package com.fggr.naranjaxchallenge.dto;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {

    private Integer code;
    private String message;
    private List<String> violations;

}

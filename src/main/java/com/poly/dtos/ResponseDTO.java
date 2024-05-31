package com.poly.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO<T> {
    private int status;
    private String message;
    private  T data;
}

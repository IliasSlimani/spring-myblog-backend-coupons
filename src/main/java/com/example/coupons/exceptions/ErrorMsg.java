package com.example.coupons.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMsg {

    private String message;

    private String statusCode;

    private Date timestamp;


}

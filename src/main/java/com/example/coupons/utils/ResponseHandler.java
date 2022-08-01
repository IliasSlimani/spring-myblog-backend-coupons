package com.example.coupons.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ResponseHandler {

    public ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object reponseObj) {
        Map<String, Object> map = new LinkedHashMap<String,Object>();
        map.put("message", message);
        map.put("status", status);
        map.put("data", reponseObj);

        return new ResponseEntity<Object>(map,status);

    }
}

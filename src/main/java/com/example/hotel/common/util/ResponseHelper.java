package com.example.hotel.common.util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public class ResponseHelper {
    public static ResponseEntity<Object> getResponse(Object obj, HttpStatus status) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("data", obj);
        map.put("hasErrors", false);
        map.put("errors", "");
        map.put("timestamp", LocalDateTime.now());
        map.put("status", status.value());

        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> getErrorResponse(BindingResult errors, HttpStatus status) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("data", null);
        map.put("hasErrors", true);
        map.put("errors", ErrorHelper.getAllError(errors));
        map.put("timestamp", LocalDateTime.now());
        map.put("status", status.value());

        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> getErrorResponse(String error, HttpStatus status) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("data", null);
        map.put("hasErrors", true);
        map.put("errors", error);
        map.put("timestamp", LocalDateTime.now());
        map.put("status", status.value());

        return new ResponseEntity<Object>(map, status);
    }
}
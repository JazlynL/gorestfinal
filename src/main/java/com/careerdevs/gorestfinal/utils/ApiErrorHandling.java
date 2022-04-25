package com.careerdevs.gorestfinal.utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiErrorHandling {
    public static ResponseEntity<?> genericApiError (Exception e) {
        System.out.println(e.getMessage());
        System.out.println(e.getClass());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<?> customApiError (String message, HttpStatus status) {
        return new ResponseEntity<>(message, status);
    }

    // creating boolean to see whether or not what the user imported is a String.
    public static boolean isStrNaN(String strNum){
        //  checking if the user DATA is null.
        if(strNum == null ){
            return true;
        }

        try {

            // parsing the int so it can be output by a String.
            Integer.parseInt(strNum);
        } catch (NumberFormatException e) {
            return true;
        }

        return false;
    }

}

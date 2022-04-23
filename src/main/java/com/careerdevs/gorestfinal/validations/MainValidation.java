package com.careerdevs.gorestfinal.validations;

public class MainValidation {
    public static void main(String [] args){


        ValidationError testVal = new ValidationError();


        //this outputs the message as JSONData.
        testVal.addError("name", "Name is an invalid input");
        testVal.addError("age", "Age is an invalid input");
        testVal.addError("email","email is invalid");

    }


}

package com.careerdevs.gorestfinal.validations;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

    public class ValidationError {
        /*
HashMap in Java is a collection based on Map and consists of key-value pairs.
A HashMap is denoted by < Key, Value > or < K, V >.
A HashMap element can be accessed using a Key i.e. we must know the key to access the HashMap element
        */
        private final HashMap<String, String> errors = new HashMap<>();

        public void addError (String key, String errorMsg) {
            errors.put(key, errorMsg);
        }
        public boolean hasError() {
            return errors.size() != 0;
        }

        @Override
        public String toString() {
            StringBuilder errorMessage = new StringBuilder("ValidationError:\n");
            for (Map.Entry<String, String> err : errors.entrySet()) {
                errorMessage.append(err.getKey()).append(": ").append(err.getValue()).append("\n");
            }
            return errorMessage.toString();
        }

        public String toJSONobject(){
            return new JSONObject(errors).toString();

        }
}

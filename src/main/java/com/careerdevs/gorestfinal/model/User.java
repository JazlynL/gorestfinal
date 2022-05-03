package com.careerdevs.gorestfinal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//
@Entity
public class User {

    /*
  {
    "id": 2696,
    "name": "Chandrakala Agarwal",
    "email": "agarwal_chandrakala@dicki-robel.net",
    "gender": "female",
    "status": "inactive"
  },*/

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private int id;
    private String email;
    private String gender;
    private String status;


    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

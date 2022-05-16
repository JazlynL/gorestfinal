package com.careerdevs.gorestfinal.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class ToDos {
    /*"id": 1373,
            "user_id": 2703,
            "title": "Delicate tyrannus itaque tremo ut vorax vicinus uter.",
            "due_on": "2022-05-25T00:00:00.000+05:30",
            "status": "completed"*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @JsonProperty("user_id")
    private long user_id;

    private String title;

    @JsonProperty("due_on")
    private String due_on;

    private String status;

    public long getId() {
        return id;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDue_on() {
        return due_on;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ToDos{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", title='" + title + '\'' +
                ", due_on=" + due_on +
                ", status='" + status + '\'' +
                '}';
    }
}

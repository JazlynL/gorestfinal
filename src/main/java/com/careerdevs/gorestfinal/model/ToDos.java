package com.careerdevs.gorestfinal.model;


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
    private long userId;
    private String title;
    private Date dueOn;
    private String status;

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public Date getDueOn() {
        return dueOn;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ToDos{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

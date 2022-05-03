package com.careerdevs.gorestfinal.model;


import javax.persistence.*;

/*
*
*  {
    "id": 1362,
    "post_id": 1365,
    "name": "Rukmin Dutta",
    "email": "dutta_rukmin@champlin-treutel.net",
    "body": "Facilis deleniti omnis. Quis atque magni. Nesciunt ipsam ut."
  },
  * */


// This entity is used for our repository to recognize that this class can be used as a table
@Entity
public class Comment {


    //The @Id annotation is inherited from javax.persistence.Id，
    // indicating the member field below is the primary key of current entity.
    @Id

    //Provides for the specification of generation strategies for the values of primary keys. The GeneratedValue annotation may
    // be applied to a primary key property or field of an entity or mapped superclass in conjunction with the Id annotation
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long post_id;
    private String email;

    @Column(length = 512)
    private String  body;


    public long getId() {
        return id;
    }

    public long getPost_id() {
        return post_id;
    }

    public String getEmail() {
        return email;
    }

    public String getBody() {
        return body;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", post_id=" + post_id +
                ", email='" + email + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}

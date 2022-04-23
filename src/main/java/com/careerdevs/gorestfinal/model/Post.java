package com.careerdevs.gorestfinal.model;

import javax.persistence.*;
import java.rmi.server.UID;


@Entity
public class Post {
    /*
       id": 1939,
    "user_id": 4097,
    "title": "Defungo utrum solvo talis crastinus delibero et dignissimos blandior cras vix.",
    "body": "Sono videlicet qui. Clam provident thema. Celer ocer adicio. Dolor utor astrum. Sapiente admoneo conscendo. Debilito totidem advenio. Benevolentia tui virga. Animi acer videlicet. Volubilis deludo decet. Venustas adamo ante. Et deduco curia. Taedium rem vulgo. Triginta agnitio tergeo. Complectus capto conduco. Tui thesaurus depopulo. Clam vapulus tui. Somnus thymum barba. Defigo aegrus desolo. Verecundia conitor bos. Peccatus aurum earum."
  },
      */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private long id;
    private long user_Id;
    private String title;
    //
    @Column(length = 512)
    private String body;

    public long getId() {
        return id;
    }

    public long getUser_Id() {
        return user_Id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String
    toString() {
        return "Post{" +
                "id=" + id +
                ", user_Id=" + user_Id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}

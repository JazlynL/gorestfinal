package com.careerdevs.gorestfinal.repository;

import com.careerdevs.gorestfinal.model.Comment;
import org.springframework.data.repository.CrudRepository;



public interface CommentRepository extends CrudRepository<Comment, Long> {

}


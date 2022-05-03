package com.careerdevs.gorestfinal.repository;

import com.careerdevs.gorestfinal.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PostRepository extends CrudRepository<Post, Long> {

}

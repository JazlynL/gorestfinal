package com.careerdevs.gorestfinal.repository;

import com.careerdevs.gorestfinal.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
}

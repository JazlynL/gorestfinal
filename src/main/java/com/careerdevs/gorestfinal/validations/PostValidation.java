package com.careerdevs.gorestfinal.validations;

import com.careerdevs.gorestfinal.model.Post;
import com.careerdevs.gorestfinal.repository.PostRepository;

public class PostValidation {

    public static ValidationError validatePost(Post post, PostRepository postRepo, boolean isUpdating){

        ValidationError errors = new ValidationError();

        return errors;

    }
}

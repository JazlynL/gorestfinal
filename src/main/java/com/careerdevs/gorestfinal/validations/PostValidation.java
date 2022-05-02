package com.careerdevs.gorestfinal.validations;

import com.careerdevs.gorestfinal.model.Post;
import com.careerdevs.gorestfinal.repository.PostRepository;

import java.util.Optional;

public class PostValidation {

    public static ValidationError validatePost(Post post, PostRepository postRepo, boolean isUpdating){

        ValidationError errors = new ValidationError();
 // if the boolean is true
        if (isUpdating) {
            //if the id is equal to 0
            if (post.getId() == 0) {

                // Add error message to the error var.
                errors.addError("Id", "there is no valid id ");
            } else {
                /*A container object which may or may not contain a non-null value.
                If a value is present, isPresent() will return true and get() will return the value.
                Additional methods that depend on the presence or absence of a contained value are provided,
                such as orElse() (return a default value if value not present) and ifPresent() (execute a block of code if the value is present).
                This is a value-based class; use of identity-sensitive operations (including reference equality (==), identity hash code,
                or synchronization) on instances of Optional may have unpredictable results and should be avoided.*/
                Optional<Post> foundUser = postRepo.findById(post.getId());


            // is Empty is a static modifier of the Optional class, Empty returns an of an empty Optional instance.

            if (foundUser.isEmpty()){
                errors.addError("id","No User found with the ID " + post.getId());
              }

            }
        }

       // fields we are setting for
        String postTitle = post.getTitle();
        String postBody = post.getBody();
        Long postId = post.getId();



        // throwing specific errors within these conditionals
        if (postTitle == null || postTitle.trim().equals("")) {
            errors.addError("title", "Title can not be left blank");
        }

        if (postBody == null || postBody.trim().equals("")) {
            errors.addError("body", "Body can not be left blank");
        }

        if (postId == 0) {
            errors.addError("user_id", "User_ID can not be left blank");
        } else {
            // is the postUserId connected to an existing user.
            Optional<Post> foundUser = postRepo.findById(postId);

            if (foundUser.isEmpty()) {
                errors.addError("user_id", "User_ID is invalid because there is no user found with the id: " + postId);
            }
        }
        return errors;


    }
}

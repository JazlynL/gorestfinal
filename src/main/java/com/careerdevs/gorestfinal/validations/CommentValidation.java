package com.careerdevs.gorestfinal.validations;

import com.careerdevs.gorestfinal.model.Comment;
import com.careerdevs.gorestfinal.model.Post;
import com.careerdevs.gorestfinal.repository.CommentRepository;

import java.util.Optional;

public class CommentValidation {


    public static ValidationError commentValidation(Comment comment, CommentRepository commentRepository, boolean isUpdating ){

        ValidationError errors =  new ValidationError();

        if(isUpdating){
            if(comment.getId() == 0){
                errors.addError("Id", "not a valid Id");

            }else{

                Optional<Comment> foundComment = commentRepository.findById(comment.getId());
            if(foundComment.isEmpty()){
                errors.addError("Id","Id is not valid and not found");
            }

            }
        }
        String  commentEmail = comment.getEmail();
        String commentBody = comment.getBody();
        Long commentId = comment.getId();



        // throwing specific errors within these conditionals
        if (commentEmail == null ||  commentEmail.trim().equals("")) {
            errors.addError("email", "Email can not be left blank");
        }

        if (commentBody == null || commentBody.trim().equals("")) {
            errors.addError("body", "Body can not be left blank");
        }

        if (commentId == 0) {
            errors.addError("user_id", "User_ID can not be left blank");
        } else {
            // is the postUserId connected to an existing user.
            Optional<Comment> foundUser = commentRepository.findById(commentId);

            if (foundUser.isEmpty()) {
                errors.addError("user_id", "User_ID is invalid because there is no user found with the id: " + commentId);
            }
        }
        return errors;

    }
}

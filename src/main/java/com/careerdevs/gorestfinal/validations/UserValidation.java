package com.careerdevs.gorestfinal.validations;

import com.careerdevs.gorestfinal.model.Post;
import com.careerdevs.gorestfinal.model.User;
import com.careerdevs.gorestfinal.repository.PostRepository;
import com.careerdevs.gorestfinal.repository.UserRepository;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

public class UserValidation {

    public static ValidationError validateUser(User user, UserRepository userRepository, boolean isUpdating){

        ValidationError errors = new ValidationError();

        if(isUpdating){

            if(user.getId() == 0){
                errors.addError("Id","There is no Valid Id");
            }else{


                Optional<User> foundUser = userRepository.findById( user.getId());



            if(foundUser.isEmpty()){
                errors.addError("Id"," Id is not found or valid");
            }

            }
        }

        int userId = user.getId();
        String userEmail = user.getEmail();
        String userGender = user.getGender();


        if (userEmail == null || userEmail.trim().equals("")) {
            errors.addError("Email", "Email can not be left blank");
        }

        if (userGender == null || userGender.trim().equals("")) {
            errors.addError("Gender", "Gender can not be left blank");
        }

        if (userId == 0) {
            errors.addError("user_id", "User_ID can not be left blank");
        } else {
            // is the postUserId connected to an existing user.
            Optional<User> foundUser = userRepository.findById(userId);

            if (foundUser.isEmpty()) {
                errors.addError("user_id", "User_ID is invalid because there is no user found with the id: " + postId);
            }
        }
        return errors;



    }
}

package com.careerdevs.gorestfinal.validations;


import com.careerdevs.gorestfinal.model.ToDos;
import com.careerdevs.gorestfinal.repository.ToDoRepository;
import java.util.Optional;

public class ToDosValidation {




    public static ValidationError validateToDO(ToDos toDo, ToDoRepository toDoRepository, boolean isUpdating) {
        ValidationError errors = new ValidationError();

        if(isUpdating){
            if(toDo.getId() == 0){
                errors.addError("Id","Id is empty");

            }else{


                Optional<ToDos> foundToDo = toDoRepository.findById(toDo.getUserId());


                if(foundToDo.isEmpty()){

                    errors.addError("Id","ID not found.");
                }



            }
        }

        long toDos = toDo.getUserId();
        String toDoTitle= toDo.getTitle();
        String toDoStatus = toDo.getStatus();



        // throwing specific errors within these conditionals
        if (toDoTitle == null || toDoTitle.trim().equals("")) {
            errors.addError("title", "Title can not be left blank");
        }

        if (toDoStatus == null || toDoStatus.trim().equals("")) {
            errors.addError("status", "status can not be left blank");
        }

        if (toDos == 0) {
            errors.addError("user_id", "User_ID can not be left blank");
        } else {
            // is the postUserId connected to an existing user.
            Optional<ToDos> foundUser = toDoRepository.findById(toDos);

            if (foundUser.isEmpty()) {
                errors.addError("user_id", "User_ID is invalid because there is no user found with the id: " + toDos);
            }
        }
        return errors;
    }

}

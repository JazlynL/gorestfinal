package com.careerdevs.gorestfinal.controller;


import com.careerdevs.gorestfinal.model.User;
import com.careerdevs.gorestfinal.repository.UserRepository;
import com.careerdevs.gorestfinal.utils.ApiErrorHandling;
import com.careerdevs.gorestfinal.utils.BasicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
     /*
Required Routes for GoRestSQL Final: complete for each resource; User, Post, Comment, Todo,
   ^^^* GET route that returns one [resource] by ID from the SQL database
  ^^^ * GET route that returns all [resource]s stored in the SQL database
 ^^^* DELETE route that deletes one [resource] by ID from SQL database (returns the deleted SQL [resource] data)
 ^^^* DELETE route that deletes all [resource]s from SQL database (returns how many [resource]s were deleted)
^^^ * POST route that queries one [resource] by ID from GoREST and saves their data to your local database (returns
the SQL [resource] data)
 ^^^*POST route that uploads all [resource]s from the GoREST API into the SQL database (returns how many
[resource]s were uploaded)
 ^^*POST route that create a [resource] on JUST the SQL database (returns the newly created SQL [resource] data)
 ^^*PUT route that updates a [resource] on JUST the SQL database (returns the updated SQL [resource] data)*/



    // allows us to access userRepo anytime
    @Autowired
     private UserRepository userRepository;


   @GetMapping("/{id}")
   public ResponseEntity<?> getUserId(@PathVariable("id") String id){
    try{

        if(BasicUtils.isStrNaN(id)){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,id +"Not a valid Id.");
        }

        int uId = Integer.parseInt(id);


        Optional<User> foundUser = userRepository.findById(uId);


        if(foundUser.isEmpty()){
            throw  new HttpClientErrorException(HttpStatus.NOT_FOUND, uId + "User Id not found");
        }

        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }catch(HttpClientErrorException e){
        return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
    }
    catch (Exception e){
        return ApiErrorHandling.genericApiError(e);
    }
   }


    // getting all users

    @GetMapping("/all")


    public ResponseEntity<?> getAllUsers(){
        try{

           // will return how many users we have in the database
            Iterable<User> allUsers = userRepository.findAll();

            userRepository.deleteAll();
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        } catch(HttpClientErrorException e){
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        }
        catch (Exception e){
            return ApiErrorHandling.genericApiError(e);
        }


    }



    // use path variable
    @PostMapping("/upload/{id}")
    public ResponseEntity<?> uploadUserById(@PathVariable("id") String userId, RestTemplate restTemplate){
        try{

           int uId = Integer.parseInt(userId);

            if(ApiErrorHandling.isStrNaN(userId)){
                throw  new HttpClientErrorException(HttpStatus.BAD_REQUEST, userId + "   is not a Valid Id");
            }

            String url = "https://gorest.co.in/public/v2/users/"+ uId ;
            System.out.println(url);
            User goRestUser = restTemplate.getForObject(url, User.class);

            // turn this into an if statement eventually
            if(goRestUser == null){
                throw new  HttpClientErrorException(HttpStatus.NOT_FOUND,userId +"user Id not found");
            }



            User savedUser =  userRepository.save(goRestUser);

            return  new ResponseEntity<>(savedUser,HttpStatus.CREATED);


        }catch(HttpClientErrorException e){
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        }
        catch (Exception e){
            return ApiErrorHandling.genericApiError(e);
        }
    }


}

package com.careerdevs.gorestfinal.controller;


import com.careerdevs.gorestfinal.model.Comment;
import com.careerdevs.gorestfinal.model.User;
import com.careerdevs.gorestfinal.repository.UserRepository;
import com.careerdevs.gorestfinal.utils.ApiErrorHandling;
import com.careerdevs.gorestfinal.utils.BasicUtils;
import com.careerdevs.gorestfinal.validations.UserValidation;
import com.careerdevs.gorestfinal.validations.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
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
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,id +" is not a valid Id.");
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

            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        } catch(HttpClientErrorException e){
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        }
        catch (Exception e){
            return ApiErrorHandling.genericApiError(e);
        }


    }


    @DeleteMapping("/{id}")

    public ResponseEntity<?> deleteById(@PathVariable("id") String userId){
       try{
           int uID = Integer.parseInt(userId);

           // We are using this line to be able to find the user by Id.
           //  Confirmation that it exists
           Optional<User> foundUser = userRepository.findById(uID);


           userRepository.deleteById(uID);

           return new ResponseEntity<>(foundUser, HttpStatus.OK);

       }catch(HttpClientErrorException e){
           return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
       }
       catch (Exception e){
           return ApiErrorHandling.genericApiError(e);
       }

    }


     @DeleteMapping("/deleteall")
     public ResponseEntity<?> deleteAllUsers(){
       try {
           long totalUsers = userRepository.count();
           userRepository.deleteAll();

           return new ResponseEntity<>("Total users deleted: "+totalUsers, HttpStatus.OK);
       }catch(HttpClientErrorException e){
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

            if(savedUser == null){
                throw  new HttpClientErrorException(HttpStatus.NOT_FOUND,"This user is not created");
            }

            return  new ResponseEntity<>(savedUser,HttpStatus.CREATED);


        }catch(HttpClientErrorException e){
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        }
        catch (Exception e){
            return ApiErrorHandling.genericApiError(e);
        }
    }


    @PostMapping("/uploadall")


    public ResponseEntity <?>  uploadAllUsers(RestTemplate restTemplate){
        // initializing the post to the Url variable
        String url = "https://gorest.co.in/public/v2/comments";

        //response,  we are using the getForEntity()
        // method of the RestTemplate class to invoke the API and get the response as a JSON string
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);

        //The getBody() method returns an InputStream from which the response body can be accessed.
        User firstPage = response.getBody();

        //if its null it  will throw an exception error
        if (firstPage == null) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET first page of " +
                    "posts from GoREST");
        }


        // Creating an Array List and storing the first Page.
        ArrayList<User> allUsers = new ArrayList<>(Arrays.asList(firstPage));

        //using the response variable , and getting the HTTP Headers
        HttpHeaders responseHeaders = response.getHeaders();


        //Creating a STRING variable and initializing it to the Objects class we are e
        String totalPages = Objects.requireNonNull(responseHeaders.get("X-Pagination-Pages")).get(0);

        // using parse to take the total page num.
        int totalPgNum = Integer.parseInt(totalPages);


        // to iterate through the pages.we set it at 2 to get the following page.
        for (int i = 2; i <= totalPgNum; i++) {
            // grabbing the url and making a query of it
            String pageUrl = url + "?page=" + i;

            //
            User pageUsers = restTemplate.getForObject(pageUrl, User.class);

            // setting the conditional for the exception thrown. if page of users is null
            if (pageUsers == null) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET page " + i  +
                        " of users from GoREST");
            }


            allUsers.addAll(Arrays.asList(firstPage));
        }
        //upload all users to SQL
        userRepository.saveAll(allUsers);


        return  new ResponseEntity <> ("Succesully uploaded all users"+ allUsers.size(),HttpStatus.CREATED);
    }



    @PostMapping("/")
    public ResponseEntity<?>  createUser(@RequestBody User newUser){
       try{


           ValidationError errors = UserValidation.validateUser(newUser,userRepository,false);

           if(errors.hasError()){

               throw  new HttpClientErrorException(HttpStatus.BAD_REQUEST,"This has errors in the request");
           }

           User createdNewUser = userRepository.save(newUser);

           return new ResponseEntity<>(createdNewUser,HttpStatus.CREATED);
       }catch(HttpClientErrorException e){
           return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
       }
       catch (Exception e){
           return ApiErrorHandling.genericApiError(e);
       }
    }



    @PutMapping

    public ResponseEntity<?> updateUser(@RequestBody User updateUser){
       try{
           ValidationError errors = UserValidation.validateUser(updateUser, userRepository, true);

           if(errors.hasError()){
               throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, " You are not able to update this user");
           }

           User updatedUser = userRepository.save(updateUser);

           return new ResponseEntity<>(updatedUser,HttpStatus.CREATED);

       }catch(HttpClientErrorException e){
           return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
       }
       catch (Exception e){
           return ApiErrorHandling.genericApiError(e);
       }
    }


}

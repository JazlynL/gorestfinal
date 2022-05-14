package com.careerdevs.gorestfinal.controller;

import com.careerdevs.gorestfinal.model.Comment;
import com.careerdevs.gorestfinal.model.Post;
import com.careerdevs.gorestfinal.model.ToDos;
import com.careerdevs.gorestfinal.repository.ToDoRepository;
import com.careerdevs.gorestfinal.utils.ApiErrorHandling;
import com.careerdevs.gorestfinal.utils.BasicUtils;
import com.careerdevs.gorestfinal.validations.ToDosValidation;
import com.careerdevs.gorestfinal.validations.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")

public class ToDoController {


    @Autowired
    private ToDoRepository toDoRepository;
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


@GetMapping("/{id}")
public ResponseEntity<?> getToDobyId(@PathVariable("id") String id){
    try{

        long uId = Long.parseLong(id);

        if(ApiErrorHandling.isStrNaN(id)){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, uId + " This id is not ");
        }

        Optional<ToDos> foundToDo =   toDoRepository.findById(uId);

        if(foundToDo.isEmpty()){
            throw  new HttpClientErrorException(HttpStatus.NOT_FOUND, uId + " This Id empty");
        }

        return  new ResponseEntity<>(foundToDo,HttpStatus.OK);




    }catch(HttpClientErrorException e){


        return  ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());

    }catch(Exception e){
        return ApiErrorHandling.genericApiError(e);

    }
}

@GetMapping("/all")
public ResponseEntity<?> getAllToDos(){
    try{
        Iterable<ToDos> toDosFound = toDoRepository.findAll();

        return  new ResponseEntity<>(toDosFound, HttpStatus.OK);
    }catch(HttpClientErrorException e){


        return  ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());

    }catch(Exception e){
        return ApiErrorHandling.genericApiError(e);

    }


}


@DeleteMapping("/{id}")


    public  ResponseEntity<?> getToDosById(@PathVariable("id") String id){
    try{
        long toDoId = Long.parseLong(id);

        if(BasicUtils.isStrNaN(id)){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, id + " Id is not a string");
        }

        Optional<ToDos> foundTo = toDoRepository.findById(toDoId);

        toDoRepository.deleteById(toDoId);


        return new ResponseEntity<>(foundTo,HttpStatus.OK);
    }
    catch(HttpClientErrorException e){


        return  ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());

    }catch(Exception e){
        return ApiErrorHandling.genericApiError(e);

    }
}

@DeleteMapping("/all")
    public ResponseEntity<?> deleteAllById(){
    try{
         long deleteAll = toDoRepository.count();

         toDoRepository.deleteAll();

         return new ResponseEntity<>(deleteAll,HttpStatus.OK);

    }catch(HttpClientErrorException e){


        return  ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());

    }catch(Exception e){
        return ApiErrorHandling.genericApiError(e);

    }
}

@PostMapping("/{id}")
    public  ResponseEntity<?> toDoById(@PathVariable ("id") String id, RestTemplate restTemplate){
    try{



        long toDoId = Long.parseLong(id);

        String url = "https://gorest.co.in/public/v2/todos/"+toDoId;

        if(BasicUtils.isStrNaN(id)){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, id + " no id is found");
        }
        ToDos foundToDo = restTemplate.getForObject(url,ToDos.class);

        if(foundToDo == null){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND,"No user at this ID");
        }

        ToDos savedId = toDoRepository.save(foundToDo);

        return new ResponseEntity<>(savedId,HttpStatus.CREATED);

    }catch(HttpClientErrorException e){


        return  ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());

    }catch(Exception e){
        return ApiErrorHandling.genericApiError(e);

    }

}

@PostMapping("/uploadall")
    public  ResponseEntity<?> uploadAllToDos(RestTemplate restTemplate){
    try{
        // initializing the post to the Url variable
        String url = "https://gorest.co.in/public/v2/todos";

        //response
        ResponseEntity<ToDos[]> response = restTemplate.getForEntity(url, ToDos[].class);

        //The getBody() method returns an InputStream from which the response body can be accessed.
        ToDos[] firstPage = response.getBody();

        //if its null it  will throw an exception error
        if (firstPage == null) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET first page of " +
                    "posts from GoREST");
        }


        // Creating an Array List and storing the first Page.
        ArrayList<ToDos> allUsers = new ArrayList<>(Arrays.asList(firstPage));

        //using the response variable , and getting the HTTP Headers
        HttpHeaders responseHeaders = response.getHeaders();


        //Creating a STRING variable and initializing it to the Objects class we are e
        String totalPages = Objects.requireNonNull(responseHeaders.get("X-Pagination-Pages")).get(0);

        // using parse to take the total page num.
        int totalPgNum = Integer.parseInt(totalPages);


        // to iterate through the pages.
        for (int i = 2; i <= totalPgNum; i++) {

            // grabbing the url and making a query of it
            String pageUrl = url + "?page=" + i;

            //
            ToDos[] pageUsers = restTemplate.getForObject(pageUrl, ToDos[].class);

            // setting the conditional for the exception thrown.
            if (pageUsers == null) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET page " + i  +
                        " of users from GoREST");
            }
            allUsers.addAll(Arrays.asList(firstPage));
        }
        //upload all users to SQL
        toDoRepository.saveAll(allUsers);


        return new ResponseEntity<>("Users Created: " + allUsers.size(), HttpStatus.OK);
    }catch(HttpClientErrorException e){


        return  ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());

    }catch(Exception e){
        return ApiErrorHandling.genericApiError(e);

    }
}

@PostMapping("/")

    public ResponseEntity<?>  toDosUpload(@RequestBody ToDos toDosUser){
    try{
        ValidationError errors = ToDosValidation.validateToDO(toDosUser, toDoRepository,false);

        if(errors.hasError()){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, errors.toJSONobject());
        }

        ToDos createdToDo = toDoRepository.save(toDosUser);


        return  new ResponseEntity<>(createdToDo,HttpStatus.CREATED);


    }catch(HttpClientErrorException e){


        return  ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());

    }catch(Exception e){
        return ApiErrorHandling.genericApiError(e);

    }

}


@PutMapping

    public ResponseEntity<?> updateToDos(@RequestBody ToDos updateToDo){
    try{

        ValidationError errors = ToDosValidation.validateToDO(updateToDo,toDoRepository, true);
        if(errors.hasError()){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Id couldn't be updated.");
        }

        ToDos updatedToDo = toDoRepository.save(updateToDo);

        return  new ResponseEntity<>(updatedToDo,HttpStatus.CREATED);

    }catch(HttpClientErrorException e){


        return  ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());

    }catch(Exception e){
        return ApiErrorHandling.genericApiError(e);

    }
}
}

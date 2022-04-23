package com.careerdevs.gorestfinal.controller;

import com.careerdevs.gorestfinal.model.Post;
import com.careerdevs.gorestfinal.repository.PostRepository;
import com.careerdevs.gorestfinal.utils.ApiErrorHandling;
import com.careerdevs.gorestfinal.validations.PostValidation;
import com.careerdevs.gorestfinal.validations.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api/posts")

public class PostController {

  //  @GetMapping("/test")
    // public


    @Autowired
    PostRepository postRepository;



    // getting all resources stored in the mySQL database.
    @GetMapping("/all")
    public ResponseEntity<?> getAllPosts(){
        try{
            Iterable<Post> allPosts = postRepository.findAll();

            return new ResponseEntity<>(allPosts, HttpStatus.OK);
        }
        catch(Exception e){
            return ApiErrorHandling.genericApiError(e);

        }
    }


    // POST resource that creates a [resource] on just the sql Database( returns the newly created SQL resource Data).

        @PostMapping("/")
                public ResponseEntity<?> createPost ( @RequestBody Post newPost){
             try {


                 ValidationError errors = PostValidation.validatePost(newPost,postRepository, false);

                 if(errors.hasError() ){
                     throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, errors.toJSONobject());
                 }
                 Post createNew = postRepository.save(newPost);

                 return new ResponseEntity<>(createNew, HttpStatus.CREATED);
             }catch(HttpClientErrorException e){
                 return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());

             }
             catch(Exception e){
                 return ApiErrorHandling.genericApiError(e);

             }


        }
        // put mapping

    }


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
     // getting all pages with a post request

  /*  @PostMapping ("/uploadall")
    public ResponseEntity<?> uploadAll (
            RestTemplate restTemplate
    ) {
        try {
            String url = "https://gorest.co.in/public/v2/users";
            ResponseEntity<User[]> response = restTemplate.getForEntity(url, User[].class);
            User[] firstPageUsers = response.getBody();
            if (firstPageUsers == null) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET first page of " +
                        "users from GoREST");
            }
            ArrayList<User> allUsers = new ArrayList<>(Arrays.asList(firstPageUsers));
            HttpHeaders responseHeaders = response.getHeaders();
            String totalPages = Objects.requireNonNull(responseHeaders.get("X-Pagination-Pages")).get(0);
            int totalPgNum = Integer.parseInt(totalPages);
            for (int i = 2; i <= totalPgNum; i++) {
                String pageUrl = url + "?page=" + i;
                User[] pageUsers = restTemplate.getForObject(pageUrl, User[].class);
                if (pageUsers == null) {
                    throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET page " + i  +
                            " of users from GoREST");
                }
                allUsers.addAll(Arrays.asList(firstPageUsers));
            }
            //upload all users to SQL
            userRepository.saveAll(allUsers);
            return new ResponseEntity<>("Users Created: " + allUsers.size(), HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }
*/




        // put mapping

    @PutMapping

    public ResponseEntity<?>  updateUser(@RequestBody Post updateUser){
        try{

            // instead of using false we will be using true to update the user info in our database.
            ValidationError errors = PostValidation.validatePost(updateUser ,postRepository,true);

            // using this method to see if the errors were checked.

            if(errors.hasError()){
                throw  new HttpClientErrorException(HttpStatus.BAD_REQUEST, errors.toJSONobject());
            }

            Post updateNew = postRepository.save(updateUser);


            return  new ResponseEntity<>(updateNew,HttpStatus.CREATED);

        }catch(HttpClientErrorException e){
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());

        }
        catch(Exception e){
            return ApiErrorHandling.genericApiError(e);

        }

    }



    }


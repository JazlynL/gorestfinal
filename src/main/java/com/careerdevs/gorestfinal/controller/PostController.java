package com.careerdevs.gorestfinal.controller;

import com.careerdevs.gorestfinal.model.Post;
import com.careerdevs.gorestfinal.model.User;
import com.careerdevs.gorestfinal.repository.PostRepository;
import com.careerdevs.gorestfinal.repository.UserRepository;
import com.careerdevs.gorestfinal.utils.ApiErrorHandling;
import com.careerdevs.gorestfinal.utils.BasicUtils;
import com.careerdevs.gorestfinal.validations.PostValidation;
import com.careerdevs.gorestfinal.validations.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/posts")

public class PostController {

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

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    String postUrl = "https://gorest.co.in/public/v2/posts/";


    @GetMapping("/{id}")

    public ResponseEntity<?> getUserById(@PathVariable("id") String id ){
        try{

            if(ApiErrorHandling.isStrNaN(id)){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, id + "is not a valid ID");

            }

            // parsing the string data and storing it as an int.
            long uId = Long.parseLong(id);



            Optional<Post> foundUser = postRepository.findById( uId);

            // a method to check if any users are actually empty.
            if(foundUser.isEmpty()){
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No user with this ID is found"+ id);
            }


            return new ResponseEntity<>(foundUser,HttpStatus.OK);

        }  catch(HttpClientErrorException e){
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        }catch(Exception e){
            return ApiErrorHandling.genericApiError(e);



        }


    }

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




     @DeleteMapping("/{id}")

     public ResponseEntity<?> deleteById(@PathVariable("id") String id){
        try{
            Long uId = Long.parseLong(id);

            if(BasicUtils.isStrNaN(id)){
                throw  new HttpClientErrorException(HttpStatus.BAD_REQUEST, id + "Not a valid id inputed");
            }

            Optional  <Post> foundUser = postRepository.findById(uId);

            if(foundUser.isEmpty()){
                throw  new HttpClientErrorException(HttpStatus.NOT_FOUND, id +" Not found in ID");
            }

            postRepository.deleteById(uId);

            return new ResponseEntity<>( foundUser, HttpStatus.OK);

        } catch(HttpClientErrorException e){
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        }catch(Exception e){
            return ApiErrorHandling.genericApiError(e);



        }
     }


     @DeleteMapping("/deleteall")

     public ResponseEntity<?> deleteAllPosts(){

       try {
           long totalAmountPost = postRepository.count();


           postRepository.deleteAll();

           return new ResponseEntity<>(totalAmountPost, HttpStatus.OK);

       } catch(HttpClientErrorException e){
           return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());

       }
       catch(Exception e){
           return ApiErrorHandling.genericApiError(e);

       }


     }

    // POST resource that creates a [resource] on just the sql Database( returns the newly created SQL resource Data).

        @PostMapping("/")
                public ResponseEntity<?> createPost ( @RequestBody Post newPost){
             try {


                 ValidationError errors = PostValidation.validatePost(newPost,postRepository,userRepository, false);

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


        @PostMapping("/upload/{id}")

        public ResponseEntity<?> uploadById(@PathVariable("id") String id,
                                            RestTemplate restTemplate){
        try{
            // parsing the user ID
            int uId = Integer.parseInt(id);

            String url = postUrl + uId;


            // we are using the BasicUtils to determine whether or not its a string
            if(BasicUtils.isStrNaN(id)){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, id);

            }



            // using the getForObject remember we use the url and thr class for the parameters.
            Post foundedPost = restTemplate.getForObject(url,Post.class);

            // checking if its not Null

            if(foundedPost == null){
                throw  new HttpClientErrorException(HttpStatus.NOT_FOUND, id + "User id not found" );
            }

          /*  Iterable<User> allPosts = userRepository.findAll();
            List<User> result = new ArrayList<User>();
            allPosts.forEach(result::add);
            long randomId = result.get((int) (result.size()* Math.random())).getId();
            foundedPost.setUser_id(randomId);*/

            Post savedPost =  postRepository.save(foundedPost);
            return  new ResponseEntity<>(savedPost,HttpStatus.CREATED);



        }catch(HttpClientErrorException e){
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        }catch(Exception e){
            return  ApiErrorHandling.genericApiError(e);
        }

        }


     // getting all pages with a post request

    @PostMapping ("/uploadall")
    public ResponseEntity<?> uploadAll (
            RestTemplate restTemplate
    ) {
        try {
            // initializing the post to the Url variable
            String url = postUrl;

            //response
            ResponseEntity<Post[]> response = restTemplate.getForEntity(url, Post[].class);

            //The getBody() method returns an InputStream from which the response body can be accessed.
            Post[] firstPage = response.getBody();

            //if its null it  will throw an exception error
            if (firstPage == null) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET first page of " +
                        postUrl + "posts from GoREST");
            }


            // Creating an Array List and storing the first Page.
            ArrayList<Post> allUsers = new ArrayList<>(Arrays.asList(firstPage));

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
                Post[] pageUsers = restTemplate.getForObject(pageUrl, Post[].class);

                // setting the conditional for the exception thrown.
                if (pageUsers == null) {
                    throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET page " + i  +
                            " of users from GoREST");
                }
                allUsers.addAll(Arrays.asList(firstPage));
            }
            //upload all users to SQL
            postRepository.saveAll(allUsers);


            return new ResponseEntity<>("Users Created: " + allUsers.size(), HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }





        // put mapping

    @PutMapping

    public ResponseEntity<?>  updateUser(@RequestBody Post updateUser){
        try{

            // instead of using false we will be using true to update the user info in our database.
            ValidationError errors = PostValidation.validatePost(updateUser ,postRepository,userRepository,true);

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


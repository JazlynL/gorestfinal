package com.careerdevs.gorestfinal.controller;


import com.careerdevs.gorestfinal.model.Comment;
import com.careerdevs.gorestfinal.model.Post;
import com.careerdevs.gorestfinal.model.Post;
import com.careerdevs.gorestfinal.model.User;
import com.careerdevs.gorestfinal.repository.CommentRepository;
import com.careerdevs.gorestfinal.repository.PostRepository;
import com.careerdevs.gorestfinal.repository.UserRepository;
import com.careerdevs.gorestfinal.utils.ApiErrorHandling;
import com.careerdevs.gorestfinal.utils.BasicUtils;
import com.careerdevs.gorestfinal.validations.CommentValidation;
import com.careerdevs.gorestfinal.validations.UserValidation;
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
@RequestMapping("/api/comments")
public class CommentController {
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
    private CommentRepository commentRepository;


    @Autowired
    private PostRepository postRepository;


     @GetMapping("/{id}")
    public ResponseEntity<?> getCommentByID (@PathVariable("id") String id){
        try{
            if(BasicUtils.isStrNaN(id)){
                throw  new HttpClientErrorException(HttpStatus.BAD_REQUEST, id + "Id is a string.");

            }


            long commentId = Long.parseLong(id);


            Optional<Comment> foundId = commentRepository.findById(commentId);
            if(foundId.isEmpty()){
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND,id + "This is an empty id");
            }



            return new ResponseEntity<>(foundId, HttpStatus.OK);





        }catch(HttpClientErrorException e){
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        }
        catch (Exception e){
            return ApiErrorHandling.genericApiError(e);
        }

    }
    @GetMapping("/all")

    public ResponseEntity<?> getAllComment(){
         try{
             Iterable<Comment> grabComments = commentRepository.findAll();

             return new ResponseEntity<>(grabComments, HttpStatus.OK);

         }catch(HttpClientErrorException e){
             return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
         }
         catch (Exception e){
             return ApiErrorHandling.genericApiError(e);
         }

    }


    @DeleteMapping("/{id}")

    public ResponseEntity<?> deleteById(@PathVariable ("id") String id){

         try{


             long commentId = Long.parseLong(id);


             if(BasicUtils.isStrNaN(id)){
                 throw  new HttpClientErrorException(HttpStatus.BAD_REQUEST, id+ " this is not a valid id");
             }

             Optional<Comment> foundUser = commentRepository.findById(commentId);

             if(foundUser.isEmpty()){
                 throw  new HttpClientErrorException(HttpStatus.NOT_FOUND, id+ " no id was found");
             }

             commentRepository.deleteById(commentId);

             return  new ResponseEntity<>(foundUser,HttpStatus.OK);




         }catch(HttpClientErrorException e){
             return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
         }
         catch (Exception e){
             return ApiErrorHandling.genericApiError(e);
         }
    }


     @DeleteMapping("/all")
     public ResponseEntity<?> deleteAllComments(){
         try{
              long commentsFound = commentRepository.count();

             commentRepository.deleteAll();

             return new ResponseEntity<>(commentsFound,HttpStatus.OK);

         }catch(HttpClientErrorException e){
             return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
         }
         catch (Exception e){
             return ApiErrorHandling.genericApiError(e);
         }
     }

     @PostMapping("/upload/{id}")

     public ResponseEntity<?> uploadById(@PathVariable("id") String id , RestTemplate restTemplate){
         try{

             long uploadById = Long.parseLong(id);

              String url = "https://gorest.co.in/public/v2/comments/" + uploadById;

              if(BasicUtils.isStrNaN(id)){

                 throw  new HttpClientErrorException(HttpStatus.BAD_REQUEST, id + " No user id found");

             }

              Comment uploadedComments = restTemplate.getForObject(url, Comment.class);


              if(uploadedComments == null){
                  throw new HttpClientErrorException(HttpStatus.NOT_FOUND, id + "  This seems to be empty");
              }

             Iterable<Post> allPosts = postRepository.findAll();
             List<Post> result = new ArrayList<Post>();
             for(Post posts : allPosts ){
                 result.add(posts);
             }
             long randomId = result.get((int) (result.size()* Math.random())).getId();
             uploadedComments.setPost_id(randomId);


              commentRepository.save(uploadedComments);


              return new ResponseEntity<>( uploadedComments, HttpStatus.OK);


         }catch(HttpClientErrorException e){
             return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
         }
         catch (Exception e){
             return ApiErrorHandling.genericApiError(e);
         }


     }

     @PostMapping("/uploadall")

    public ResponseEntity<?> uploadAll( RestTemplate restTemplate
     ){
         try{
             // initializing the post to the Url variable
             String url = "https://gorest.co.in/public/v2/comments";

             //response,  we are using the getForEntity()
             // method of the RestTemplate class to invoke the API and get the response as a JSON string
             ResponseEntity<Comment[]> response = restTemplate.getForEntity(url, Comment[].class);

             //The getBody() method returns an InputStream from which the response body can be accessed.
             Comment[] firstPage = response.getBody();

             //if its null it  will throw an exception error
             if (firstPage == null) {
                 throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET first page of " +
                         "posts from GoREST");
             }


             // Creating an Array List and storing the first Page.
             ArrayList<Comment> allUsers = new ArrayList<>(Arrays.asList(firstPage));

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
                 Comment[] pageComments = restTemplate.getForObject(pageUrl, Comment[].class);

                 // setting the conditional for the exception thrown. if page of users is null
                 if (pageComments == null) {
                     throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET page " + i  +
                             " of users from GoREST");
                 }


                 allUsers.addAll(Arrays.asList(pageComments));
             }

             Iterable<Post> allPost = postRepository.findAll();
             List<Post> result = new ArrayList<Post>();

             for(Post post : allPost ){
                 result.add(post);
             }


             for(Post post: allPost){
                 long randomId = result.get((int) (result.size()* Math.random())).getId();
                 post.setUser_id(randomId);
             }
             //upload all users to SQL
             commentRepository.saveAll(allUsers);


             return  new ResponseEntity <> ("Successfully uploaded all users"+ allUsers.size(),HttpStatus.CREATED);

         }catch(HttpClientErrorException e){
             return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
         }
         catch (Exception e){
             return ApiErrorHandling.genericApiError(e);
         }
     }


     @PostMapping("/")


    public  ResponseEntity<?> uploadComment(@RequestBody Comment comment){

         try{
             ValidationError errors = CommentValidation.commentValidation(comment,commentRepository, postRepository, false);

             if(errors.hasError()){
                 throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, errors.toJSONobject());
             }

             Comment createdComment = commentRepository.save(comment);
             return new ResponseEntity<>( createdComment, HttpStatus.OK);

         }catch(HttpClientErrorException e){
             return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
         }
         catch (Exception e){
             return ApiErrorHandling.genericApiError(e);
         }
     }


     @PutMapping

    public ResponseEntity<?> updateComment(@RequestBody Comment updateComment){
         try{
             ValidationError errors = CommentValidation.commentValidation(updateComment,commentRepository,postRepository, true);
             if(errors.hasError()){
                 throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, errors.toJSONobject());
             }

             Comment updatedComment = commentRepository.save(updateComment);

             return new ResponseEntity<>(updatedComment, HttpStatus.CREATED);


         }catch(HttpClientErrorException e){
             return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
         }
         catch (Exception e){
             return ApiErrorHandling.genericApiError(e);
         }
     }

}

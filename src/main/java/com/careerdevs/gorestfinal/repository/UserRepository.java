package com.careerdevs.gorestfinal.repository;


import com.careerdevs.gorestfinal.model.User;
import org.springframework.data.repository.CrudRepository;



public interface UserRepository extends CrudRepository<User, Integer> {

}


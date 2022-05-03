package com.careerdevs.gorestfinal.repository;


import com.careerdevs.gorestfinal.model.ToDos;
import org.springframework.data.repository.CrudRepository;


public interface ToDoRepository extends CrudRepository<ToDos, Long> {

    }


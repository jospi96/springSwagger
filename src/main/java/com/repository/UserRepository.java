package com.repository;


import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.model.User;



@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Qui possiamo aggiungere metodi personalizzati se necessario
	Optional<User> findByUsername(String username);
}
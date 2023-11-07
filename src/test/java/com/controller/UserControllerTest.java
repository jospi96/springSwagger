package com.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.model.User;
import com.service.UserService;


@RunWith(MockitoJUnitRunner.class)
class UserControllerTest {
	
	 @Mock
	    private UserService userService; // Crea un mock del servizio UserService

	    @InjectMocks
	    private UserController userController; // Crea un'istanza del UserController con le dipendenze mock iniettate

	    private User user; // Variabile per l'oggetto User utilizzato nei test

	    @BeforeEach
	    void setUp() {
	        user = new User(); // Inizializza un nuovo User
	        user.setId("1");
	        user.setUsername("testUser");
	        user.setPassword("testPassword");
	    }

	    @Test
	    void whenGetAllUsers_thenReturnListOfUsers() {
	        List<User> users = Arrays.asList(user); 					// Crea una lista di utenti per il test
	        when(userService.findAllUsers()).thenReturn(users); 		// Configura il mock per restituire la lista di utenti

	        ResponseEntity<?> response = userController.getAllUsers(); 	// Chiama il metodo getAllUsers

	        assertEquals(ResponseEntity.ok(users), response); 			// Verifica che la risposta contenga la lista di utenti
	        verify(userService).findAllUsers(); 						// Verifica che il metodo findAllUsers sia stato chiamato
	    }

	    @Test
	    void whenGetUser_withValidId_thenReturnUser() {
	        when(userService.findUserById("1")).thenReturn(user); 		// Configura il mock per restituire l'utente quando cercato per id

	        ResponseEntity<?> response = userController.getUser("1"); 	// Chiama il metodo getUser con un id valido

	        assertEquals(ResponseEntity.ok(user), response); 			// Verifica che la risposta contenga l'utente
	        verify(userService).findUserById("1"); 						// Verifica che il metodo findUserById sia stato chiamato con l'id corretto
	    }

	    @Test
	    void whenGetUser_withInvalidId_thenReturnNotFound() {
	        when(userService.findUserById("2")).thenReturn(null); 		// Configura il mock per restituire null se l'utente non viene trovato

	        ResponseEntity<?> response = userController.getUser("2"); 	// Chiama il metodo getUser con un id non valido

	        assertEquals(ResponseEntity.notFound().build(), response); 	// Verifica che la risposta sia di tipo NotFound
	        verify(userService).findUserById("2"); 						// Verifica che il metodo findUserById sia stato chiamato con un id non esistente
	    }

	    @Test
	    void whenCreateUser_thenReturnSavedUser() {
	        when(userService.saveUser(any(User.class))).thenReturn(user); 	// Configura il mock per restituire l'utente salvato

	        ResponseEntity<?> response = userController.createUser(user); 	// Chiama il metodo createUser

	        assertEquals(ResponseEntity.ok(user), response); 				// Verifica che la risposta contenga l'utente salvato
	        verify(userService).saveUser(any(User.class)); 					// Verifica che il metodo saveUser sia stato chiamato con un oggetto User
	    }
}

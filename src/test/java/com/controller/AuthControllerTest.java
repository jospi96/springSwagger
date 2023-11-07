package com.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.config.JwtUtil;
import com.dto.LoginDTO;
import com.model.User;
import com.service.UserService;


@RunWith(MockitoJUnitRunner.class)
class AuthControllerTest {

	@Mock
    private UserService userService; // Mock del servizio UserService

    @Mock
    private JwtUtil jwtUtil; // Mock della classe di utilità per JWT

    @InjectMocks
    private AuthController authController; // La classe di test AuthController con le dipendenze mock iniettate

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(); // Crea e inizializza un oggetto User per i test
        user.setUsername("testUser");
        user.setPassword("password");
        user.setId("1");
    }

    @Test
    void whenRegisterUser_thenReturnSuccessResponse() {
        when(userService.findUserByUsername(anyString())).thenReturn(null); 	// Nessun utente trovato con quell'username
        when(userService.saveUser(any(User.class))).thenReturn(user); 			// Salvataggio dell'utente restituisce l'oggetto User
        when(jwtUtil.generateToken(anyString())).thenReturn("token"); 			// Il metodo generateToken restituisce un token

        ResponseEntity<?> response = authController.register(user); 			// Chiama il metodo register

        assertEquals(HttpStatus.OK, response.getStatusCode()); 					// Verifica che lo stato della risposta sia OK
        assertNotNull(response.getBody()); 										// Verifica che il corpo della risposta non sia nullo

        // Cast del corpo della risposta per verificare il contenuto
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(user.getUsername(), responseBody.get("user")); 			// Verifica che l'username nell'oggetto di risposta sia corretto
        assertEquals("token", responseBody.get("token")); 						// Verifica che il token nell'oggetto di risposta sia corretto
    }

    @Test
    void whenRegisterUserWithExistingUsername_thenReturnBadRequest() {
        when(userService.findUserByUsername(anyString())).thenReturn(user); 	// L'username fornito esiste già

        ResponseEntity<?> response = authController.register(user); 			// Chiama il metodo register

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 		// Verifica che lo stato della risposta sia BAD_REQUEST
        assertEquals("Unable to register user", response.getBody()); 			// Verifica che il corpo della risposta contenga il messaggio di errore
    }

    @Test
    void whenLoginWithValidCredentials_thenReturnSuccessResponse() {
        when(userService.findUserByUsername(anyString())).thenReturn(user); 	// L'utente esiste
        when(jwtUtil.generateToken(anyString())).thenReturn("token"); 			// Il metodo generateToken restituisce un token

        // Crea un oggetto LoginDTO con le credenziali valide
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("password");

        ResponseEntity<?> response = authController.login(loginDTO); 			// Chiama il metodo login

        assertEquals(HttpStatus.OK, response.getStatusCode()); 					// Verifica che lo stato della risposta sia OK
        assertNotNull(response.getBody()); 										// Verifica che il corpo della risposta non sia nullo

        // Cast del corpo della risposta per verificare il contenuto
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("token", responseBody.get("token")); 						// Verifica che il token nell'oggetto di risposta sia corretto
        assertEquals(user.getUsername(), responseBody.get("user")); 			// Verifica che l'username nell'oggetto di risposta sia corretto
    }

    @Test
    void whenLoginWithInvalidCredentials_thenReturnUnauthorizedResponse() {
        when(userService.findUserByUsername(anyString())).thenReturn(user); 	// L'utente esiste

        // Crea un oggetto LoginDTO con credenziali non valide
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("wrongPassword");

        ResponseEntity<?> response = authController.login(loginDTO); 			// Chiama il metodo login

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode()); 		// Verifica che lo stato della risposta sia UNAUTHORIZED
        assertEquals("Invalid credentials", response.getBody()); 				// Verifica che il corpo della risposta contenga il messaggio di errore
    }
}

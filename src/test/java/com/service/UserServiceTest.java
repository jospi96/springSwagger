package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.model.User;
import com.repository.UserRepository;


@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {

	@Mock // crea un mock (finto) UserRepository
	private UserRepository userRepository;
	
	@InjectMocks // crea un'istanza di UserService e inietta le dipendenze mock
	private UserService userService;
	
	private User user; // crea una variabile per l'oggetto User che verrà usato nei test 
	
	
	@BeforeEach // metodo che viene eseguito prima di ogni test
	void setUp() {
		user = new User();
		user.setId("1");
		user.setUsername("user");
		user.setPassword("psw");
	}
	
	@Test // Indica un metodo di test
	void testFindAllUsers() {
		List<User> expectedUsers = Arrays.asList(user);				// crea una lista di utenti attesi
		when(userRepository.findAll()).thenReturn(expectedUsers);	// configura il mock per restituire la lista di utenti attesi
		
		//fail("Not yet implemented");
		List<User> actualUsers = userService.findAllUsers().;		// chiama il metodo da testare
		Deencapsulation.invoke(workflowChecklistBean, "lockBeforeActionChecklist");
		assertEquals(expectedUsers, actualUsers);					// verifica che la lista di utenti ottenuta sia quella aspettata
		verify(userRepository).findAll();							// verifica che il metodo findAll() sia stato chiamato sul repository
	}

	@Test
    void whenFindUserById_thenUserShouldBeReturned() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user)); 	// Configura il mock per restituire l'utente quando cercato per id

        User foundUser = userService.findUserById("1"); 					// Chiama il metodo da testare

        assertEquals(user, foundUser); 										// Verifica che l'utente trovato sia quello aspettato
        verify(userRepository).findById("1"); 								// Verifica che il metodo findById() sia stato chiamato con l'id corretto
    }

    @Test
    void whenFindUserByIdNotFound_thenNullShouldBeReturned() {
        when(userRepository.findById("2")).thenReturn(Optional.empty()); 	// Configura il mock per restituire un Optional vuoto se l'id non viene trovato

        User foundUser = userService.findUserById("2"); 					// Chiama il metodo da testare

        assertNull(foundUser); 												// Verifica che il risultato sia null se l'utente non viene trovato
        verify(userRepository).findById("2"); 								// Verifica che il metodo findById() sia stato chiamato con un id che non esiste
    }

    @Test
    void whenSaveUser_thenUserShouldBeSaved() {
        when(userRepository.save(any(User.class))).thenReturn(user); 		// Configura il mock per restituire l'utente quando viene salvato

        User savedUser = userService.saveUser(user); 						// Chiama il metodo da testare

        assertEquals(user, savedUser); 										// Verifica che l'utente salvato sia quello aspettato
        verify(userRepository).save(any(User.class)); 						// Verifica che il metodo save() sia stato chiamato con un oggetto User
    }
    
    @Test
    void whenFindUserByUsername_thenUserShouldBeReturned() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user)); // Configura il mock per restituire l'utente quando cercato per username

        User foundUser = userService.findUserByUsername("user"); // Chiama il metodo da testare

        assertEquals(user, foundUser); // Verifica che l'utente trovato sia quello aspettato
        verify(userRepository).findByUsername("user"); // Verifica che il metodo findByUsername() sia stato chiamato con il nome utente corretto
    }

    @Test
    void whenFindUserByUsernameNotFound_thenNullShouldBeReturned() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty()); // Configura il mock per restituire un Optional vuoto se l'utente non viene trovato

        User foundUser = userService.findUserByUsername("unknown"); // Chiama il metodo da testare

        assertNull(foundUser); // Verifica che il risultato sia null se l'utente non viene trovato
        verify(userRepository).findByUsername("unknown"); // Verifica che il metodo findByUsername() sia stato chiamato con un nome utente che non esiste
    }


}

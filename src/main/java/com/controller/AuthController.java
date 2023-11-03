package com.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.config.JwtUtil;
import com.dto.LoginDTO;
import com.model.User;
import com.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private UserService userService;
	@Autowired
    private JwtUtil jwtUtil;
	
	/*@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		return ResponseEntity.ok(userService.saveUser(user));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
		User user = userService.findUserByUsername(loginDTO.getUsername());
		if (user != null && user.getPassword().equals(loginDTO.getPassword())) {
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
	}*/
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
	    // Qui assicurati che l'utente non esista già e che i dati siano validi prima di salvare
		User finduser = userService.findUserByUsername(user.getUsername());
		if (finduser != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to register user");
		}
		
	    User newUser = userService.saveUser(user);

	    // Dopo che l'utente è stato salvato, generiamo un token JWT per lui
	    if (newUser != null) {
	        String token = jwtUtil.generateToken(newUser.getUsername());

	        // Crea un oggetto Map per includere l'utente e il token nella risposta
	        Map<String, Object> responseMap = new HashMap<>();
	        responseMap.put("user", newUser.getUsername()); // Includi le informazioni che desideri ritornare
	        responseMap.put("token", token);

	        // Restituisci il token e qualsiasi altra informazione dell'utente necessaria
	        return ResponseEntity.ok(responseMap);
	    } else {
	        // Se non è possibile salvare l'utente, restituisci un errore appropriato
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to register user");
	    }
	}

	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        User user = userService.findUserByUsername(loginDTO.getUsername());
        if (user != null && user.getPassword().equals(loginDTO.getPassword())) {
            // Genera il token JWT
            String token = jwtUtil.generateToken(user.getUsername());

            // Restituisci il token nella risposta
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", token);
            tokenMap.put("user", user.getUsername());

            return ResponseEntity.ok(tokenMap);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}

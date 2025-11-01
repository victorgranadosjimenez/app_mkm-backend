package app_mkm.controller;

import app_mkm.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Permitir peticiones desde Blumhost
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (authService.login(username, password)) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Login correcto"));
        } else {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Credenciales incorrectas"));
        }
    }

    @GetMapping("/api/ping")
    public String ping() {
        return "✅ App MKM is running!";
    }
}

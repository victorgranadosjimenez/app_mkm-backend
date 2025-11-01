package app_mkm.controller;

import app_mkm.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth") // 👈 más coherente con la ruta del frontend
@CrossOrigin(origins = "*") // Permitir peticiones desde cualquier dominio (Render incluido)
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
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login correcto"
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Credenciales incorrectas"
            ));
        }
    }

    @GetMapping("/ping") // 👈 no repitas "/api"
    public String ping() {
        return "✅ App MKM is running!";
    }
}

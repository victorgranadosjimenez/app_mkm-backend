package app_mkm.controller;

import app_mkm.entity.Alert;
import app_mkm.scheduler.AlertChecker;
import app_mkm.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "*")
public class AlertController {

    private final AlertService alertService;
    private final AlertChecker alertChecker;

    public AlertController(AlertService alertService, AlertChecker alertChecker) {
        this.alertService = alertService;
        this.alertChecker = alertChecker;
    }

    @GetMapping
    public List<Alert> getAll() {
        return alertService.getAll();
    }

    @PostMapping
    public ResponseEntity<Alert> create(@RequestBody Alert alert) {
        Alert saved = alertService.save(alert);
        return ResponseEntity.ok(saved);
    }

    // 🚨 Endpoint de prueba: fuerza una alerta y envía correo
    @GetMapping("/test")
    public ResponseEntity<String> triggerTestAlert() {
        alertChecker.triggerTestAlert();
        return ResponseEntity.ok("✅ Alerta de prueba ejecutada y correo enviado (revisa tu bandeja).");
    }
}

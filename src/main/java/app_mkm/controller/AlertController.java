package app_mkm.controller;

import app_mkm.entity.Alert;
import app_mkm.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "*")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
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




}

package app_mkm.controller;

import app_mkm.entity.AlertHistory;
import app_mkm.repository.AlertHistoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alert-history")
@CrossOrigin(origins = "*") // permite acceso desde tu frontend
public class AlertHistoryController {

    private final AlertHistoryRepository historyRepository;

    public AlertHistoryController(AlertHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @GetMapping
    public List<AlertHistory> getAllHistory() {
        return historyRepository.findAll();
    }
}

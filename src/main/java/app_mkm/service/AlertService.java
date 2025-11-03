package app_mkm.service;

import app_mkm.entity.Alert;
import app_mkm.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository repository;

    public AlertService(AlertRepository repository) {
        this.repository = repository;
    }

    public List<Alert> getAll() {
        return repository.findAll();
    }

    public Alert save(Alert alert) {
        return repository.save(alert);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }


}

package app_mkm.repository;

import app_mkm.entity.AlertHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {
}

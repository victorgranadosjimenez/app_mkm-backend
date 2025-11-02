package app_mkm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="card_name")
    private String cardName;

    @Column(name="set_name")
    private String setName;

    @Column(name = "card_condition")
    private String condition; // "Good", "Near Mint", etc.

    @Column(name="max_price")
    private double maxPrice;  // precio máximo permitido
    private String country;   // "Spain", etc.
    private String email;     // correo al que se enviará el aviso

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_triggered_at")
    private LocalDateTime lastTriggeredAt;




    public Alert() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }

    public String getSetName() { return setName; }
    public void setSetName(String setName) { this.setName = setName; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(double maxPrice) { this.maxPrice = maxPrice; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastTriggeredAt() {return lastTriggeredAt;}
    public void setLastTriggeredAt(LocalDateTime lastTriggeredAt) {this.lastTriggeredAt = lastTriggeredAt;}

}

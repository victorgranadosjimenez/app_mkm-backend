package app_mkm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alert_history")
public class AlertHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="card_name")
    private String cardName;

    @Column(name="set_name")
    private String setName;
    private double price;

    @Column(name="match_date")
    private LocalDateTime matchDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alert_id")
    @JsonIgnore
    private Alert alert;




    private String alertCardNameSnapshot;
    private String alertSetNameSnapshot;
    private String alertConditionSnapshot;
    private String alertCountrySnapshot;
    private Double alertMaxPriceSnapshot;




    public String getAlertCardNameSnapshot() {return alertCardNameSnapshot;}
    public void setAlertCardNameSnapshot(String alertCardNameSnapshot) {this.alertCardNameSnapshot = alertCardNameSnapshot;}
    public String getAlertSetNameSnapshot() {return alertSetNameSnapshot;}
    public void setAlertSetNameSnapshot(String alertSetNameSnapshot) {this.alertSetNameSnapshot = alertSetNameSnapshot;}
    public String getAlertConditionSnapshot() {return alertConditionSnapshot;}
    public void setAlertConditionSnapshot(String alertConditionSnapshot) {this.alertConditionSnapshot = alertConditionSnapshot;}
    public String getAlertCountrySnapshot() {return alertCountrySnapshot;}
    public void setAlertCountrySnapshot(String alertCountrySnapshot) {this.alertCountrySnapshot = alertCountrySnapshot;}
    public Double getAlertMaxPriceSnapshot() {return alertMaxPriceSnapshot;}
    public void setAlertMaxPriceSnapshot(Double alertMaxPriceSnapshot) {this.alertMaxPriceSnapshot = alertMaxPriceSnapshot;}






    // Getters & Setters
    public Long getId() { return id; }

    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }

    public String getSetName() { return setName; }
    public void setSetName(String setName) { this.setName = setName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public LocalDateTime getMatchDate() { return matchDate; }
    public void setMatchDate(LocalDateTime matchDate) { this.matchDate = matchDate; }

    public Alert getAlert() { return alert; }
    public void setAlert(Alert alert) { this.alert = alert; }
}


package com.progettoesame.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "game_plans")
@Data
public class Gameplan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private int totalHours;       // Ore totali stimate del gioco (es. 80)
    private int targetMonths;     // In quanti mesi l'utente vuole finirlo (es. 1)
    private String status;        // Stato del gioco: "DA_INIZIARE", "IN_CORSO", "COMPLETATO"
    private String playStyle;
}

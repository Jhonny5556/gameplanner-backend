package com.progettoesame.backend.controller;

import com.progettoesame.backend.model.Gameplan;
import com.progettoesame.backend.model.WeeklyAvailability;
import com.progettoesame.backend.repository.GamePlanRepository;
import com.progettoesame.backend.repository.WeeklyAvailabilityRepository;
import com.progettoesame.backend.service.GamePlannerService;
import com.progettoesame.backend.service.GamePlannerService.ReportResult;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planner")
public class GamePlannerController {

    @Autowired
    private GamePlannerService plannerService;

    @Autowired
    private GamePlanRepository gamePlanRepository;

    @Autowired
    private WeeklyAvailabilityRepository availabilityRepository;

    // --- CLASSE DTO PER RICEVERE I DATI DAL FRONTEND ---
    @Data
    public static class PlannerRequest {
        private Gameplan gamePlan;
        private WeeklyAvailability availability;
    }

    // --- ENDPOINT 1: Genera il report e salva nel DB ---
    @PostMapping("/generate")
    public ResponseEntity<ReportResult> generateAndSavePlan(@RequestBody PlannerRequest request) {
        
        // 1. Calcola il report usando il Service
        ReportResult report = plannerService.generateReport(
                request.getGamePlan().getTotalHours(),
                request.getGamePlan().getTargetMonths(),
                request.getAvailability(),
                request.getGamePlan().getPlayStyle()
        );

        // 2. Se è fattibile, salviamo i dati nel Database
        if (report.feasible) {
            // Salva la disponibilità
            availabilityRepository.save(request.getAvailability());
            
            // Salva il gioco impostando lo stato
            Gameplan planToSave = request.getGamePlan();
            planToSave.setStatus("IN_CORSO");
            gamePlanRepository.save(planToSave);
        }

        // 3. Restituisci il report calcolato al Frontend (Angular)
        return ResponseEntity.ok(report);
    }

    // --- ENDPOINT 2: Ottieni tutti i giochi in corso (Per la Dashboard di Angular) ---
    @GetMapping("/games")
    public ResponseEntity<List<Gameplan>> getAllGames() {
        return ResponseEntity.ok(gamePlanRepository.findAll());
    }
    
    // --- ENDPOINT 3: Cambia lo stato di un gioco (Es. da IN_CORSO a COMPLETATO) ---
    @PutMapping("/games/{id}/status")
    public ResponseEntity<Gameplan> updateGameStatus(@PathVariable Long id, @RequestParam String status) {
        return gamePlanRepository.findById(id).map(game -> {
            game.setStatus(status);
            Gameplan updated = gamePlanRepository.save(game);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    // --- ENDPOINT 4: Elimina un gioco dal Database ---
    @DeleteMapping("/games/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        if (gamePlanRepository.existsById(id)) {
            gamePlanRepository.deleteById(id);
            return ResponseEntity.ok().build(); // Gioco eliminato con successo
        }
        return ResponseEntity.notFound().build(); // Gioco non trovato
    }
    
}
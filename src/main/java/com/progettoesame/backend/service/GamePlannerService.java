package com.progettoesame.backend.service;

import com.progettoesame.backend.model.WeeklyAvailability;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GamePlannerService {

    // Questo è un DTO (Data Transfer Object) interno per restituire il report
    public static class ReportResult {
        public boolean feasible;
        public double requiredWeeklyHours;
        public String message;
        public Map<String, Double> schedule = new HashMap<>();
    }

    public ReportResult generateReport(int totalGameHours, int targetMonths, WeeklyAvailability availability, String playStyle) {
        ReportResult report = new ReportResult();

        // --- NUOVO: MOLTIPLICATORE DI DIFFICOLTÀ ---
        double multiplier = 1.0;
        if ("SPEEDRUNNER".equalsIgnoreCase(playStyle)) multiplier = 0.8;
        else if ("COMPLETISTA".equalsIgnoreCase(playStyle)) multiplier = 1.5;

        double adjustedHours = totalGameHours * multiplier; // Le ore reali in base allo stile

        int targetWeeks = targetMonths * 4; 
        double requiredWeeklyHours = adjustedHours / targetWeeks;
        double totalAvailableHours = availability.getTotalWeeklyHours();

        if (totalAvailableHours < requiredWeeklyHours) {
            report.feasible = false;
            int realWeeksNeeded = (int) Math.ceil(adjustedHours / totalAvailableHours);
            double realMonthsNeeded = realWeeksNeeded / 4.0;
            report.message = String.format("Troppe ore richieste per un %s! Ti serviranno circa %.1f mesi.", 
                    playStyle != null ? playStyle : "giocatore", realMonthsNeeded);
            return report;
        }

        report.feasible = true;
        report.requiredWeeklyHours = Math.round(requiredWeeklyHours * 10.0) / 10.0;
        report.message = "Tabella di marcia calcolata con successo per lo stile: " + playStyle;

        report.schedule.put("Lunedi", calculateDailyQuota(availability.getMonday(), totalAvailableHours, requiredWeeklyHours));
        report.schedule.put("Martedi", calculateDailyQuota(availability.getTuesday(), totalAvailableHours, requiredWeeklyHours));
        report.schedule.put("Mercoledi", calculateDailyQuota(availability.getWednesday(), totalAvailableHours, requiredWeeklyHours));
        report.schedule.put("Giovedi", calculateDailyQuota(availability.getThursday(), totalAvailableHours, requiredWeeklyHours));
        report.schedule.put("Venerdi", calculateDailyQuota(availability.getFriday(), totalAvailableHours, requiredWeeklyHours));
        report.schedule.put("Sabato", calculateDailyQuota(availability.getSaturday(), totalAvailableHours, requiredWeeklyHours));
        report.schedule.put("Domenica", calculateDailyQuota(availability.getSunday(), totalAvailableHours, requiredWeeklyHours));

        return report;
    }

    // Metodo di supporto per arrotondare a 1 cifra decimale le ore giornaliere
    private double calculateDailyQuota(double dayHours, double totalAvailable, double requiredHours) {
        if (dayHours == 0) return 0.0;
        double quota = (dayHours / totalAvailable) * requiredHours;
        return Math.round(quota * 10.0) / 10.0;
    }
}

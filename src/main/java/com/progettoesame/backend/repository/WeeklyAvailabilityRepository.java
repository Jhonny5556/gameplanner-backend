package com.progettoesame.backend.repository;

import com.progettoesame.backend.model.WeeklyAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyAvailabilityRepository extends JpaRepository<WeeklyAvailability, Long> {
}
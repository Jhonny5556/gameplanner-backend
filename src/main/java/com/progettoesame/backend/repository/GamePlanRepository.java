package com.progettoesame.backend.repository;

import com.progettoesame.backend.model.Gameplan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamePlanRepository extends JpaRepository<Gameplan, Long> {
    // Spring Boot implementerà automaticamente i metodi save(), findAll(), findById(), ecc.
}

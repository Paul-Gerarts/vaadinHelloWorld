package be.juvo.paul.battleship.repositories;

import be.juvo.paul.battleship.entities.PlayConditions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayConditionsRepository extends JpaRepository<PlayConditions, Long> {

    Optional<PlayConditions> findByNameAndGridSize(String name, int gridSize);
}

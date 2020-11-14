package be.juvo.paul.battleship.repositories;

import be.juvo.paul.battleship.entities.ComputerShot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComputerShotRepository extends JpaRepository<ComputerShot, Long> {

}

package be.juvo.paul.battleship.repositories;

import be.juvo.paul.battleship.entities.Vector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VectorRepository extends JpaRepository<Vector, Long> {

    List<Vector> findAllByMyBoatsAndHit(boolean myBoats, boolean hit);

    List<Vector> findAllByMyBoatsAndBoatNameAndHitIsFalse(boolean myBoats, String boatName);

    List<Vector> findAllByMyBoats(boolean myBoats);
}

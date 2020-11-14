package be.juvo.paul.battleship.repositories;

import be.juvo.paul.battleship.entities.Vector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VectorRepository extends JpaRepository<Vector, Long> {

    List<Vector> findAllByMyBoatsAndHitAndContainsBoat(boolean myBoats, boolean hit, boolean containsBoat);

    List<Vector> findAllByMyBoatsAndContainsBoat(boolean myBoats, boolean containsBoat);

    List<Vector> findAllByMyBoatsAndBoatNameAndHitIsFalse(boolean myBoats, String boatName);

}

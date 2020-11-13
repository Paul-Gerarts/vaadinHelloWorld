package be.juvo.paul.battleship.services;

import be.juvo.paul.battleship.entities.Vector;
import be.juvo.paul.battleship.repositories.VectorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class VectorServiceImpl {

    private final VectorRepository vectorRepository;

    @Autowired
    public VectorServiceImpl(
            VectorRepository vectorRepository
    ) {
        this.vectorRepository = vectorRepository;
    }

    public void save(Vector vector) {
        vectorRepository.save(vector);
    }

    public List<Vector> findAllUnsunkBoats(boolean myBoats) {
        return vectorRepository.findAllByMyBoatsAndHit(myBoats, false);
    }

    public List<Vector> findAllUnsunkBoatsByName(boolean myBoats, String boatName) {
        return vectorRepository.findAllByMyBoatsAndBoatNameAndHitIsFalse(myBoats, boatName);
    }

    public List<Vector> findAllByMyBoats(boolean myBoats) {
        return vectorRepository.findAllByMyBoats(myBoats);
    }

    public void deleteAll() {
        vectorRepository.deleteAll();
    }
}

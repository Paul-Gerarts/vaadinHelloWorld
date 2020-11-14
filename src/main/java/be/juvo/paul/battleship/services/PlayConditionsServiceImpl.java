package be.juvo.paul.battleship.services;

import be.juvo.paul.battleship.entities.PlayConditions;
import be.juvo.paul.battleship.repositories.PlayConditionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayConditionsServiceImpl {

    private final PlayConditionsRepository playConditionsRepository;

    @Autowired
    public PlayConditionsServiceImpl(
            PlayConditionsRepository playConditionsRepository
    ) {
        this.playConditionsRepository = playConditionsRepository;
    }

    public void save(String name, int gridSize) {
        playConditionsRepository.save(PlayConditions.builder()
                .name(name)
                .gridSize(gridSize)
                .build());
    }

    public void deleteAll() {
        playConditionsRepository.deleteAll();
    }

    public PlayConditions findByNameAndGridSize(String name, int gridSize) {
        return playConditionsRepository.findByNameAndGridSize(name, gridSize).orElse(null);
    }
}

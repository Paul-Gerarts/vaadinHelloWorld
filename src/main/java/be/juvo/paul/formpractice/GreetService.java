package be.juvo.paul.formpractice;

import be.juvo.paul.battleship.entities.Coordinate;
import be.juvo.paul.battleship.entities.PlayConditions;
import be.juvo.paul.battleship.services.PlayConditionsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Service
public class GreetService implements Serializable {

    private PlayConditionsServiceImpl playConditionsService;

    @Autowired
    public GreetService(
            PlayConditionsServiceImpl playConditionsService
    ) {
        this.playConditionsService = playConditionsService;
    }

    public String greet(String name, int gridSize) {
        PlayConditions playConditions = playConditionsService.findByNameAndGridSize(name, gridSize);
        if (isBlank(name) || gridSize == 0) {
            log.info("invalid form information");
            return "Invalid form information";
        } else if (null == playConditions) {
            playConditionsService.save(name, gridSize);
            log.info("starting new game with config: 'name: {}, gridSize: {}'", name, gridSize);
            return "Starting new game with config: 'name: " + name + ", gridSize: " + gridSize + "'";
        } else {
            log.info("Reloaded game, started with config: {}", playConditions.toString());
            return "Reloaded game, started with config: " + playConditions.toString();
        }
    }

    public String update(boolean hit, boolean computer, Coordinate coordinate) {
        if (computer) {
            return "The computer shot and " + (hit ? "hit" : "missed") + " at " + coordinate.getRow() + coordinate.getColumn().substring(1);
        }
        return "You shot and " + (hit ? "hit" : "missed") + " at " + coordinate.getRow() + coordinate.getColumn().substring(1);
    }

    public String sunkBoat(boolean computer, String boatName) {
        if (computer) {
            return "The computer sunk your " + boatName;
        }
        return "You sunk the enemy's " + boatName;
    }

    public String fieldAlreadySelected() {
        return "field already targeted! Try another one!";
    }

    public String gameOver(String message) {
        return message;
    }

}

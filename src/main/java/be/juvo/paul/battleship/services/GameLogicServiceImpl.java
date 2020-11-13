package be.juvo.paul.battleship.services;

import be.juvo.paul.battleship.entities.Coordinate;
import be.juvo.paul.battleship.entities.Vector;
import be.juvo.paul.battleship.exceptions.WinException;
import be.juvo.paul.formpractice.GreetService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import static be.juvo.paul.battleship.constants.Markings.HIT;
import static be.juvo.paul.battleship.constants.Markings.MISS;

@Slf4j
@Service
public class GameLogicServiceImpl {

    private VectorServiceImpl vectorService;
    private GreetService greetService;

    @Autowired
    public GameLogicServiceImpl(
            VectorServiceImpl vectorService,
            GreetService greetService
    ) {
        this.vectorService = vectorService;
        this.greetService = greetService;
    }

    public void executePlayerLogic(List<Vector> boatPositions,
                                   Grid<Coordinate> grid,
                                   AbstractField.ComponentValueChangeEvent<Grid<Coordinate>, Coordinate> clickEvent,
                                   boolean myGrid) throws WinException {
        Coordinate coordinate = clickEvent.getValue();
        boolean hit = boatPositions.stream().anyMatch(containsBoat(coordinate.getColumn(), coordinate.getRow()));
        handleHit(coordinate, myGrid, hit);
        Notification.show(greetService.update(hit, false, coordinate));
        grid.getEditor().editItem(markAsShot(coordinate, hit));
        log.info("New coordinate: {}", clickEvent.getValue().toString());
        grid.getDataProvider().refreshItem(coordinate);
    }

    public void executeComputerLogic(List<Grid<Coordinate>> computergrids,
                                     boolean myGrid) throws WinException {
        Random random = new Random();
        int randomCoordinate = random.nextInt(computergrids.size());
        // TODO fetch the right grid!
        Grid<Coordinate> grid = computergrids.get(randomCoordinate);
        Coordinate coordinate = grid.getEditor().getItem();
        boolean hit = vectorService.findAllByMyBoats(false).stream().anyMatch(containsBoat(coordinate.getColumn(), coordinate.getRow()));
        handleHit(coordinate, myGrid, hit);
        Notification.show(greetService.update(hit, true, coordinate));
        grid.getEditor().editItem(markAsShot(coordinate, hit));
        log.info("New coordinate: {}", coordinate.toString());
        grid.getDataProvider().refreshItem(coordinate);
    }

    public void fieldAlreadySelected() {
        Notification.show(greetService.fieldAlreadySelected());
    }

    public void gameOver(WinException we) {
        Notification.show(greetService.gameOver(we.getMessage()));
    }

    private Predicate<Vector> containsBoat(String header, String row) {
        return vector -> vector.getRow().equals(row) && String.valueOf(vector.getColumn()).equals(header.substring(1));
    }

    private void handleHit(Coordinate coordinate, boolean myBoats, boolean hit) throws WinException {
        if (hit) {
            List<Vector> unsunkBoats = vectorService.findAllUnsunkBoats(myBoats);
            Vector unsunkBoat = unsunkBoats.stream()
                    .filter(containsBoat(coordinate.getColumn(), coordinate.getRow()))
                    .findFirst()
                    .orElse(null);

            if (!unsunkBoats.isEmpty() && null != unsunkBoat) {
                unsunkBoat.setHit(true);
                vectorService.save(unsunkBoat);
                List<Vector> remainingPiecesOfSingleBoat = vectorService.findAllUnsunkBoatsByName(myBoats, unsunkBoat.getBoatName());
                if (remainingPiecesOfSingleBoat.isEmpty()) {
                    Notification.show(greetService.sunkBoat(myBoats, unsunkBoat.getBoatName()));
                    if (vectorService.findAllUnsunkBoats(myBoats).isEmpty()) {
                        throw new WinException(myBoats);
                    }
                }
            }
        }
    }

    private Coordinate markAsShot(Coordinate coordinate, boolean hit) {
        String column = coordinate.getColumn().substring(1);
        String marking = hit ? HIT.getStringValue() : MISS.getStringValue();
        switch (column) {
            case "1":
                coordinate.set_1(marking);
                break;
            case "2":
                coordinate.set_2(marking);
                break;
            case "3":
                coordinate.set_3(marking);
                break;
            case "4":
                coordinate.set_4(marking);
                break;
            case "5":
                coordinate.set_5(marking);
                break;
            case "6":
                coordinate.set_6(marking);
                break;
            case "7":
                coordinate.set_7(marking);
                break;
            case "8":
                coordinate.set_8(marking);
                break;
            case "9":
                coordinate.set_9(marking);
                break;
            case "10":
                coordinate.set_10(marking);
                break;
            default:
        }
        coordinate.setDesignated(true);
        return coordinate;
    }
}

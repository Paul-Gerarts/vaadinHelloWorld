package be.juvo.paul.battleship.services;

import be.juvo.paul.battleship.entities.ComputerShot;
import be.juvo.paul.battleship.entities.Coordinate;
import be.juvo.paul.battleship.entities.Vector;
import be.juvo.paul.battleship.exceptions.WinException;
import be.juvo.paul.formpractice.GreetService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.provider.ListDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static be.juvo.paul.battleship.constants.Markings.HIT;
import static be.juvo.paul.battleship.constants.Markings.MISS;

@Slf4j
@Service
public class GameLogicServiceImpl {

    private VectorServiceImpl vectorService;
    private GreetService greetService;
    private ComputerShotServiceImpl computerShotService;

    @Autowired
    public GameLogicServiceImpl(
            VectorServiceImpl vectorService,
            GreetService greetService,
            ComputerShotServiceImpl computerShotService
    ) {
        this.vectorService = vectorService;
        this.greetService = greetService;
        this.computerShotService = computerShotService;
    }

    public void executePlayerLogic(List<Vector> boatPositions,
                                   Grid<Coordinate> grid,
                                   AbstractField.ComponentValueChangeEvent<Grid<Coordinate>, Coordinate> clickEvent) throws WinException {
        Coordinate coordinate = clickEvent.getValue();
        boolean hit = boatPositions.stream().anyMatch(containsBoat(coordinate.getColumn(), coordinate.getRow()));
        handleHit(coordinate, true, hit);
        Notification.show(greetService.update(hit, false, coordinate)).setPosition(Notification.Position.BOTTOM_END);
        grid.getEditor().editItem(markAsShot(coordinate, hit));
        log.info("New playerGrid coordinate: {}", clickEvent.getValue().toString());
        grid.getDataProvider().refreshItem(coordinate);
    }

    public void executeComputerLogic(List<Grid<Coordinate>> computergrids) throws WinException {
        Random random = new Random();
        int excludeNonTargetableCoordinate = computergrids.size() - 1;
        Coordinate coordinate;
        Grid<Coordinate> grid;
        int counter = 0;

        int column = 0;
        int row = 0;
        boolean keepShooting;
        List<ComputerShot> hits = computerShotService.findAllSortedByIdDesc()
                .stream()
                .limit(4)
                .filter(ComputerShot::getHit)
                .collect(Collectors.toList());

        do {
            if (!hits.isEmpty() && counter++ <= 4) {
                keepShooting = true;
                ComputerShot computerShot = hits.get(0);
                column = computerShot.getColumn();
                row = computeRowNumber(computerShot.getRow());
                int randomColumnIndex = column;
                int randomRowIndex = row;
                grid = computergrids.get(randomColumnIndex);
                ListDataProvider<Coordinate> gridCoordinates = (ListDataProvider<Coordinate>) grid.getDataProvider();
                List<Coordinate> coordinates = (List<Coordinate>) gridCoordinates.getItems();
                coordinate = coordinates.get(randomRowIndex);
                List<Vector> targets = coordinate.getNeighbouringCells().stream().filter(vector -> !vector.getHit()).collect(Collectors.toList());
                int newTarget = random.nextInt(targets.size());
                Vector targetVector = targets.get(newTarget);
                column = targetVector.getColumn();
                row = computeRowNumber(targetVector.getRow());
            } else {
                keepShooting = false;
            }

            int randomColumnIndex = keepShooting ? column : random.nextInt(excludeNonTargetableCoordinate) + 1;
            int randomRowIndex = keepShooting ? row : random.nextInt(excludeNonTargetableCoordinate);
            grid = computergrids.get(randomColumnIndex);
            ListDataProvider<Coordinate> gridCoordinates = (ListDataProvider<Coordinate>) grid.getDataProvider();
            List<Coordinate> coordinates = (List<Coordinate>) gridCoordinates.getItems();
            coordinate = coordinates.get(randomRowIndex);
        } while (coordinate.isDesignated());

        boolean hit = vectorService.findAllByMyBoatsAndContainsBoat(false, true).stream().anyMatch(containsBoat(coordinate.getColumn(), coordinate.getRow()));

        handleHit(coordinate, false, hit);
        Notification.show(greetService.update(hit, true, coordinate)).setPosition(Notification.Position.BOTTOM_END);
        grid.getEditor().editItem(markAsShot(coordinate, hit));
        log.info("New computerGrid coordinate: {}", coordinate.toString());
        grid.getDataProvider().refreshItem(coordinate);

        computerShotService.save(ComputerShot.builder()
                .column(Integer.parseInt(coordinate.getColumn().substring(1)))
                .row(coordinate.getRow())
                .hit(hit)
                .designated(true)
                .build());
    }

    private int computeRowNumber(String row) {
        switch (row) {
            default:
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
            case "E":
                return 4;
            case "F":
                return 5;
            case "G":
                return 6;
            case "H":
                return 7;
            case "I":
                return 8;
            case "J":
                return 9;
        }
    }

    public void fieldAlreadySelected() {
        Notification.show(greetService.fieldAlreadySelected()).setPosition(Notification.Position.BOTTOM_END);
    }

    public void gameOver(WinException we) {
        Notification.show(greetService.gameOver(we.getMessage())).setPosition(Notification.Position.BOTTOM_END);
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
                    Notification.show(greetService.sunkBoat(myBoats, unsunkBoat.getBoatName())).setPosition(Notification.Position.BOTTOM_END);
                    computerShotService.deleteAll();
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

package be.juvo.paul.battleship.factories;

import be.juvo.paul.battleship.entities.Coordinate;
import be.juvo.paul.battleship.entities.PlayConditions;
import be.juvo.paul.battleship.entities.Vector;
import be.juvo.paul.battleship.services.PlayConditionsServiceImpl;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.selection.SingleSelect;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static be.juvo.paul.battleship.constants.Markings.*;

@Slf4j
@Builder
@Getter
@Setter
@ToString
@Component
@NoArgsConstructor
public class GridBuilder {

    private PlayConditionsServiceImpl playConditionsService;

    @Autowired
    public GridBuilder(
            PlayConditionsServiceImpl playConditionsService
    ) {
        this.playConditionsService = playConditionsService;
    }

    public List<Grid<Coordinate>> buildGrid(String name, int gridSize) {
        PlayConditions playConditions = playConditionsService.findByNameAndGridSize(name, gridSize);

        if (null != playConditions) {
            // later implement to directly load Grid
            gridSize = playConditions.getGridSize();
        }
        switch (gridSize) {
            case 0:
                return new ArrayList<>();
            case 10:
            default:
                return buildGrid(gridSize
                        , new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"}
                        , new String[]{"row", "_1", "_2", "_3", "_4", "_5", "_6", "_7", "_8", "_9", "_10"});
        }
    }

    private List<Grid<Coordinate>> buildGrid(int gridSize, String[] rows, String[] headers) {
        List<Grid<Coordinate>> gameGrid = new LinkedList<>();
        List<Vector> boatPositions = markBoatPositions(gridSize);
        for (String header : headers) {
            Grid<Coordinate> grid = new Grid<>(Coordinate.class);
            grid.setItems(getBlankGridItems(gridSize, rows, header, boatPositions));
            grid.setColumns(header);
            grid.setWidth("120px");
            grid.setHeight("450px");
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            SingleSelect<Grid<Coordinate>, Coordinate> coordinateSelect = grid.asSingleSelect();
            grid.setColumnReorderingAllowed(false);
            grid.setMultiSort(false);
            grid.getColumns().forEach(column -> column.setSortable(false));
            coordinateSelect.addValueChangeListener(clickEvent -> {
                try {
                    Coordinate coordinate = clickEvent.getValue();
                    boolean hit = boatPositions.stream().anyMatch(containsBoat(coordinate.getColumn(), coordinate.getRow()));
                    log.info("You shot and {}. Coordinate: {}", hit ? "hit" : "missed", coordinate.toString());
                    grid.getEditor().editItem(markAsShot(coordinate, hit));
                    log.info("New coordinate?: {}", clickEvent.getValue().toString());
                    grid.getDataProvider().refreshItem(coordinate);
                } catch (NullPointerException npe) {
                    log.info("field already targeted! Try another one!");
                }
            });
            gameGrid.add(grid);
        }
        return gameGrid;
    }

    private List<Coordinate> getBlankGridItems(int gridSize, String[] rows, String header, List<Vector> boatPositions) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 1; i <= gridSize; i++) {
            final String row = rows[i - 1];
            Coordinate coordinate = Coordinate.builder()
                    .row(row)
                    ._1(BLANK.getStringValue())
                    ._2(BLANK.getStringValue())
                    ._3(BLANK.getStringValue())
                    ._4(BLANK.getStringValue())
                    ._5(BLANK.getStringValue())
                    ._6(BLANK.getStringValue())
                    ._7(BLANK.getStringValue())
                    ._8(BLANK.getStringValue())
                    ._9(BLANK.getStringValue())
                    ._10(BLANK.getStringValue())
                    .designated(false)
                    .column(header)
                    .neighbouringCells(calculateNeighbouringCells(rows[i - 1], header, gridSize))
                    .build();

            if (boatPositions.stream().anyMatch(containsBoat(header, row))) {
                coordinate.setContainsBoat(true);
            }
            coordinates.add(coordinate);
        }
        return coordinates;
    }

    private Predicate<Vector> containsBoat(String header, String row) {
        return vector -> vector.getRow().equals(row) && String.valueOf(vector.getColumn()).equals(header.substring(1));
    }

    private List<Vector> calculateNeighbouringCells(String row, String header, int gridSize) {
        Vector[] neighbouringCells;
        int column = header.equals("row") ? 0 : Integer.parseInt(header.substring(1));
        switch (row) {
            case "A":
                neighbouringCells = calculateNeighbouringVectors(null, "B", "A", column);
                break;
            case "B":
                neighbouringCells = calculateNeighbouringVectors("A", "C", "B", column);
                break;
            case "C":
                neighbouringCells = calculateNeighbouringVectors("B", "D", "C", column);
                break;
            case "D":
                neighbouringCells = calculateNeighbouringVectors("C", "E", "D", column);
                break;
            case "E":
                neighbouringCells = calculateNeighbouringVectors("D", "F", "E", column);
                break;
            case "F":
                neighbouringCells = calculateNeighbouringVectors("E", "G", "F", column);
                break;
            case "G":
                neighbouringCells = calculateNeighbouringVectors("F", "H", "G", column);
                break;
            case "H":
                neighbouringCells = calculateNeighbouringVectors("G", "I", "H", column);
                break;
            case "I":
                neighbouringCells = calculateNeighbouringVectors("H", "J", "I", column);
                break;
            case "J":
                neighbouringCells = calculateNeighbouringVectors("I", null, "J", column);
                break;
            default:
                neighbouringCells = calculateNeighbouringVectors(null, null, null, column);
        }
        return Arrays.stream(neighbouringCells)
                .filter(filterOutInvalidVectors(gridSize))
                .collect(Collectors.toList());
    }

    private Predicate<Vector> filterOutInvalidVectors(int gridSize) {
        return vector -> null != vector.getRow() || vector.getColumn() < 1 || vector.getColumn() > gridSize;
    }

    private Vector[] calculateNeighbouringVectors(String rowAbove, String rowBelow, String currentRow, int column) {
        int index = 0;
        Vector[] neighbouringCells = new Vector[4];
        // above
        neighbouringCells[index++] = Vector.builder()
                .row(rowAbove)
                .column(column)
                .build();
        // below
        neighbouringCells[index++] = Vector.builder()
                .row(rowBelow)
                .column(column)
                .build();
        // left
        neighbouringCells[index++] = Vector.builder()
                .row(currentRow)
                .column(column - 1)
                .build();
        // right
        neighbouringCells[index] = Vector.builder()
                .row(currentRow)
                .column(column + 1)
                .build();
        return neighbouringCells;
    }

    private String fillCoordinate(int columnToFill, int currentColumn) {
        return columnToFill == currentColumn ? HIT.getStringValue() : NOT_AVAILABLE.getStringValue();
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

    private List<Vector> markBoatPositions(int gridSize) {
        List<Vector> boatPositions = new ArrayList<>();
        switch (gridSize) {
            case 10:
                boatPositions.addAll(randomizeBoats(4, gridSize));
                boatPositions.addAll(randomizeBoats(5, gridSize));
            case 5:
                boatPositions.addAll(randomizeBoats(2, gridSize));
                boatPositions.addAll(randomizeBoats(3, gridSize));
                boatPositions.addAll(randomizeBoats(3, gridSize));
                break;
            default:
        }
        return boatPositions;
    }

    private List<Vector> randomizeBoats(int boatSize, int gridSize) {
        // TODO make sure boats don't overlap. Ensure correct count. Perhaps cluster the boats for future hit-notification
        List<Vector> boatPositions = new ArrayList<>();
        int randomColumnStart;
        Random random = new Random();
        int randomDirection = random.nextInt(4) + 1;
        char randomRow;
        switch (randomDirection) {
            // UP
            case 1:
                randomColumnStart = (random.nextInt(gridSize) + 1);
                randomRow = (char) (65 + boatSize + random.nextInt(boatSize));
                for (int i = 1; i <= boatSize; i++) {
                    char row = (char) (randomRow - i);
                    boatPositions.add(Vector.builder()
                            .row(String.valueOf(row))
                            .column(randomColumnStart)
                            .build());
                }
                break;
            // DOWN
            case 2:
                randomColumnStart = (random.nextInt(gridSize) + 1);
                randomRow = (char) (65 + random.nextInt(boatSize));
                for (int i = 1; i <= boatSize; i++) {
                    char row = (char) (randomRow + i);
                    boatPositions.add(Vector.builder()
                            .row(String.valueOf(row))
                            .column(randomColumnStart)
                            .build());
                }
                break;
            // LEFT
            case 3:
                randomColumnStart = (random.nextInt(boatSize) + 1) + boatSize;
                randomRow = (char) (65 + random.nextInt(gridSize));
                for (int i = 1; i <= boatSize; i++) {
                    boatPositions.add(Vector.builder()
                            .row(String.valueOf(randomRow))
                            .column(randomColumnStart - i)
                            .build());
                }
                break;
            case 4:
                randomColumnStart = (random.nextInt(boatSize) + 1);
                randomRow = (char) (65 + random.nextInt(gridSize));
                for (int i = 1; i <= boatSize; i++) {
                    boatPositions.add(Vector.builder()
                            .row(String.valueOf(randomRow))
                            .column(randomColumnStart + i)
                            .build());
                }
                break;
            default:
        }
        return boatPositions;
    }
}

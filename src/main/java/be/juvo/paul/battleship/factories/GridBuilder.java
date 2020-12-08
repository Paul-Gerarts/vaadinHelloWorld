package be.juvo.paul.battleship.factories;

import be.juvo.paul.battleship.entities.Coordinate;
import be.juvo.paul.battleship.entities.PlayConditions;
import be.juvo.paul.battleship.entities.Vector;
import be.juvo.paul.battleship.exceptions.WinException;
import be.juvo.paul.battleship.services.GameLogicServiceImpl;
import be.juvo.paul.battleship.services.PlayConditionsServiceImpl;
import be.juvo.paul.battleship.services.VectorServiceImpl;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.selection.SingleSelect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static be.juvo.paul.battleship.constants.Markings.BLANK;
import static be.juvo.paul.battleship.constants.Markings.BOAT;

@Slf4j
@Getter
@Setter
@ToString
@Component
@NoArgsConstructor
public class GridBuilder {

    private PlayConditionsServiceImpl playConditionsService;
    private VectorServiceImpl vectorService;
    private GameLogicServiceImpl gameLogicService;
    private static final String FAIL_SAFE = "NOT AVAILABLE";
    private List<Grid<Coordinate>> computerGrids = new ArrayList<>();

    @Autowired
    public GridBuilder(
            PlayConditionsServiceImpl playConditionsService,
            VectorServiceImpl vectorService,
            GameLogicServiceImpl gameLogicService
    ) {
        this.playConditionsService = playConditionsService;
        this.vectorService = vectorService;
        this.gameLogicService = gameLogicService;
    }

    public List<Grid<Coordinate>> buildGrid(String name, int gridSize, boolean myGrid) {
        PlayConditions playConditions = playConditionsService.findByNameAndGridSize(name, gridSize);

        if (null != playConditions) {
            // later implement to directly load Grid
            gridSize = playConditions.getGridSize();
        }
        switch (gridSize) {
            case 5:
                return buildGrid(gridSize,
                        new String[]{"A", "B", "C", "D", "E"},
                        new String[]{"row", "_1", "_2", "_3", "_4", "_5"},
                        myGrid);
            case 10:
            default:
                return buildGrid(gridSize,
                        new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"},
                        new String[]{"row", "_1", "_2", "_3", "_4", "_5", "_6", "_7", "_8", "_9", "_10"},
                        myGrid);
        }
    }

    /*
     * see the Vaadin book at page 49 -> also Tree Grid for hierarchical structures
     */
    private List<Grid<Coordinate>> buildGrid(int gridSize, String[] rows, String[] headers, boolean myGrid) {
        List<Grid<Coordinate>> gameGrid = new LinkedList<>();
        List<Vector> boatPositions = markBoatPositions(gridSize, myGrid);
        for (String header : headers) {
            Grid<Coordinate> grid = new Grid<>(Coordinate.class); // this automatically sets the property names as column keys
            grid.setItems(getGridItems(gridSize, rows, header, boatPositions, myGrid));
            grid.setColumns(header);
            grid.setWidth("120px");
            grid.setHeight(gridSize == 10 ? "450px" : "250px");
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            SingleSelect<Grid<Coordinate>, Coordinate> coordinateSelect = grid.asSingleSelect();
            grid.setColumnReorderingAllowed(false); // default is false
            grid.setMultiSort(false);
            grid.getColumns().forEach(column -> column.setSortable(false));
            // == grid.setSortableColumns(); when no param -> column excluded and thus not sortable
            coordinateSelect.addValueChangeListener(clickEvent -> {
                try {
                    gameLogicService.executePlayerLogic(boatPositions, grid, clickEvent);
                    gameLogicService.executeComputerLogic(computerGrids);
                } catch (NullPointerException npe) {
                    gameLogicService.fieldAlreadySelected();
                } catch (WinException we) {
                    gameLogicService.gameOver(we);
                }
            });
            gameGrid.add(grid);
        }
        if (!myGrid) {
            computerGrids = gameGrid;
        }
        return gameGrid;
    }


    private List<Coordinate> getGridItems(int gridSize, String[] rows, String header, List<Vector> boatPositions, boolean myGrid) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 1; i <= gridSize; i++) {
            final String row = rows[i - 1];
            final boolean containsBoat = boatPositions.stream().anyMatch(containsBoat(header, row));
            final String marking = containsBoat && !myGrid ? BOAT.getStringValue() : BLANK.getStringValue();
            Coordinate coordinate = Coordinate.builder()
                    .row(row)
                    ._1(marking)
                    ._2(marking)
                    ._3(marking)
                    ._4(marking)
                    ._5(marking)
                    ._6(marking)
                    ._7(marking)
                    ._8(marking)
                    ._9(marking)
                    ._10(marking)
                    .designated(false)
                    .column(header)
                    .neighbouringCells(calculateNeighbouringCells(rows[i - 1], header, gridSize))
                    .build();

            if (!header.equals("row")) {
                Vector vector = Vector.builder()
                        .row(row)
                        .column(Integer.parseInt(header.substring(1)))
                        .hit(false)
                        .myBoats(myGrid)
                        .containsBoat(containsBoat)
                        .build();
                List<Vector> singleBoat = boatPositions.stream().filter(containsBoat(header, row)).distinct().collect(Collectors.toList());
                if (!singleBoat.isEmpty()) {
                    vector.setBoatName(singleBoat.get(0).getBoatName());
                }
                vectorService.save(vector);
            }

            if (containsBoat) {
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
        return vector -> null != vector.getRow() || (vector.getColumn() < 1 && vector.getColumn() <= gridSize);
    }

    private Vector[] calculateNeighbouringVectors(String rowAbove, String rowBelow, String currentRow, int column) {
        int index = 0;
        Vector[] neighbouringCells = new Vector[4];
        // above
        neighbouringCells[index++] = Vector.builder()
                .row(rowAbove)
                .column(column)
                .hit(false)
                .build();
        // below
        neighbouringCells[index++] = Vector.builder()
                .row(rowBelow)
                .column(column)
                .hit(false)
                .build();
        // left
        neighbouringCells[index++] = Vector.builder()
                .row(currentRow)
                .column(column - 1)
                .hit(false)
                .build();
        // right
        neighbouringCells[index] = Vector.builder()
                .row(currentRow)
                .column(column + 1)
                .hit(false)
                .build();
        return neighbouringCells;
    }

    private List<Vector> markBoatPositions(int gridSize, boolean myBoats) {
        List<Vector> boatPositions = new ArrayList<>();
        int amountOfVectors = 0;
        switch (gridSize) {
            case 10:
                boatPositions.addAll(createAndSaveBoat(gridSize, 5, boatPositions, "Battleship", myBoats));
                boatPositions.addAll(createAndSaveBoat(gridSize, 4, boatPositions, "Cruiser", myBoats));
                amountOfVectors = amountOfVectors + 9;
            case 5:
                boatPositions.addAll(createAndSaveBoat(gridSize, 3, boatPositions, "Minesweeper", myBoats));
                boatPositions.addAll(createAndSaveBoat(gridSize, 3, boatPositions, "Submarine", myBoats));
                boatPositions.addAll(createAndSaveBoat(gridSize, 2, boatPositions, "Patrolboat", myBoats));
                amountOfVectors = amountOfVectors + 8;
                break;
            default:
        }
        if (boatPositions.size() != amountOfVectors) {
            vectorService.deleteAll();
            return markBoatPositions(gridSize, myBoats);
        }
        return boatPositions;
    }

    private List<Vector> createAndSaveBoat(int gridSize, int boatSize, List<Vector> boatPositions, String name, boolean myBoats) {
        List<Vector> individualBoatPositions;
        individualBoatPositions = randomizeBoats(boatSize, gridSize, boatPositions);
        individualBoatPositions.forEach(vector -> {
            vector.setMyBoats(myBoats);
            vector.setBoatName(name);
        });
        return individualBoatPositions;
    }

    private List<Vector> randomizeBoats(int boatSize, int gridSize, List<Vector> definedBoatPositions) {
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
            // RIGHT
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
        for (Vector position : boatPositions) {
            boolean overLap = definedBoatPositions.stream().anyMatch(containsBoat("_" + position.getColumn(), position.getRow()));
            if (overLap) {
                return randomizeBoats(boatSize, gridSize, definedBoatPositions);
            }
        }
        return boatPositions;
    }

    private void configureColumn(Grid<Coordinate> grid) {
        Grid.Column<Coordinate> column = grid
                .addColumn(Coordinate::get_1)
                .setHeader("1")
                .setFlexGrow(0)
                .setWidth("100px")
                .setResizable(false)
                .setKey("identifierForQuickRetrievalThrough_getColumnByKey(String name)");
        column.setVisible(false); // still holds it data when other columns are filled. Better to remove, or don't add for starters
        column.setFrozen(true); // will remain in place and visible when the user scrolls horizontally
    }

    private void setColumnsManually() {
        try {
            Grid<Coordinate> grid = new Grid<>(Coordinate.class, false);
            grid.addColumn(coordinate -> coordinate.getRow().split(" ")[0])
                    .setHeader("Row");
            grid.addColumns("designated", "containsBoat");
        } catch (IllegalArgumentException iae) {
            log.warn("You tried to add a column that's already present in the grid");
        }
    }

    /*
     * Look into Renderers p.73 --most interesting IMHO: ComponentRenderer p. 80--
     */
    private void groupColumns(Grid<Coordinate> grid) {
        // Create a header row
        HeaderRow topRow = grid.prependHeaderRow();
        // group two columns under the same label
        topRow.join(grid.getColumnByKey("identifier1"), grid.getColumnByKey("identifier2"))
                .setComponent(new Label("Basic Information"));
        // group the other two columns in the same header row
        topRow.join(grid.getColumnByKey("identifier3"), grid.getColumnByKey("identifier4"))
                .setComponent(new Label("Address Information"));
    }
}

package be.juvo.paul.battleship.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Coordinate {

    private String row;
    private String _1;
    private String _2;
    private String _3;
    private String _4;
    private String _5;
    private String _6;
    private String _7;
    private String _8;
    private String _9;
    private String _10;
    private boolean designated;
    private boolean containsBoat;
    private String column;
    private List<Vector> neighbouringCells;
}

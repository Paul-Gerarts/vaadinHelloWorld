package be.juvo.paul.battleship.entities;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Vector {

    private String row;
    private int column;
}

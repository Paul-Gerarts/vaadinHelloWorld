package be.juvo.paul.battleship.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum Markings {

    HIT("X"),
    MISS("O"),
    BOAT("B"),
    BLANK("");

    private final String stringValue;
}

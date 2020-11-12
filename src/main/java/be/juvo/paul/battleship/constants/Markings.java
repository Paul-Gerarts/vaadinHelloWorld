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
    BLANK(""),
    NOT_AVAILABLE("N/A");

    private final String stringValue;
}

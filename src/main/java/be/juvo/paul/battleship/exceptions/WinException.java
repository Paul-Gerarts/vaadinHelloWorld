package be.juvo.paul.battleship.exceptions;

public class WinException extends Exception {

    public WinException(boolean computer) {
        super(computer ? "Alas, the computer won the game!" : "Congratulations, you won the game!");
    }
}

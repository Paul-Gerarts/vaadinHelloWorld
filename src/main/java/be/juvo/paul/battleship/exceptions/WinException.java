package be.juvo.paul.battleship.exceptions;

public class WinException extends Exception {

    public WinException(boolean myboats) {
        super(myboats ? "Congratulations, you won the game!" : "Alas, the computer won the game!");
    }
}

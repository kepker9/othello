package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

//This interface is needed so Piece array can store both normal pieces and dummy pieces
/**
 * Piece interface defines common behavior for all game pieces: visible real pieces and
 * dummy pieces used for minimax. This interface is needed so board class doesn't need
 * separate methods for every type of pieces.
 */
public interface Piece {
    Color getColor();
    void flip();
    void deleteFromPane(Pane pane);
}

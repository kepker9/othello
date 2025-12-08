package othello;

import javafx.scene.paint.Color;

//This interface is needed so Piece array can store both normal pieces and dummy pieces
public interface Piece {
    Color getColor();
    void flip();
}

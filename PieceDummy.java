package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * PieceDummy represents a dummy piece used during minimax simulations.
 */
public class PieceDummy implements Piece{
    private Color color;
    /**
     * Constructs a dummy piece with specified color.
     */
    public PieceDummy(Color color) {
        this.color = color;
    }
    public Color getColor() {return this.color;}
    /**
     * Flips dummy piece to opposite color without animation.
     */
    public void flip() {
        if (this.color==Color.WHITE){
            this.color = Color.BLACK;
        }
        else{
            this.color=Color.WHITE;
        }
    }
    /**
     * Empty method that just has to be in this class because Interface Piece has it.
     */
    public void deleteFromPane(Pane pane){}
}

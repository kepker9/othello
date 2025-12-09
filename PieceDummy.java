package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

//piece class that has no graphics
public class PieceDummy implements Piece{
    private Color color;

    public PieceDummy(Color color) {
        this.color = color;
    }

    public Color getColor() {return this.color;}

    public void flip() {
        if (this.color==Color.WHITE){
            this.color = Color.BLACK;
        }
        else{
            this.color=Color.WHITE;
        }
    }
    public void deleteFromPane(Pane pane){} //empty method because we don't need it
}

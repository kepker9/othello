package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
/**
 * Square class represents a single board tile in the othello grid. Squares can
 * change color to indicate possible moves or its border state.
 */
public class Square {
    private Rectangle body;
    private Color color;
    public Square(int x, int y, boolean borderSquare, Pane pane){
        this.createSquare( x,  y, borderSquare, pane);
    }
    /**
     * Constructs a square at specified pixel coordinates.
     */
    private void createSquare(int x, int y, boolean borderSquare, Pane pane){
        this.body = new Rectangle(x, y, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        if(borderSquare){
            this.color = Color.DARKSLATEGRAY;
            this.body.setFill(this.color);
        }
        else{
            this.color = Color.DARKGREEN;
            this.body.setFill(this.color);
        }
        this.body.setStroke(Color.BLACK);
        pane.getChildren().add(this.body);
    }
    public void setColor(Color color){
        this.body.setFill(color);
        this.color=color;
    }
    public Color getColor(){
        return this.color;
    }




}

package othello;


import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square {
    private Rectangle body;
    public Square(int x, int y, boolean boardSquare, Pane pane){
        this.createSquare( x,  y, boardSquare, pane);
    }
    private void createSquare(int x, int y, boolean boardSquare, Pane pane){
        this.body = new Rectangle(x, y, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        if(boardSquare){
            this.body.setFill(Color.DARKSLATEGRAY);
        }
        else{
            this.body.setFill(Color.DARKGREEN);
        }
        this.body.setStroke(Color.BLACK);
        pane.getChildren().add(this.body);
    }


}

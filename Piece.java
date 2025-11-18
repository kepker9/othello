package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Piece {
    private Circle body;
    private boolean isWhite;
    public Piece (int arrayX, int arrayY, boolean isWhite, Pane pane){
        this.isWhite = isWhite;
        this.createPiece(arrayX, arrayY, pane);
    }
    private void createPiece(int arrayX, int arrayY, Pane pane){
        if(this.isWhite){
            this.body = new Circle(Constants.PIECE_RADIUS, Color.WHITE);
        }
        else{
            this.body = new Circle(Constants.PIECE_RADIUS, Color.BLACK);
        }
        this.body.setCenterX(this.transformX(arrayX));
        this.body.setCenterY(this.transformY(arrayY));
        this.body.setStroke(Color.BLACK);
        pane.getChildren().add(this.body);

    }
    //takes array's x index as parameter and returns X coordinate of piece's center
    private int transformX(int arrayX){
        return arrayX*Constants.SQUARE_WIDTH + Constants.SQUARE_WIDTH/2;
    }
    private int transformY(int arrayY){
        return arrayY*Constants.SQUARE_WIDTH + Constants.SQUARE_WIDTH/2;
    }
}

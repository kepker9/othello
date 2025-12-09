package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
/**
 * Piece class represents a single othello piece on the board. Pieces can flip between
 * black and white when captured, updating the game score.
 */
public class PieceNormal implements Piece {
    private Circle body;
    private Color color;
    public PieceNormal(int arrayX, int arrayY, Color color, Pane pane){
        this.color = color;
        this.createPiece(arrayX, arrayY, pane);
    }
    /**
     * Constructs a visual circle.
     */
    private void createPiece(int arrayX, int arrayY, Pane pane){
        this.body = new Circle(Constants.PIECE_RADIUS, this.color);
        this.body.setCenterX(this.transformX(arrayX));
        this.body.setCenterY(this.transformY(arrayY));
        this.body.setStroke(Color.BLACK);
        pane.getChildren().add(this.body);
    }
    /**
     * Converts an x–index from the board array into the pixel coordinate for the center of the piece.
     */
    private int transformX(int arrayX){
        return arrayX*Constants.SQUARE_WIDTH + Constants.SQUARE_WIDTH/2;
    }
    /**
     * Converts an y–index from the board array into the pixel coordinate for the center of the piece.
     */
    private int transformY(int arrayY){
        return arrayY*Constants.SQUARE_WIDTH + Constants.SQUARE_WIDTH/2;
    }
    public Color getColor(){return this.color;}
    /**
     * Flips the piece to the opposite color and changes the score.
     */
    public void flip(){
        if (this.color==Color.WHITE){
            this.color = Color.BLACK;
            this.body.setFill(Color.BLACK);
            Game.blackScore++;
            Game.whiteScore--;
        }
        else{
            this.color = Color.WHITE;
            this.body.setFill(Color.WHITE);
            Game.whiteScore++;
            Game.blackScore--;
        }
    }
    public void deleteFromPane(Pane pane){
        pane.getChildren().remove(this.body);
    }
}

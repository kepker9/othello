package othello;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.util.Duration;

/**
 * Piece class represents a single othello piece on the board. Pieces can flip between
 * black and white when captured, updating the game score. Also does animation of flipping.
 */
public class PieceNormal implements Piece {
    private Ellipse body;
    private Color color;
    private Timeline timeline;
    private int animationFrame; //current frame in animation sequence (12 total frames)
    /**
     * Constructs a dummy piece with specified color and at specified board coordinates.
     */
    public PieceNormal(int arrayX, int arrayY, Color color, Pane pane){
        this.color = color;
        this.createPiece(arrayX, arrayY, pane);
        this.setupTimeline();
    }
    /**
     * Creates Ellipse that represents of piece and adds it to pane.
     */
     private void createPiece(int arrayX, int arrayY, Pane pane){
        this.body = new Ellipse(Constants.PIECE_RADIUS, Constants.PIECE_RADIUS);
        this.body.setCenterX(this.transformX(arrayX));
        this.body.setCenterY(this.transformY(arrayY));
        this.body.setFill(this.color);
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
     * Initiates piece flip process, changing this.colors and starting animation.
     */
    public void flip(){
        if (this.color==Color.WHITE){
            this.color = Color.BLACK;
            Game.blackScore++;
            Game.whiteScore--;
        }
        else{
            this.color = Color.WHITE;
            Game.whiteScore++;
            Game.blackScore--;
        }

        this.animationFrame = 1;
        this.timeline.play();
    }
    /**
     * Removes piece from pane.
     */
    public void deleteFromPane(Pane pane){
        pane.getChildren().remove(this.body);
    }
    /**
     * Sets up timeline for flip animation.
     * Animation consists of 12 frames: 6 shrinking, color change on the last
     * shrinking frame, 6 expanding.
     */
    private void setupTimeline() {
        KeyFrame kf = new KeyFrame(Duration.seconds(Constants.ANIMATION_TIME),
                (ActionEvent e) -> this.flipAnimate());
        this.timeline = new Timeline(kf);
        this.timeline.setCycleCount(12);
    }
    /**
     * Executes one frame of flip animation.
     */
    private void flipAnimate(){
        if(this.animationFrame<6){
            this.body.setRadiusX(this.body.getRadiusX()-3.5);
            this.animationFrame++;
        }
        else if (this.animationFrame==6){
            this.body.setFill(this.color);
            this.body.setRadiusX(this.body.getRadiusX()-3.5);
            this.animationFrame++;
        }
        else {
            this.body.setRadiusX(this.body.getRadiusX()+3.5);
            this.animationFrame++;
        }


    }

}

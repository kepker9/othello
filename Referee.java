package othello;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Referee controls turn-taking, checks for valid moves, tracks user's mouse clicks, places new pieces, and
 * calls move() methods.
 */
public class Referee {
    private boolean whiteTurn;
    private Board board;
    private Pane gamePane;
    private Player[] players;
    private Label scoreLabel;
    private Label turnLabel;
    private Timeline timeline;
    private Move pendingMove;

    /**
     * Constructor
     */
    public Referee(Board board, Pane pane, Label scoreLabel, Label turnLabel){
        this.board = board;
        this.gamePane = pane;

        this.scoreLabel = scoreLabel;
        this.turnLabel = turnLabel;

        this.whiteTurn = this.whiteGoesFirst();

        this.makeTimeline();
    }
    public void setPlayers (Player[] players){
        this.players = players;
        this.whiteTurn = !this.whiteTurn; //so it calcels out another switch of turns inside nextMove()
    }
    private boolean whiteGoesFirst(){
        //uncomment next two lines for random first turn generation. By default white goes first
        //int randomNum = (int)(Math.random()*2);
        //return randomNum==0;
        return true;
    }
    /**
     * Initiates the next turn by calling makeMove() on the current player whether it's human or computer.
     */
    public void nextMove(){
        this.whiteTurn = !this.whiteTurn;
        this.updateTurnLabel();
        if (this.checkForEndGame()) {
            return; //stop calling other players
        }
        if(this.whiteTurn){
            this.players[0].makeMove();
        }
        else{
            this.players[1].makeMove();
        }

    }
    /**
     * Converts a mouse click output into board coordinates and if the piece can be placed in the clicked
     * square by human player, places a piece and deactivates mouse at the clicked location.
     */
    private void handleClick(MouseEvent e){
        int x = (int)e.getX() / Constants.SQUARE_WIDTH;
        int y = (int)e.getY() / Constants.SQUARE_WIDTH;
        Move move = new Move(x, y);
        if (this.board.clickedLegalSquare(x, y, this.whiteTurn)) {
            this.deactivateMouse();
            this.addPiece(move);
        }
    }
    /**
     * Enables mouse click input on the game pane so a human player may select a square on their turn.
     */
    public void activateMouse(){
        this.gamePane.setOnMouseClicked(e -> handleClick(e));
    }
    /**
     * Disables mouse input.
     */
    private void deactivateMouse(){
        this.gamePane.setOnMouseClicked(null);
    }
    public void computerAddPiece(Move move){
        this.pendingMove = move;
        this.timeline.play();
    }
    private void waitAndAddPiece(){
        this.timeline.stop();
        this.board.clickedLegalSquare(this.pendingMove.getX(), this.pendingMove.getY(), this.whiteTurn);
        this.addPiece(this.pendingMove);
    }
    /**
     * Adds a new piece to the board at the given coordinates, switches turns, updates game labels,
     * and starts the next move.
     */
    private void addPiece(Move move){
        if (this.whiteTurn){
            this.board.addPiece(move.getX(), move.getY(), Color.WHITE, this.gamePane);
            Game.whiteScore++;
        }
        else{
            this.board.addPiece(move.getX(), move.getY(), Color.BLACK, this.gamePane);
            Game.blackScore++;
        }
        this.updateScoreLabel();
        this.nextMove();
    }
    /**
     * Updates the label that says whose turn to move
     */
    private void updateTurnLabel() {
        if (this.whiteTurn)
            this.turnLabel.setText("White to move");
        else
            this.turnLabel.setText("Black to move");
    }
    /**
     * Updates the score label
     */
    private void updateScoreLabel() {
        this.scoreLabel.setText("White: " + Game.whiteScore + " - Black: " + Game.blackScore);
    }
    private boolean playerHasMove(boolean isWhite) {
        this.board.updateLegalMoves(isWhite);
        return !this.board.getLegalMoves().isEmpty();
    }
    public boolean checkForEndGame(){
        boolean player1CanMove = this.playerHasMove(true);
        boolean player2CanMove = this.playerHasMove(false);
        if (!player1CanMove && !player2CanMove) {
            this.endGame();
            return true;
        }
        return false;
    }
    private void endGame(){
        String result;
        if (Game.whiteScore > Game.blackScore){
            result = "GAME OVER: White player wins!";
        }
        else if (Game.whiteScore < Game.blackScore){
            result = "GAME OVER: Black player wins!";
        }
        else {
            result = "GAME OVER: It's a tie!";
        }
        this.turnLabel.setText(result);
        //and stop everything
    }
    public boolean getTurn(){
        return this.whiteTurn;
    }
    private void makeTimeline(){
        KeyFrame kf = new KeyFrame(Duration.seconds(Constants.TIME_BETWEEN_COMPUTER_MOVES),
                (ActionEvent e) -> this.waitAndAddPiece());
        this.timeline = new Timeline(kf);
        this.timeline.setCycleCount(1);
    }
    //method for game reset since only referee has access to timeline and has update labels methods
    public void resetGame(){
        this.updateScoreLabel();
        this.turnLabel.setText("");
        this.timeline.stop();
        this.whiteTurn = this.whiteGoesFirst();
    }
}

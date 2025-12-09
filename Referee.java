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
     * Constructs a Referee with game components
     */
    public Referee(Board board, Pane pane, Label scoreLabel, Label turnLabel){
        this.board = board;
        this.gamePane = pane;

        this.scoreLabel = scoreLabel;
        this.turnLabel = turnLabel;

        this.whiteTurn = this.whiteGoesFirst();

        this.makeTimeline();
    }
    /**
     * Connects players to referee.
     */
    public void setPlayers (Player[] players){
        this.players = players;
        this.whiteTurn = !this.whiteTurn; //so it calcels out another switch of turns inside nextMove()
    }
    /**
     * Determines which player goes first. By default, for convenience, it's always white.
     * Randomization can be enabled by uncommenting two lines of code
     */
    private boolean whiteGoesFirst(){
        //uncomment next two lines for random first turn generation. By default white goes first
        //int randomNum = (int)(Math.random()*2);
        //return randomNum==0;
        return true;
    }
    /**
     * Switches players' turns and initiates their move. Checks for game end.
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
    /**
     * Schedules a computer move to be placed after a delay for visual effect.
     */
    public void computerAddPiece(Move move){
        this.pendingMove = move;
        this.timeline.play();
    }
    /**
     * Places computer move on board after delay
     */
    private void waitAndAddPiece(){
        this.timeline.stop();
        this.board.clickedLegalSquare(this.pendingMove.getX(), this.pendingMove.getY(), this.whiteTurn);
        this.addPiece(this.pendingMove);
    }
    /**
     * Adds a new piece to the board at the given coordinates, switches turns,
     * updates score label, and starts next player's move.
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
     * Updates the label that says whose turn is to move.
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
    /**
     * Checks if player has any legal moves available.
     */
    private boolean playerHasMove(boolean isWhite) {
        this.board.updateLegalMoves(isWhite);
        return !this.board.getLegalMoves().isEmpty();
    }
    /**
     * Checks if the game is over. If game is over, triggers end game sequence.
     */
    public boolean checkForEndGame(){
        boolean player1CanMove = this.playerHasMove(true);
        boolean player2CanMove = this.playerHasMove(false);
        if (!player1CanMove && !player2CanMove) {
            this.endGame();
            return true;
        }
        return false;
    }
    /**
     * Ends game. Determines winner and displays the result.
     */
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
    /**
     * Creates timeline for delaying computer moves.
     */
    private void makeTimeline(){
        KeyFrame kf = new KeyFrame(Duration.seconds(Constants.TIME_BETWEEN_COMPUTER_MOVES),
                (ActionEvent e) -> this.waitAndAddPiece());
        this.timeline = new Timeline(kf);
        this.timeline.setCycleCount(1);
    }
    /**
     * Resets game to initial state: stops timeline, updates labels, resets turn order.
     */
    public void resetGame(){
        this.updateScoreLabel();
        this.turnLabel.setText("");
        this.timeline.stop();
        this.whiteTurn = this.whiteGoesFirst();
    }
}

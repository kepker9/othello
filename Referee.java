package othello;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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
    /**
     * Constructor
     */
    public Referee(Player[] players, Board board, Pane pane, Label scoreLabel, Label turnLabel){
        this.board = board;
        this.gamePane = pane;
        this.players = players;

        this.scoreLabel = scoreLabel;
        this.turnLabel = turnLabel;

        this.whiteTurn = true; //temporarily white pieces go first, but later will add random turn
    }
    /**
     * Initiates the next turn by calling makeMove() on the current player whether it's human or computer.
     */
    public void nextMove(){
        this.checkForEndGame();
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
     * Adds a new piece to the board at the given coordinates, switches turns, updates game labels,
     * and starts the next move.
     */
    public void addPiece(Move move){
        if (this.whiteTurn){
            this.board.addPiece(move.getX(), move.getY(), Color.WHITE, this.gamePane);
            Game.whiteScore++;
        }
        else{
            this.board.addPiece(move.getX(), move.getY(), Color.BLACK, this.gamePane);
            Game.blackScore++;
        }
        this.updateScoreLabel();
        this.whiteTurn = !this.whiteTurn; //changing turn after adding piece and flipping pieces
        this.updateTurnLabel();
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
    private void checkForEndGame(){
        boolean player1CanMove = playerHasMove(true);
        boolean player2CanMove = playerHasMove(false);
        if (!player1CanMove && !player2CanMove) {
            this.endGame();
        }
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
        this.scoreLabel.setText(result);
        //and stop everything
    }
}

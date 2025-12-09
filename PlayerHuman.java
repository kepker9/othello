package othello;

import java.util.ArrayList;

/**
 * PlayerHuman represents a human-controlled player. On its turn, the human player is allowed to click
 * on the game board. Human player uses Referee methods for move validation and mouse tracking.
 */
public class PlayerHuman implements Player{
    private Referee referee;
    private boolean isWhite;
    private Board board;
    public PlayerHuman(boolean isWhite, Board board){
        this.isWhite = isWhite;
        this.board = board;
    }
    public void setReferee(Referee referee){this.referee = referee;}
    /**
     * This method starts player's turn.
     *
     * Highlights all possible squares to play and activates mouse input so the user may choose a square.
     */
    public void makeMove(){
        this.board.updateLegalMoves(this.isWhite);
        ArrayList<Move> moves = this.board.getLegalMoves();
        if (moves.isEmpty()) {//skip turn
            this.referee.nextMove();
            return;
        }
        this.board.highlightPossibleMoves(true);
        this.referee.activateMouse();
    }
}

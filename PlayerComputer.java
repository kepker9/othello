package othello;

import java.util.ArrayList;

/**
 * PlayerComputer represents AI players with different difficulty levels (1-3).
 * Uses minimax algorithm to determine optimal moves.
 */
public class PlayerComputer implements Player {
    private Referee referee;
    private int AILevel;
    private Board board;
    /**
     * Constructs computer player with specified AI level.
     */
    public PlayerComputer(int AILevel, Board board) {
        this.AILevel = AILevel;
        this.board = board;
    }
    public void setReferee(Referee referee) {
        this.referee = referee;
    }
    /**
     * Determines the best move using minimax algorithm.
     */
    private Move getBestMove(Board board, int intelligence, boolean isWhite) {
        if (board.isGameOver()) {
            Move fakeMove = new Move(-1, -1);
            int value = board.evaluateBoard();
            if(isWhite){
                if(value > 0){
                    fakeMove.setValue(Integer.MAX_VALUE/10);
                }
                else if(value < 0){
                    fakeMove.setValue(Integer.MIN_VALUE/10);
                }
                else{
                    fakeMove.setValue(0);
                }
            }else{
                if(value < 0){
                    fakeMove.setValue(Integer.MAX_VALUE/10);
                }
                else if(value > 0){
                    fakeMove.setValue(Integer.MIN_VALUE/10);
                }
                else{
                    fakeMove.setValue(0);
                }
            }
            return fakeMove;
        }

        board.updateLegalMoves(isWhite);
        ArrayList<Move> moves = board.getLegalMoves();

        if (moves.isEmpty()) {
            if (intelligence == 1) {
                int value = board.evaluateBoard();
                Move fakeMove = new Move(-1, -1);
                if (!isWhite) {
                    value = -value;
                }
                fakeMove.setValue(value);
                return fakeMove;
            }
            else {
                Board dummyBoard = new Board(board);
                Move reply = this.getBestMove(dummyBoard, intelligence - 1, !isWhite);
                Move fakeMove = new Move(-1, -1);
                fakeMove.setValue(-reply.getValue());
                return fakeMove;
            }
        }
        Move bestMove = moves.get(0); //just assign the first possible move to be the best move to begin
        int bestValue = Integer.MIN_VALUE;

        for (Move move:moves){
            Board dummyBoard = new Board(board);
            dummyBoard.addDummyPiece(move, isWhite);
            int value;
            if(intelligence==1){
                value = dummyBoard.evaluateBoard();
                if (!isWhite) {
                    value = -value;
                }
            }
            else{
                Move opponentBest = this.getBestMove(dummyBoard,
                        intelligence - 1, !isWhite);
                value = -opponentBest.getValue();
            }
            if(value > bestValue){
                bestValue=value;
                bestMove=move;
            }
        }
        bestMove.setValue(bestValue);
        return bestMove;
    }
    /**
     * Calculates best move using minimax method and plays this move.
     */
    public void makeMove() {
        boolean isWhite = this.referee.getTurn();
        this.board.updateLegalMoves(isWhite);
        ArrayList<Move> moves = this.board.getLegalMoves();

        if (moves.isEmpty()) {//skip turn
            this.referee.nextMove();
            return;
        }
        Move move = this.getBestMove(this.board, this.AILevel, isWhite);
        if (move == null || move.getX() < 1|| move.getY() < 1) {
            this.referee.nextMove();
            return;
        }
        this.referee.computerAddPiece(move);
    }
}

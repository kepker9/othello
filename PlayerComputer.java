package othello;

import java.util.ArrayList;

public class PlayerComputer implements Player {
    private Referee referee;
    private int AILevel;
    private Board board;
    private boolean isWhite;
    public PlayerComputer(int AILevel, Board board) {
        this.AILevel = AILevel;
        this.board = board;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }
    private Move getBestMove(Board board, int intelligence, boolean isWhite) {

        //if (board.isGameOver()) {
        //    int eval = board.evaluateBoard(isWhite);
        //}
        board.updateLegalMoves(isWhite);
        ArrayList<Move> moves = board.getLegalMoves();
        Move bestMove = moves.get(0); //just assign the first possible move to be the best move to begin
        int bestValue = Integer.MIN_VALUE;
        for (Move move:moves){
            Board dummyBoard = new Board(board);
            dummyBoard.addDummyPiece(move, isWhite);
            int value;
            if(intelligence==1){
                value = dummyBoard.evaluateBoard(isWhite);
            }
            else{
                value = -this.getBestMove(dummyBoard, intelligence-1, !isWhite).getValue();
            }
            //check if this value is bigger than previous move's value
            if(value>bestValue){
                bestValue=value;
                bestMove=move;
            }
        }
        bestMove.setValue(bestValue);
        return bestMove;
    }

    public void makeMove() {
        this.isWhite = this.referee.getTurn();
        this.board.updateLegalMoves(this.isWhite);
        ArrayList<Move> moves = this.board.getLegalMoves();

        if (moves.isEmpty()) {//skip turn
            this.referee.nextMove();
            return;
        }
        Move move = this.getBestMove(this.board, this.AILevel, this.isWhite);
        this.referee.computerAddPiece(move);



    }
}

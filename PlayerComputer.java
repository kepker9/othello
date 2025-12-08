package othello;


public class PlayerComputer implements Player{
    private Referee referee;
    private int AILevel;
    private Board board;
    public PlayerComputer(int AILevel, Board board){
        this.AILevel = AILevel;
        this.board = board;
    }

    public void setReferee(Referee referee){
        this.referee = referee;
    }


    public void makeMove() {
        System.out.println("Computer thinking...");

        int depth = this.AILevel;
        Board dummyBoard = new Board(this.board);

        //int[] best = this.getBestMove(dummyBoard, depth, this.referee.isWhiteTurn());

        //this.referee.addPiece(best[0], best[2]);
    }

}

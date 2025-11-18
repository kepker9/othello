package othello;

public class Referee {
    private boolean whiteTurn;
    private Board board;
    public Referee(Board board){
        this.board = board;
        this.whiteTurn = true; //temporarily white pieces go first, but later will add random turn

    }
    public void handleSquareClick(){
        //add pieces if can
    }


}

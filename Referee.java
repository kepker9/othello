package othello;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Referee {
    private boolean whiteTurn;
    private Board board;
    private Pane gamePane;
    private Player[] players;
    public Referee(Player[] players, Board board, Pane pane){
        this.board = board;
        this.gamePane = pane;
        this.players = players;

        this.whiteTurn = true; //temporarily white pieces go first, but later will add random turn

    }
    public void nextMove(){
        if(this.whiteTurn){
            this.players[0].makeMove();
        }
        else{
            this.players[1].makeMove();
        }
    }
    public void activateMouse(){
        this.gamePane.setOnMouseClicked(e -> handleClick(e));
    }
    public void deavtivateMouse(){
        this.gamePane.setOnMouseClicked(null);
    }
    private void handleClick(MouseEvent e){
        int boardX = (int)e.getX() / Constants.SQUARE_WIDTH;
        int boardY = (int)e.getY() / Constants.SQUARE_WIDTH;
        this.deavtivateMouse();
        this.addPiece(boardX, boardY);
    }

    public void addPiece(int x, int y){
        this.board.getPiecesArray()[x][y] = new Piece(x, y, this.whiteTurn, this.gamePane);
        //flip "sandwiched" pieces
        this.whiteTurn = !this.whiteTurn; //changing turn after adding piece and flipping pieces
        this.nextMove();
    }



}

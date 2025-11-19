package othello;

import javafx.scene.layout.Pane;

public class Board {
    private Square[][] squares;
    private Piece[][] pieces;
    public Board(Pane pane){
        this.squares = new Square[10][10]; //column first then row; x first then y
        this.pieces = new Piece[10][10]; //indexes 0 and 9 are always empty
        this.generateBoard(pane);
        this.setInitialPieces(pane);


    }
    private void generateBoard(Pane pane){
        for (int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++){
                if (x==0 || y==0 || x==9 || y==9){
                    //borderSquare is true because these squares are just for visuals.
                    //user cannot put pieces on them
                    this.squares[x][y] = new Square(x*Constants.SQUARE_WIDTH, y*Constants.SQUARE_WIDTH,
                            true, pane);
                }
                else{
                    this.squares[x][y] = new Square(x*Constants.SQUARE_WIDTH, y*Constants.SQUARE_WIDTH,
                            false, pane);
                }

            }
        }
    }
    private void setInitialPieces(Pane pane){
        this.pieces[4][4] = new Piece(4, 4, true, pane);
        this.pieces[5][5] = new Piece(5, 5, true, pane);
        this.pieces[4][5] = new Piece(4, 5, false, pane);
        this.pieces[5][4] = new Piece(5, 4, false, pane);
    }
    public Piece[][] getPiecesArray(){return this.pieces;}
}

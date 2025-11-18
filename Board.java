package othello;

import javafx.scene.layout.Pane;

public class Board {
    private Square[][] squares;
    private Piece[][] pieces;
    private Referee referee;
    public Board(Pane pane){
        this.squares = new Square[10][10]; //column first then row; x first then y
        this.pieces = new Piece[10][10]; //indexes 0 and 9 are always empty
        this.generateBoard(pane);
        this.setInitialPieces(pane);

        this.referee = new Referee(this);
    }
    private void generateBoard(Pane pane){
        for (int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++){
                if (x==0 || y==0 || x==9 || y==9){
                    this.squares[x][y] = new Square(x*Constants.SQUARE_WIDTH, y*Constants.SQUARE_WIDTH,
                            true, pane);
                }
                else{

                    this.squares[x][y] = new Square(x*Constants.SQUARE_WIDTH, y*Constants.SQUARE_WIDTH,
                            false, pane);
                    int X = x;
                    int Y = y; //need this because handleSquareClick() doesn't want to accept non-final x and y
                    this.squares[x][y].getBody().setOnMouseClicked(e -> handleSquareClick(X, Y));
                }

            }
        }
    }
    public void handleSquareClick(int x, int y) {
        System.out.println("Board clicked: " + x + ", " + y);
        this.referee.handleSquareClick();
    }
    private void setInitialPieces(Pane pane){
        this.pieces[4][4] = new Piece(4, 4, true, pane);
        this.pieces[5][5] = new Piece(5, 5, true, pane);
        this.pieces[4][5] = new Piece(4, 5, false, pane);
        this.pieces[5][4] = new Piece(5, 4, false, pane);
    }
}

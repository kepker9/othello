package othello;

import javafx.scene.layout.Pane;

public class Board {
    private Square[][] squares;
    public Board(Pane pane){
        this.squares = new Square[10][10]; //column first then row; x first then y
        this.generateBoard(pane);
    }
    private void generateBoard(Pane pane){
        boolean borderSquare;
        for (int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++){
                if (x==0 || y==0 || x==9 || y==9){
                    borderSquare = true;
                }
                else{
                    borderSquare = false;
                }
                this.squares[x][y] = new Square(x*Constants.SQUARE_WIDTH, y*Constants.SQUARE_WIDTH,
                        borderSquare, pane);
            }
        }
    }
}

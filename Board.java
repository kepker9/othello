package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
/**
 * The Board class manages the full game grid, including both the visual squares and the logical piece positions.
 * It sets the starting layout, validates moves, detects flippable lines, performs flips, and
 * highlights legal moves on the board.
 */
public class Board {
    private Square[][] squares;
    private Piece[][] pieces;
    public Board(Pane pane){
        this.squares = new Square[10][10]; //column first then row; x first then y
        this.pieces = new Piece[10][10]; //indexes 0 and 9 are always empty
        this.generateBoard(pane);
        this.setInitialPieces(pane);
    }
    /**
     * Generates all visual Square objects on the board.
     */
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
    /**
     * Places the initial four pieces
     */
    private void setInitialPieces(Pane pane){
        this.pieces[4][4] = new Piece(4, 4, Color.WHITE, pane);
        this.pieces[5][5] = new Piece(5, 5, Color.WHITE, pane);
        this.pieces[4][5] = new Piece(4, 5, Color.BLACK, pane);
        this.pieces[5][4] = new Piece(5, 4, Color.BLACK, pane);
    }
    public Piece[][] getPiecesArray(){return this.pieces;}
    /**
     * Checks whether a move may be played at the given coordinates. Verifies that the square is empty,
     * not on the border, and that playing here would flip at least one opponent piece.
     */
    public boolean moveLegal(int x, int y, boolean isWhiteTurn){
        if (x==0 || y==0 || x==9 || y==9){
            return false;
        }
        else if(this.pieces[x][y]!= null){
            return false;
        }
        else{
            return this.checkIfSandwich(x, y, isWhiteTurn);
        }
    }
    /**
     * Checks if placing a piece at the given coordinates creates a “sandwich” in at least one direction.
     */
    private boolean checkIfSandwich(int x, int y, boolean isWhiteTurn){
        Color color;
        if (isWhiteTurn){
            color = Color.WHITE;
        }
        else{
            color = Color.BLACK;
        }
        for (int deltaX = -1; deltaX <=1; deltaX++){
            for (int deltaY = -1; deltaY <=1; deltaY++){
                if (deltaX ==0 && deltaY == 0){
                    continue;
                }
                if (this.checkOneDirection(x+deltaX, y+deltaY, deltaX, deltaY,
                        color, false, false)){
                    return true;
                }
            }
        }

        return false;
    }
    /**
     * Recursively searches along a set direction to check if this row, column, or diagonal can be "sandwiched"
     *
     * This function also has a flippingMode that can flip pieces while searching if enabled.
     */
    private boolean checkOneDirection(int nextX, int nextY, int deltaX, int deltaY,
                                      Color playerColor, boolean seenOpponent, boolean flippingMode){
        if (nextX==0 || nextY==0 || nextX==9 || nextY==9 || this.pieces[nextX][nextY]==null){
            return false;
        }
        else if(this.pieces[nextX][nextY].getColor() != playerColor){
            if(flippingMode){
                this.pieces[nextX][nextY].flip();
            }
            return this.checkOneDirection(nextX + deltaX, nextY+deltaY, deltaX, deltaY,
                    playerColor, true, flippingMode);
        }
        else if(this.pieces[nextX][nextY].getColor() == playerColor && seenOpponent){
            return true;
        }
        else{
            return false;
        }
    }
    /**
     * Flips pieces in all 8 directions if possible
     */
    private void flipPieces(int x, int y, Color color){
        for (int deltaX = -1; deltaX <=1; deltaX++){
            for (int deltaY = -1; deltaY <=1; deltaY++){
                if (deltaX ==0 && deltaY == 0){
                    continue;
                }
                //checking if this direction should be flipped
                boolean validDirection = this.checkOneDirection(x + deltaX, y + deltaY,
                        deltaX, deltaY, color, false, false);
                //if this direction should be flipped - launch same method again but in flipping mode
                if(validDirection){
                    this.checkOneDirection(x + deltaX, y + deltaY,
                            deltaX, deltaY, color, false, true);
                }
            }
        }
    }
    /**
     * Highlights all legal move squares for the current player
     */
    public void highlightPossibleMoves(boolean isWhiteTurn){
        for (int x = 1; x<10; x++){
            for (int y = 1; y<10; y++){
                if (this.moveLegal(x, y, isWhiteTurn)){
                    this.squares[x][y].setColor(Color.LIME);
                }
            }
        }
    }
    /**
     * Handles the action of clicking a highlighted square. If the square is a valid move, it resets all
     * square colors to default dark green color and returns true. Otherwise, rejects the click and returns false.
     */
    public boolean clickedHighlightedSquare(int squareX, int squareY, boolean whiteTurn){
        if (this.squares[squareX][squareY].getColor()==Color.LIME){
            for (int x = 1; x<10; x++){
                for (int y = 1; y<10; y++){
                    this.squares[x][y].setColor(Color.DARKGREEN);
                }
            }
            Color color;
            if(whiteTurn){
                color = Color.WHITE;
            }
            else{
                color = Color.BLACK;
            }
            this.flipPieces(squareX,squareY, color);

            return true;
        }
        else{
            return false;
        }
    }
}

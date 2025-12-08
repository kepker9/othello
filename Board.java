package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * The Board class manages the full game grid, including both the visual squares and the logical piece positions.
 * It sets the starting layout, validates moves, detects flippable lines, performs flips, and
 * highlights legal moves on the board.
 */
public class Board {
    private Square[][] squares;
    private Piece[][] pieces;
    private ArrayList<Move> legalMoves;

    public Board(Pane pane){
        this.squares = new Square[10][10]; //column first then row; x first then y
        this.pieces = new PieceNormal[10][10]; //indexes 0 and 9 are always empty
        this.legalMoves = new ArrayList<>();
        this.generateBoard(pane);
        this.setInitialPieces(pane);

        ArrayList<int[]> legalMovesList = new ArrayList<>();
    }
    public Board(Board original) {
        this.pieces = new PieceDummy[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (original.pieces[x][y] != null) {
                    Color color = original.pieces[x][y].getColor();
                    this.pieces[x][y] = new PieceDummy(color);
                }
            }
        }
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
        this.pieces[4][4] = new PieceNormal(4, 4, Color.WHITE, pane);
        this.pieces[5][5] = new PieceNormal(5, 5, Color.WHITE, pane);
        this.pieces[4][5] = new PieceNormal(4, 5, Color.BLACK, pane);
        this.pieces[5][4] = new PieceNormal(5, 4, Color.BLACK, pane);
    }
    public void addPiece(int x, int y, Color color, Pane gamePane){
        this.pieces[x][y] = new PieceNormal(x, y, color, gamePane);
    }
    public void addDummyPiece(Move move, boolean isWhite){
        Color color;
        if(isWhite){
            color = Color.WHITE;
        }
        else{
            color = Color.BLACK;
        }
        this.pieces[move.getX()][move.getY()] = new PieceDummy(color);
    }
    /**
     * Checks whether a move may be played at the given coordinates. Verifies that the square is empty,
     * not on the border, and that playing here would flip at least one opponent piece.
     */
    private boolean moveLegal(Move move, boolean isWhiteTurn){
        int x = move.getX();
        int y = move.getY();
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
    public void highlightPossibleMoves(){
        for (Move move:this.legalMoves){
            int x = move.getX();
            int y = move.getY();
            this.squares[x][y].setColor(Color.LIME);
        }
    }
    /**
     * Handles the action of clicking a highlighted square. If the square is a valid move, it resets all
     * square colors to default dark green color and returns true. Otherwise, rejects the click and returns false.
     */
    public boolean clickedLegalSquare(int squareX, int squareY, boolean whiteTurn){
        for (Move move : this.legalMoves) {
            if (move.getX() == squareX && move.getY() == squareY) {
                for (Move move2:this.legalMoves){
                    int x = move2.getX();
                    int y = move2.getY();
                    this.squares[x][y].setColor(Color.DARKGREEN);
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
        }
        return false;

    }
    public int evaluateBoard(boolean whiteTurn) {
        int score = 0;
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 8; y++) {
                Piece piece = this.pieces[x][y];
                if (piece == null){
                    continue;
                }
                int w = Constants.WEIGHTS[x-1][y-1];

                if (whiteTurn) {
                    if (piece.getColor() == Color.WHITE) {
                        score = score + w;
                    } else {
                        score = score - w;
                    }
                } else {
                    if (piece.getColor() == Color.BLACK) {
                        score = score + w;
                    } else {
                        score = score - w;
                    }
                }
            }
        }
        return score;
    }
    public void updateLegalMoves(boolean isWhite) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int x = 1; x <=8; x++) {
            for (int y = 1; y <= 8; y++) {
                Move move = new Move(x,y);
                if (this.moveLegal(move, isWhite)) {
                    moves.add(move);
                }
            }
        }
        this.legalMoves = moves;
    }
    public ArrayList<Move> getLegalMoves(){
        return this.legalMoves;
    }

}

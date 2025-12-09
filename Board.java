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
    private Move previousMove; //for bells and whistles highlighting the previous move
    /**
     * Constructs a new board with starting pieces and instantiates required arrays and lists.
     */
    public Board(Pane pane){
        this.squares = new Square[10][10]; //column first then row; x first then y
        this.pieces = new PieceNormal[10][10]; //indexes 0 and 9 are always empty
        this.legalMoves = new ArrayList<>();
        this.generateBoard(pane);
        this.setInitialPieces(pane);
        //assigning a square for previousMove that definitely will not be
        //used for the first move of the game (corner)
        this.previousMove = new Move(1,1);
    }
    /**
     * Creates a copy of an existing board for the minimax simulation of computer moves.
     * Uses PieceDummy objects to avoid graphical appearance on the pane during minimax calculations.
     */
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
                    this.squares[x][y] = new Square(x*Constants.SQUARE_WIDTH,
                            y*Constants.SQUARE_WIDTH, true, pane);
                }
                else{
                    this.squares[x][y] = new Square(x*Constants.SQUARE_WIDTH,
                            y*Constants.SQUARE_WIDTH, false, pane);
                }
            }
        }
    }
    /**
     * Places the initial four othello pieces
     */
    private void setInitialPieces(Pane pane){
        this.pieces[4][4] = new PieceNormal(4, 4, Color.WHITE, pane);
        this.pieces[5][5] = new PieceNormal(5, 5, Color.WHITE, pane);
        this.pieces[4][5] = new PieceNormal(4, 5, Color.BLACK, pane);
        this.pieces[5][4] = new PieceNormal(5, 4, Color.BLACK, pane);
    }
    /**
     * Adds a visible piece to the board during normal gameplay.
     */
    public void addPiece(int x, int y, Color color, Pane gamePane){
        this.pieces[x][y] = new PieceNormal(x, y, color, gamePane);
    }
    /**
     * Adds a dummy piece and flips opponent pieces during minimax calculations without visual updates.
     */
    public void addDummyPiece(Move move, boolean isWhite){
        //flip dummy pieces and then add a piece
        int x = move.getX();
        int y = move.getY();
        Color color;
        if(isWhite){
            color = Color.WHITE;
        }
        else{
            color = Color.BLACK;
        }
        this.flipPieces(x, y, color);
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
     * Checks if placing a piece at the given coordinates creates a “sandwich” in any direction.
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
        if (nextX<1 || nextY<1 || nextX>8 || nextY>8 || this.pieces[nextX][nextY]==null){
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
     * Flips pieces in all 8 directions if possible.
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
     * Highlights or unhighlights squares from legal moves.
     */
    public void highlightPossibleMoves(boolean highlight){
        Color color;
        if(highlight){
            color = Color.LIME;
        }
        else{
            color = Color.DARKGREEN;
        }
        for (Move move:this.legalMoves){
            int x = move.getX();
            int y = move.getY();
            this.squares[x][y].setColor(color);
        }
    }
    /**
     * Processes a click on a game square. If square has a legal move, puts there a
     * piece and flips captured pieces.
     */
    //this method also flips all the pieces!
    public boolean clickedLegalSquare(int squareX, int squareY, boolean whiteTurn){
        for (Move move : this.legalMoves) {
            if (move.getX() == squareX && move.getY() == squareY) {
                this.highlightPossibleMoves(false);
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
    /**
     * Evaluates board state for minimax.
     * Positive values mean white is winning, negative - black.
     */
    public int evaluateBoard(){
        int score = 0;
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 8; y++) {
                Piece piece = this.pieces[x][y];
                if (piece == null){
                    continue;
                }
                int w = Constants.WEIGHTS[x-1][y-1];

                //positive - good for white, negative - good for Black
                if (piece.getColor() == Color.WHITE) {
                    score = score + w;
                } else {
                    score = score - w;
                }
            }
        }
        return score;
    }
    /**
     * Updates list of legal moves.
     */
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
    /**
     * Checks if the game is over by checking if neither player can move.
     */
    public boolean isGameOver() {
        this.updateLegalMoves(true);
        boolean whiteCanMove = !this.getLegalMoves().isEmpty();
        this.updateLegalMoves(false);
        boolean blackCanMove = !this.getLegalMoves().isEmpty();
        return !whiteCanMove && !blackCanMove;
    }
    /**
     * Resets board to initial state.
     */
    public void resetBoard(Pane pane) {
        this.highlightPossibleMoves(false);
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 8; y++) {
                if(this.pieces[x][y]!=null){
                    this.pieces[x][y].deleteFromPane(pane);
                }
                this.pieces[x][y]=null;
            }
        }
        this.setInitialPieces(pane);
        //unhighlight the previous move
        this.squares[this.previousMove.getX()][this.previousMove.getY()].setColor(Color.DARKGREEN);
        this.previousMove = new Move (1,1);
    }
    /**
     * Highlights the move that was just made and unhighlights a move
     * that was made before
     */
    public void highlightPreviousMove(Move previousMove){
        this.squares[this.previousMove.getX()][this.previousMove.getY()].setColor(Color.DARKGREEN);
        this.squares[previousMove.getX()][previousMove.getY()].setColor(Color.MEDIUMPURPLE);
        this.previousMove = previousMove;
    }
}

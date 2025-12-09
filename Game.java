package othello;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;


public class Game {
    private Board board;
    private Player[] players; //white player - player 0, black player - player 1
    private Referee referee;
    private Pane gamePane;
    public static int whiteScore = 2;
    public static int blackScore = 2;
    private Label scoreLabel;
    private Label turnLabel;
    public Game(Pane gamePane){
        this.board = new Board(gamePane);
        this.gamePane = gamePane;
        this.players = new Player[2];

        this.createLabels();
        this.referee = new Referee(this.board, this.gamePane, this.scoreLabel, this.turnLabel);


    }
    /**
     * Instantiates two players according to the selected modes (human or some level of AI).
     * Creates the Referee, sets Referee for each player, and starts the first move
     */
    public void applySettings(int whitePlayerMode, int blackPlayerMode){
        this.board.highlightPossibleMoves(false);
        switch (whitePlayerMode){
            case 0:
                this.players[0] = new PlayerHuman(true, this.board);
                break;
            case 1:
                this.players[0] = new PlayerComputer(1, this.board);
                break;
            case 2:
                this.players[0] = new PlayerComputer(2, this.board);
                break;
            default:
                this.players[0] = new PlayerComputer(3, this.board);
                break;
        }
        //this code is slightly duplicated because we need to have only one referee for both players
        switch (blackPlayerMode){
            case 0:
                this.players[1] = new PlayerHuman(false, this.board);
                break;
            case 1:
                this.players[1] = new PlayerComputer(1, this.board);
                break;
            case 2:
                this.players[1] = new PlayerComputer(2, this.board);
                break;
            default:
                this.players[1] = new PlayerComputer(3, this.board);
                break;
        }
        this.referee.setPlayers(this.players);
        this.players[0].setReferee(this.referee);
        this.players[1].setReferee(this.referee);
        this.referee.nextMove();
    }
    /**
     * Creates the score and turn labels
     */
    private void createLabels() {
        this.scoreLabel = new Label("White: " + whiteScore + " - Black: " + blackScore);
        this.turnLabel = new Label("");
    }
    public Label getScoreLabel() { return this.scoreLabel; }
    public Label getTurnLabel() { return this.turnLabel; }
    public void resetGame(){
        blackScore = 2;
        whiteScore = 2;
        this.referee.resetGame();
        this.board.resetBoard(this.gamePane);
        this.players[0] = null;
        this.players[1] = null;

    }


}

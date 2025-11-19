package othello;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Game {
    private Board board;
    private Player[] players; //white player - player 0, black player - player 1
    private Referee referee;
    private Pane gamePane;
    public Game(BorderPane rootPane, Pane gamePane){
        this.board = new Board(gamePane);
        this.gamePane = gamePane;
        this.setUpControls(rootPane);
        this.players = new Player[2];
    }
    private void setUpControls(BorderPane rootPane){
        Controls controls = new Controls(this);
        rootPane.setRight(controls.getPane());
    }
    public void setUpPlayers(int whitePlayerMode, int blackPlayerMode){
        switch (whitePlayerMode){
            case 0:
                this.players[0] = new PlayerHuman();
                break;
            case 1:
                this.players[0] = new PlayerComputer();
                break;
            default:
                //make another levels of computer AI
                break;
        }
        //this code is slightly duplicated because we need to have only one referee for both players
        switch (blackPlayerMode){
            case 0:
                this.players[1] = new PlayerHuman();
                break;
            case 1:
                this.players[1] = new PlayerComputer();
                break;
            default:
                //make another levels of computer AI
                break;
        }
        this.referee = new Referee(this.players, this.board, this.gamePane);
        this.players[0].setReferee(this.referee);
        this.players[1].setReferee(this.referee);
        this.referee.nextMove();

    }


}

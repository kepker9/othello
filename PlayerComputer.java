package othello;

import javafx.scene.layout.Pane;

public class PlayerComputer implements Player{
    private Referee referee;
    public PlayerComputer(){

    }

    public void setReferee(Referee referee){this.referee = referee;}

    public void makeMove(){
        System.out.println("computer move");
        this.referee.addPiece(0, 0);  //just a fake method to test
    }
}

package othello;

import javafx.scene.layout.Pane;


public class PlayerHuman implements Player{
    private Referee referee;
    public PlayerHuman(){

    }

    public void setReferee(Referee referee){this.referee = referee;}

    public void makeMove(){
        this.referee.activateMouse();

    }


}

package othello;

import javafx.scene.layout.Pane;

public class Game {
    private Board board;

    private Referee referee;
    public Game(Pane pane){
        this.board = new Board(pane);



    }

}

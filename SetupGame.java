package othello;

import javafx.scene.layout.Pane;

public class SetupGame {
    private Board board;
    public SetupGame(Pane pane){
        this.board = new Board(pane);
    }
}

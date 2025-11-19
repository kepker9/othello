package othello;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class PaneOrganizer {
    private BorderPane root;
    private Pane gamePane;
    public PaneOrganizer(){
        this.root = new BorderPane();
        this.createGamePane();

        new Game(this.root, this.gamePane);

    }
    public void createGamePane(){
        this.gamePane = new Pane();
        this.root.setCenter(this.gamePane);
    }
    public Pane getRoot(){return this.root;}
}

package othello;

public class Constants {

    public static final int WHITE = 0;
    public static final int BLACK = 1;
    public static final int CONTROLS_PANE_WIDTH = 400;

    public static int SQUARE_WIDTH = 50;
    public static int PIECE_RADIUS = 21;

    public static int SCENE_HEIGHT = SQUARE_WIDTH*10;
    public static int SCENE_WIDTH = SCENE_HEIGHT+CONTROLS_PANE_WIDTH;

    public static double TIME_BETWEEN_COMPUTER_MOVES = 0.6;

    public static final int[][] WEIGHTS = {
            {200,-70, 30, 25, 25, 30,-70,200},
            {-70,-100,-10,-10,-10,-10,-100,-70},
            {30,-10,2,2,2,2,-10,30},
            {25,-10,2,2,2,2,-10,25},
            {25,-10,2,2,2,2,-10,25},
            {30,-10,2,2,2,2,-10,30},
            {-70,-100,-10,-10,-10,-10,-100,-70},
            {200,-70,30,25,25,30,-70,200}
    };


}

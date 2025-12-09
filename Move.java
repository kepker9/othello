package othello;
/**
 * Move represents a potential or actual play position on the othello board.
 * Also has a value that is used by minimax.
 */
public class Move {
    private int x;
    private int y;
    private int value;
    /**
     * Constructs a Move
     */
    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public void setValue(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}

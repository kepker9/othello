package othello;

public class Move {
    private int x;
    private int y;
    private int value;
    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Move(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
}

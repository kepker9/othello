package othello;

/**
 * Player interface ensures consistent player behavior regardless of its type.
 */
public interface Player {
    void makeMove();
    void setReferee(Referee referee);

}

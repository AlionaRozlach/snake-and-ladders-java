package SnakeAndLadders;

public class Ladder {
    private int up;
    private int down;
    private TileState state;

    public Ladder(TileState state)
    {
        this.state = state;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getUp()
    {
        return up;
    }

    public int getDown()
    {
        return down;
    }

}

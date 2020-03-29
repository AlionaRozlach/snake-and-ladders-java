package SnakeAndLadders;

public class Tile {
    private TileState state ;
    private int num;
    public Tile( TileState state)
    {
        this.state = state;

    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public int getNum()
    {
        return this.num;
    }


    public TileState getState() {
        return state;
    }

    void setState(TileState state) {
        this.state = state;
    }

}

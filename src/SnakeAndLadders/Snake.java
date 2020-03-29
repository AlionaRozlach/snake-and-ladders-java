package SnakeAndLadders;

public class Snake {

    private int head;
    private int tile;
    private TileState state;

    public Snake(TileState state)
    {
        this.state = state;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public void setTile(int tile) {
        this.tile = tile;
    }

    public int getHead()
    {
        return head;
    }

    public int getTile()
    {
        return tile;
    }

    public TileState getState() {
        return state;
    }
}

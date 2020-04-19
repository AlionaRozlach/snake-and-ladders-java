package sk.tuke.gamestudio.game.snakeAndLad.core;

public class Tile {
    private TileType state;
    private int num;

    public Tile(TileType state) {
        this.state = state;

    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return this.num;
    }


    public TileType getState() {
        return state;
    }

    void setState(TileType state) {
        this.state = state;
    }

}

package SnakeAndLadders;

import java.util.*;

import static java.lang.System.exit;
import static java.lang.System.setOut;

public class Field {

    public static final String GAME_NAME = "S&L";
    private final int rowCount;
    private final int columnCount;
    private final Tile[][] tiles;
    private int count = 0;

    Map<Player,Integer> playerPos;
    private Random random = new Random();


    public Field(int rowCount, int columnCount,List<Player> players) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.tiles = new Tile[rowCount][columnCount];

        this.playerPos = new HashMap<Player,Integer>();

        for (int i=0;i<players.size();i++)
        {
            this.playerPos.put(players.get(i),1);
        }
        generate();
    }


    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    private void generate() {
        generateField();
    }

    private void generateField() {

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (count % 2 == 0) {
                    int k = (((j + 1) + (i * columnCount)));
                    this.tiles[i][j] = new Tile(TileState.FREE);
                    this.tiles[i][j].setNum(k);
                } else {
                    int k = (((i + 1) * columnCount) - (j + 1) + 1);
                    this.tiles[i][j] = new Tile(TileState.FREE);
                    this.tiles[i][j].setNum(k);
                }

            }
            count++;
        }

        lightLevel();
    }

    public void printTile(int i, int j) {
                switch (tiles[i][j].getState()) {
                    case FREE:
                        System.out.print('.' + "\t");
                        break;
                    case SNAKEHEAD:
                        System.out.print('X' + "\t");
                        break;
                    case SNAKETILE:
                        System.out.print('S' + "\t");
                        break;
                    case LADDERUP:
                        System.out.print('H' + "\t");
                        break;
                    case LADDERDOWN:
                        System.out.print('^' + "\t");
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported tile state " + tiles[i][j].getState());

        }
    }

    private boolean snakeHead(int i,int j, Player player)
    {
        if (tiles[i][j].getState() == TileState.SNAKEHEAD) {
            for (int k = i - 1; k >= 0; k--) {
                for (int m = 5; m >= 0; m--) {
                    if (tiles[k][m].getState() == TileState.SNAKETILE) {
                        player.setPosition(tiles[k][m].getNum());
                        playerPos.put(player,tiles[k][m].getNum());
                        System.out.println("You were eaten by a snake, your position: " + tiles[k][m].getNum());
                        return false;
                    }
                }
            }
        }
        return false;
    }

    private boolean ladderDown(int i, int j, Player player)
    {
        if (tiles[i][j].getState() == TileState.LADDERDOWN) {
            for (int k = i + 1; k <= 5; k++) {
                for (int m = 5; m >= 0; m--) {
                    if (tiles[k][m].getState() == TileState.LADDERUP) {
                        playerPos.put(player,tiles[k][m].getNum());
                        player.setPosition(tiles[k][m].getNum());
                        System.out.println("Congratulations, you climbed a ladder, your position: " + tiles[k][m].getNum());
                        return false;
                    }
                }
            }
            return false;
        }
        return false;
    }


    public boolean move(Player player, int valueDice) {

        int position = playerPos.get(player);
        position += valueDice;


        System.out.println("Now your position number: " + position );


        if (position == 100) {
            playerPos.put(player,100);
            player.setPosition(position);
            System.out.println(player.getName()+" "+ "WINNER!");
            return true;
        }

        if (position > 100) {
            int k = position - valueDice;
            playerPos.put(player,k);
            player.setPosition(k);
            System.out.println("A number greater than 100 has fallen on the dice, now your position: " + playerPos.get(player));
            return false;
        } else {
            for (int i = 0; i < rowCount; i++) {

                for (int j = 0; j < columnCount; j++) {
                    if (tiles[i][j].getNum() == position && tiles[i][j].getState() != TileState.FREE && tiles[i][j].getState() != TileState.SNAKETILE && tiles[i][j].getState() != TileState.LADDERUP) {

                       snakeHead(i,j,player);
                       ladderDown(i,j,player);

                    } else if (tiles[i][j].getNum() == position && tiles[i][j].getState() != TileState.SNAKEHEAD && tiles[i][j].getState() != TileState.LADDERDOWN) {
                        playerPos.put(player,position);
                        player.setPosition(position);
                        return false;
                    }
                }
            }
            return false;
        }
    }


    public void lightLevel() {
        tiles[0][3].setState(TileState.SNAKETILE);//1
        tiles[2][4].setState(TileState.SNAKEHEAD);//1
        tiles[3][5].setState(TileState.SNAKETILE);//2
        tiles[4][1].setState(TileState.SNAKEHEAD);//2

        tiles[0][1].setState(TileState.LADDERDOWN);//1
        tiles[3][1].setState(TileState.LADDERUP);//1
        tiles[0][4].setState(TileState.LADDERDOWN);//2
        tiles[2][2].setState(TileState.LADDERUP);//2
        tiles[2][5].setState(TileState.LADDERDOWN);//3
        tiles[4][5].setState(TileState.LADDERUP);//3
        tiles[3][4].setState(TileState.LADDERDOWN);//4
        tiles[5][0].setState(TileState.LADDERUP);//4

    }

    public void heavyLevel()
    {
        tiles[0][3].setState(TileState.SNAKETILE);//1
        tiles[2][4].setState(TileState.SNAKEHEAD);//1
        tiles[3][5].setState(TileState.SNAKETILE);//2
        tiles[4][1].setState(TileState.SNAKEHEAD);//2
        tiles[0][1].setState(TileState.SNAKETILE);//1
        tiles[3][1].setState(TileState.SNAKEHEAD);//1
        tiles[2][5].setState(TileState.SNAKETILE);//3
        tiles[4][5].setState(TileState.SNAKEHEAD);//3


        tiles[0][4].setState(TileState.LADDERDOWN);//2
        tiles[2][2].setState(TileState.LADDERUP);//2
        tiles[3][4].setState(TileState.LADDERDOWN);//4
        tiles[5][0].setState(TileState.LADDERUP);//4
    }


}


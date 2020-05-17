package sk.tuke.gamestudio.game.snakeAndLad.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Field {

    public static final String GAME_NAME = "S&L";
    private final int rowCount;
    private final int columnCount;
    private final Tile[][] tiles;
    private int count = 0;
    private int position=1;
    private long startMillis;

    Map<Player, Integer> playerPos;


    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.tiles = new Tile[rowCount][columnCount];

        this.playerPos = new HashMap<Player, Integer>();


           // this.playerPos.put(player, 1);

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
        startMillis = System.currentTimeMillis();

    }


    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }

    private void generateField() {

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (count % 2 == 0) {
                    int k = (((j + 1) + (i * columnCount)));
                    this.tiles[i][j] = new Tile(TileType.FREE);
                    this.tiles[i][j].setNum(k);
                } else {
                    int k = (((i + 1) * columnCount) - (j + 1) + 1);
                    this.tiles[i][j] = new Tile(TileType.FREE);
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

    private String snakeHead(int i, int j, Player player) {
        if (tiles[i][j].getState() == TileType.SNAKEHEAD) {
            for (int k = i - 1; k >= 0; k--) {
                for (int m = 9; m >= 0; m--) {
                    if (tiles[k][m].getState() == TileType.SNAKETILE) {
                        position= tiles[k][m].getNum();
                        player.setPosition(tiles[k][m].getNum());
                        playerPos.put(player, tiles[k][m].getNum());
                        //String s="You are were eaten by snake you looser";
                        //return s;
                        return ("You were eaten by a snake, your position: " + player.getPosition());
                    }
                }
            }
        }
        return ("You were eaten by a snake, your position: " + player.getPosition());
    }

    private String ladderDown(int i, int j, Player player) {
        if (tiles[i][j].getState() == TileType.LADDERDOWN) {
            for (int k = i + 1; k <= 9; k++) {
                for (int m = 9; m >= 0; m--) {
                    if (tiles[k][m].getState() == TileType.LADDERUP) {
                        position= tiles[k][m].getNum();
                        //playerPos.put(player, tiles[k][m].getNum());
                        player.setPosition(position);
                        return ("Congratulations, you climbed a ladder, your position: " + player.getPosition());

                    }
                }
            }
            return ("Congratulations, you climbed a ladder, your position: " + player.getPosition());
        }
        return ("Congratulations, you climbed a ladder, your position: " + player.getPosition());
    }


    public String move(Player player, int valueDice) {

        position += valueDice;


        System.out.println("Now your position number: " + position);


        if (position == 100) {
         //   playerPos.put(player, 100);
            player.setPosition(position);
            return (player.getName() + " " + "WINNER!" + String.valueOf(player.getPosition()));

        }

        if (position > 100) {
           position-= valueDice;
           // playerPos.put(player, k);
            player.setPosition(position);
            return ("A number greater than 100 has fallen on the dice, now your position: " + player.getPosition());

        } else {
            for (int i = 0; i < rowCount; i++) {

                for (int j = 0; j < columnCount; j++) {
                    if (tiles[i][j].getNum() == position && tiles[i][j].getState() != TileType.FREE && tiles[i][j].getState() != TileType.SNAKETILE && tiles[i][j].getState() != TileType.LADDERUP) {
                        if (tiles[i][j].getState() == TileType.SNAKEHEAD) {
                            snakeHead(i, j, player);
                            return ("You were eaten by a snake, your position: " + player.getPosition());
                        }
                        if (tiles[i][j].getState() == TileType.LADDERDOWN) {
                            ladderDown(i, j, player);
                            return ("Congratulations, you climbed a ladder, your position: " + player.getPosition());
                        }


                    } else if (tiles[i][j].getNum() == position && tiles[i][j].getState() != TileType.SNAKEHEAD && tiles[i][j].getState() != TileType.LADDERDOWN) {
             //           playerPos.put(player, position);
                        player.setPosition(position);
                      return("Your position: " + player.getPosition());
                    }
                }
            }

        }return ("Your position: " + player.getPosition());

    }


    public void lightLevel() {
        tiles[0][2].setState(TileType.SNAKETILE);//1
        tiles[1][4].setState(TileType.SNAKEHEAD);//1
        tiles[2][5].setState(TileType.SNAKETILE);//2
        tiles[3][7].setState(TileType.SNAKEHEAD);//2
        tiles[5][2].setState(TileType.SNAKEHEAD);//2
        tiles[4][0].setState(TileType.SNAKETILE);//2
        tiles[6][5].setState(TileType.SNAKEHEAD);//2
        tiles[5][3].setState(TileType.SNAKETILE);//2
        tiles[8][5].setState(TileType.SNAKEHEAD);//2
        tiles[6][8].setState(TileType.SNAKETILE);//2
        tiles[9][2].setState(TileType.SNAKEHEAD);//2
        tiles[8][4].setState(TileType.SNAKETILE);//2

        tiles[0][0].setState(TileType.LADDERDOWN);//1
        tiles[2][0].setState(TileType.LADDERUP);//1
        tiles[2][9].setState(TileType.LADDERDOWN);//2
        tiles[4][8].setState(TileType.LADDERUP);//2
        tiles[6][0].setState(TileType.LADDERDOWN);//3
        tiles[8][1].setState(TileType.LADDERUP);//3
        tiles[8][9].setState(TileType.LADDERDOWN);//3
        tiles[9][7].setState(TileType.LADDERUP);//3


    }

    private int getPlayingTime() {
        return ((int) (System.currentTimeMillis() - startMillis)) / 1000;
    }
    public int getScore() {
        return rowCount * columnCount * 3 - getPlayingTime();
    }

}


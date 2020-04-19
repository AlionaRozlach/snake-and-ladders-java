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

    Map<Player, Integer> playerPos;


    public Field(int rowCount, int columnCount, List<Player> players) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.tiles = new Tile[rowCount][columnCount];

        this.playerPos = new HashMap<Player, Integer>();

        for (int i = 0; i < players.size(); i++) {
            this.playerPos.put(players.get(i), 1);
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

    private boolean snakeHead(int i, int j, Player player) {
        if (tiles[i][j].getState() == TileType.SNAKEHEAD) {
            for (int k = i - 1; k >= 0; k--) {
                for (int m = 5; m >= 0; m--) {
                    if (tiles[k][m].getState() == TileType.SNAKETILE) {
                        player.setPosition(tiles[k][m].getNum());
                        playerPos.put(player, tiles[k][m].getNum());
                        System.out.println("You were eaten by a snake, your position: " + tiles[k][m].getNum());
                        return false;
                    }
                }
            }
        }
        return false;
    }

    private boolean ladderDown(int i, int j, Player player) {
        if (tiles[i][j].getState() == TileType.LADDERDOWN) {
            for (int k = i + 1; k <= 5; k++) {
                for (int m = 5; m >= 0; m--) {
                    if (tiles[k][m].getState() == TileType.LADDERUP) {
                        playerPos.put(player, tiles[k][m].getNum());
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


        System.out.println("Now your position number: " + position);


        if (position == 36) {
            playerPos.put(player, 36);
            player.setPosition(position);
            System.out.println(player.getName() + " " + "WINNER!");
            return true;
        }

        if (position > 36) {
            int k = position - valueDice;
            playerPos.put(player, k);
            player.setPosition(k);
            System.out.println("A number greater than 36 has fallen on the dice, now your position: " + playerPos.get(player));
            return false;
        } else {
            for (int i = 0; i < rowCount; i++) {

                for (int j = 0; j < columnCount; j++) {
                    if (tiles[i][j].getNum() == position && tiles[i][j].getState() != TileType.FREE && tiles[i][j].getState() != TileType.SNAKETILE && tiles[i][j].getState() != TileType.LADDERUP) {

                        snakeHead(i, j, player);
                        ladderDown(i, j, player);

                    } else if (tiles[i][j].getNum() == position && tiles[i][j].getState() != TileType.SNAKEHEAD && tiles[i][j].getState() != TileType.LADDERDOWN) {
                        playerPos.put(player, position);
                        player.setPosition(position);
                        return false;
                    }
                }
            }
            return false;
        }
    }


    public void lightLevel() {
        tiles[0][3].setState(TileType.SNAKETILE);//1
        tiles[2][4].setState(TileType.SNAKEHEAD);//1
        tiles[3][5].setState(TileType.SNAKETILE);//2
        tiles[4][1].setState(TileType.SNAKEHEAD);//2

        tiles[0][1].setState(TileType.LADDERDOWN);//1
        tiles[3][1].setState(TileType.LADDERUP);//1
        tiles[0][4].setState(TileType.LADDERDOWN);//2
        tiles[2][2].setState(TileType.LADDERUP);//2
        tiles[2][5].setState(TileType.LADDERDOWN);//3
        tiles[4][5].setState(TileType.LADDERUP);//3
        tiles[3][4].setState(TileType.LADDERDOWN);//4
        tiles[5][0].setState(TileType.LADDERUP);//4

    }

}


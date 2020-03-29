package SnakeAndLadders;

import java.util.*;

public class Field {
    public static final String GAME_NAME = "Snake and Ladders";
    private final int rowCount;
    private final int columnCount;
    private int snakeCount;
    private int laddersCount;
    private final Tile[][] tiles;
    private int count = 0;


    private GameState state = GameState.PLAYING;

    private Random random = new Random();


    public Field(int rowCount, int columnCount, int snakeCount, int laddersCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.snakeCount = snakeCount;
        this.laddersCount = laddersCount;
        this.tiles = new Tile[rowCount][columnCount];


        generate();
    }


    public GameState getState() {
        return state;
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

        setSnakeLaddersLevel1();
    }

    public void printTile(int i, int j) {
        //for (int i = 0; i < rowCount; i++) {
         //   for (int j = 0; j < columnCount; j++) {
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

               // }
           // }

        } //System.out.println();
    }

    private boolean snakeHead(int i,int j, Player player)
    {
        if (tiles[i][j].getState() == TileState.SNAKEHEAD) {
            for (int k = i - 1; k >= 0; k--) {
                for (int m = 5; m >= 0; m--) {
                    if (tiles[k][m].getState() == TileState.SNAKETILE) {
                        player.setPosition(tiles[k][m].getNum());
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
                        player.setPosition(tiles[k][m].getNum());
                        System.out.println("Ty podnalsa po lestnice POS " + tiles[k][m].getNum());
                        return false;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public boolean move(Player player, int valueDice) {
        //valueDice=1;
        int position = player.getPosition();
        position += valueDice;

        System.out.println("*** Now your position number: " + position + " ***");


        if (position == 36) {
            player.setPosition(position);
            System.out.println("You are winner!!!");
            return true;
        }

        if (position > 36) {
            int k = position - valueDice;
            player.setPosition(k);
            System.out.println("*** Now your position ODKAT number: " + player.getPosition() + " ***");
            return false;
        } else {
            for (int i = 0; i < rowCount; i++) {

                for (int j = 0; j < columnCount; j++) {
                    if (tiles[i][j].getNum() == position && tiles[i][j].getState() != TileState.FREE && tiles[i][j].getState() != TileState.SNAKETILE && tiles[i][j].getState() != TileState.LADDERUP) {
                       /* if (tiles[i][j].getState() == TileState.SNAKEHEAD) {
                            for (int k = i - 1; k >= 0; k--) {
                                for (int m = 5; m >= 0; m--) {
                                    if (tiles[k][m].getState() == TileState.SNAKETILE) {
                                        player.setPosition(tiles[k][m].getNum());
                                        System.out.println("You were eaten by a snake, your position: " + tiles[k][m].getNum());
                                        return false;
                                    }
                                }
                            }

                            return false;
                        }*/
                       snakeHead(i,j,player);
                       ladderDown(i,j,player);
                        /*if (tiles[i][j].getState() == TileState.LADDERDOWN) {
                            for (int k = i + 1; k <= 5; k++) {
                                for (int m = 5; m >= 0; m--) {
                                    if (tiles[k][m].getState() == TileState.LADDERUP) {
                                        player.setPosition(tiles[k][m].getNum());
                                        System.out.println("Ty podnalsa po lestnice POS " + tiles[k][m].getNum());
                                        return false;
                                    }
                                }
                            }
                            return false;
                        }*/
                    } else if (tiles[i][j].getNum() == position && tiles[i][j].getState() != TileState.SNAKEHEAD && tiles[i][j].getState() != TileState.LADDERDOWN) {
                        player.setPosition(position);

                        return false;
                    }
                }
            }
            return false;
        }
    }


    public void setSnakeLaddersLevel1() {
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
}


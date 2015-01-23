package nl.mprog.hasamishogi;

/**
 * Created by Faraicha on 21-1-2015.
 */

import java.util.ArrayList;
import java.util.Random;

/**
 * Alle mogelijke zetten printen
 */
public class CPU {
    private double bestValue;
    private Board currentBoard;
    private ArrayList<Stone> allStones;
    private ArrayList<Integer> randomPosition;
    private ArrayList<Integer> realRandomPosition;
    private Random randomGenerator;

    public CPU(Board board){
        currentBoard = board;
        allStones = new ArrayList();
        randomGenerator = new Random();
    }

    public void miniMax(Board start, int depth, int currentPlayer) {


    }

    public void cpuMove(){
        randomPosition = new ArrayList();
        allStones = currentBoard.getStonesOnBoard();
        Stone anyStone = anyStone();
        if(anyStone.getStoneColor() == Stone.BLACK_STONE_COLOR) {
            int selectedStonePosition = anyStone.getStonePosition();
            randomPosition = currentBoard.getAllPossibleMoves(selectedStonePosition);
            System.out.println(randomPosition.size() + "voor validatie");
            System.out.println(randomPosition + "Arraylist randomPos");
            removeInvalidPositions(selectedStonePosition);
            System.out.println(realRandomPosition.size() + "na validatie");
            System.out.println(realRandomPosition + "NA");
            int anyPosition = anyPosition();
            //System.out.println(randomPosition + "Arraylist randomPos");
            if (!currentBoard.emptySpots(selectedStonePosition)) {
                Stone touchedStone = currentBoard.getStone(selectedStonePosition);
                if (currentBoard.hasSelectedStone() == false && touchedStone.getStoneColor() == currentBoard.getCurrentPlayer()) {
                    touchedStone.select();
                    System.out.println(currentBoard.hasSelectedStone() + "has selected stone");
                    currentBoard.moveSelectedStoneTo(anyPosition);
                } //else {
                //touchedStone.deselect();
                // }
            }
//            else {
//                if (currentBoard.hasSelectedStone()) {
//                    if (currentBoard.moveSelectedStoneTo(40)) {
//                        Stone stoneCaptured = currentBoard.getStone(40);
//                        currentBoard.removeCapturedStones(stoneCaptured);
//                    }
//                }
//            }

            //int randomStone = 0 + (int)(Math.random() * ((8 - 0) + 1));
//        Stone stone = currentBoard.getStone(randomStone);
//        stone.setNewPosition(randomPosition);
//        System.out.println(randomPosition + " randomposition");
        }else{
            cpuMove();
        }

    }

    public void removeInvalidPositions(int position){
        realRandomPosition = new ArrayList();
        int [] oldRowColumn = currentBoard.positionToRowColumn(position);
        //System.out.println(oldRowColumn[0] + "old row");
        //System.out.println(oldRowColumn[1] + "old row");
        for(int i = 0; i < randomPosition.size(); i++){
            int newPosition = randomPosition.get(i);
            int [] newRowColumn = currentBoard.positionToRowColumn(newPosition);
            //System.out.println(newRowColumn[0] + "new row");
            //System.out.println(newRowColumn[1] + "new col");
            //System.out.println((oldRowColumn[0] != newRowColumn[0] && oldRowColumn[1] == newRowColumn[1]) + "valideer lijst");
            if(!(oldRowColumn[0] != newRowColumn[0] && oldRowColumn[1] != newRowColumn[1])){
                //System.out.println(i + "verwijderende getallen");
                realRandomPosition.add(newPosition);
            }

            //checks if there is no stone along the horizontal path
            for (int j = Math.min(oldRowColumn[0],newRowColumn[0]) + 1; j < Math.max(oldRowColumn[0], newRowColumn[0]); j++){
                int tempPosition = currentBoard.rowColumnToPosition(j, oldRowColumn[1]);
                if (currentBoard.emptySpots(tempPosition)){
                    realRandomPosition.add(newPosition);
                }
            }

            //checks if there is no stone along the vertical path
            for (int j = Math.min(oldRowColumn[1],newRowColumn[1]) + 1; j < Math.max(oldRowColumn[1], newRowColumn[1]); j++){
                int tempPosition = currentBoard.rowColumnToPosition(oldRowColumn[0], j);
                if (currentBoard.emptySpots(tempPosition)){
                    realRandomPosition.add(newPosition);
                }
            }
        }
    }

    public Stone anyStone(){
        int index = randomGenerator.nextInt(allStones.size());
        Stone randomStone = allStones.get(index);
        return randomStone;
    }

    public int anyPosition(){
        int index = randomGenerator.nextInt(realRandomPosition.size());
        int anyPosition = realRandomPosition.get(index);
        return anyPosition;
    }


}

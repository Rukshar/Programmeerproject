package nl.mprog.hasamishogi;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Faraicha on 16-1-2015.
 */
public class Board {
    public static final int BOARD_DIMENSION = 9;
    public static final int NUMBER_OF_STONES = 9;
    private ArrayList <Stone> stonesOnBoard;
    private ArrayList <Integer> allPossibilities;
    private int currentPlayer;

    public Board(){

        stonesOnBoard = new ArrayList();
        currentPlayer = Stone.WHITE_STONE_COLOR;
    }

    public void addStones(Stone stone){
        stonesOnBoard.add(stone);
    }

    public ArrayList<Stone> getStonesOnBoard(){
        return stonesOnBoard;
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public void toggleCurrentPlayer(){
        if (currentPlayer == Stone.WHITE_STONE_COLOR){
            currentPlayer = Stone.BLACK_STONE_COLOR;
        }
        else {
            currentPlayer = Stone.WHITE_STONE_COLOR;
        }
    }

    public Stone getStone(int position){
        for (Stone stone : stonesOnBoard){
            if (stone.getStonePosition() == position){
                return stone;
            }
        }
        return null;
    }

    public boolean hasSelectedStone(){
        for (Stone stone : stonesOnBoard){
            if (stone.isSelected() == true){
                return true;
            }
        }
        return false;
    }

    public boolean emptySpots(int position){
        for(Stone stone : stonesOnBoard){
            if(position == stone.getStonePosition()){
                return false;
            }
        }
        return true;
    }

    public Stone getSelectedStone(){
        for (Stone stone : stonesOnBoard){
            if (stone.isSelected() == true){
                return stone;
            }
        }
        return null;
    }

    public boolean moveSelectedStoneTo(int position){
        if(position < 0 && position >= BOARD_DIMENSION * BOARD_DIMENSION){
            System.out.println("is niet op bord");
            return false;
        }
        Stone selectedStone = getSelectedStone();
        //extra check (should not be necessary)
        if(selectedStone == null){
            System.out.println("geen selected stone");
            return false;
        }

        int oldPosition = getSelectedStone().getStonePosition();

        //check if there are no other stones on this path
        if (!canMove(oldPosition, position)){
            System.out.println("cannot move stone to pos");
            return false;
        }

        //survived all the checks, so it must be safe to move the stone
        selectedStone.setNewPosition(position);
        System.out.println("move succesvol");
        return true;
    }

    public ArrayList<Integer> getAllPossibleMoves(int stonePosition){
        System.out.println(stonePosition + "stonePosition");
        allPossibilities = new ArrayList();
        int[] stoneRowColumn = positionToRowColumn(stonePosition);
        int stoneRow = stoneRowColumn[0];
        int stoneColumn = stoneRowColumn[1];

        // Check horizontal possibilities
        int beginRowPosition = stoneRow * BOARD_DIMENSION;
        int endRowPosition = (beginRowPosition + BOARD_DIMENSION) - 1;
        System.out.println(beginRowPosition + "beginRow");
        System.out.println(endRowPosition + "endRow");
        for(int i = beginRowPosition; i < endRowPosition; i++){
            if(emptySpots(i)){
                System.out.println(i + " weet niet hor");
                allPossibilities.add(i);
            }
        }

        // Check vertical possibilities
        int beginColumnPosition = stoneColumn;
        int endColumnPosition = ((BOARD_DIMENSION - 1) * BOARD_DIMENSION + stoneColumn);
        System.out.println(beginColumnPosition + " beginColumn");
        System.out.println(endColumnPosition + " endColumn");
        for(int i = beginColumnPosition; i < endColumnPosition; i+=BOARD_DIMENSION){
            if(emptySpots(i)){
                System.out.println(i + " weet niet col");
                allPossibilities.add(i);
            }
        }
        return allPossibilities;
    }

    public boolean canMove(int fromPosition, int toPosition){
        System.out.println(fromPosition + " fromposition");
        System.out.println(toPosition + " toposition");
        int[] oldRowColumn = positionToRowColumn(fromPosition);
        int[] newRowColumn = positionToRowColumn(toPosition);

        //System.out.println(oldRowColumn + " old row columns");
        //System.out.println(newRowColumn + " new row column");
        //System.out.println(oldRowColumn[0] + " old row columns[0]");
        //System.out.println(newRowColumn[0] + " new row columns[0]");
        //System.out.println(oldRowColumn[1] + " old row columns[1]");
        //System.out.println(newRowColumn[1] + " new row columns[1]");

        //System.out.println(oldRowColumn[0] + "!=" + newRowColumn[0] + "&&" + oldRowColumn[1] + "!=" + newRowColumn[1]);
        // System.out.println((oldRowColumn[0] != newRowColumn[0] && oldRowColumn[1] != newRowColumn[1]) + " valid move");

        //check if it is a valid move (horizontal or vertical)
        if (oldRowColumn[0] != newRowColumn[0] && oldRowColumn[1] != newRowColumn[1]){
            //System.out.println("FAAALSE");
            return false;
        }
//        System.out.println("GA VERDER");
//        System.out.println(Math.min(oldRowColumn[0],newRowColumn[0]) + 1 + "min");
//        System.out.println(Math.max(oldRowColumn[0], newRowColumn[0])+ "max");

        //checks if there is no stone along the horizontal path
        for (int i = Math.min(oldRowColumn[0],newRowColumn[0]) + 1; i < Math.max(oldRowColumn[0], newRowColumn[0]); i++){
            System.out.println(i + "i");
            int tempPosition = rowColumnToPosition(i, oldRowColumn[1]);
            System.out.println(tempPosition + "tempposition");
            System.out.println(fromPosition + " - " + toPosition + " - " + tempPosition);
            if (!emptySpots(tempPosition)){
                return false;
            }
        }

        //checks if there is no stone along the vertical path
        for (int i = Math.min(oldRowColumn[1],newRowColumn[1]) + 1; i < Math.max(oldRowColumn[1], newRowColumn[1]); i++){
            int tempPosition = rowColumnToPosition(oldRowColumn[0], i);
            System.out.println(fromPosition + " - " + toPosition + " - " + tempPosition + "vertical");
            if (!emptySpots(tempPosition)){
                return false;
            }
        }
        //System.out.println("I WAS HERE");
        return true;
    }

    public void removeCapturedStones(Stone placedStone){
        removeCapturesAlongPath(0, 1, placedStone); //up
        removeCapturesAlongPath(1, 0, placedStone); //right
        removeCapturesAlongPath(0, (-1), placedStone); //down
        removeCapturesAlongPath((-1), 0, placedStone); //left

    }

    public void removeCapturesAlongPath(int deltaX, int deltaY, Stone placedStone){
        int[] placedStoneRowColumn = positionToRowColumn(placedStone.getStonePosition());
        int currentRow = placedStoneRowColumn[0] + deltaY;
        int currentColumn = placedStoneRowColumn[1] + deltaX;

        int[] capturePositions = new int[Board.NUMBER_OF_STONES*10];
        int numberOfCaptures = 0;

        while (true){
            int tempPosition = rowColumnToPosition(currentRow, currentColumn);
            Stone tempStone = getStone(tempPosition);

            if (currentRow < 0 || currentRow >= BOARD_DIMENSION || currentColumn < 0 || currentColumn >= BOARD_DIMENSION){
                //out of bounds -> do some fancy shit
                //System.out.println(deltaX + "-" + deltaY + " out of bounds");
                break;
            }
            else if (emptySpots(tempPosition) || tempStone == null){
                //System.out.println(deltaX + "-" + deltaY + " empty");
                break;
            }
            else if (placedStone.getStoneColor() != getStone(tempPosition).getStoneColor()){
                capturePositions[numberOfCaptures] = tempPosition;
                numberOfCaptures++;
            }
            else if (placedStone.getStoneColor() == getStone(tempPosition).getStoneColor()){
                //remove everything along the path
                //System.out.println(deltaX + "-" + deltaY + " HIT");
                removeStonesAtPositions(capturePositions, numberOfCaptures);
                break;
            }
            currentRow += deltaY;
            currentColumn += deltaX;
        }
    }

    public void removeStonesAtPositions(int[] positions, int numberOfPositions){
        for (int i = 0; i < numberOfPositions; i++){
            Stone capturedStone = getStone(positions[i]);
            stonesOnBoard.remove(capturedStone);
        }
    }

    // Gets score by counting the black and white stones on the board
    public int[] getScore(){
        int whiteScore = 0;
        int blackScore = 0;
        for(Stone stone : stonesOnBoard){
            if (stone.getStoneColor() == Stone.WHITE_STONE_COLOR){
                whiteScore ++;
            }else{
                blackScore ++;
            }
        }
        return new int[] {whiteScore,blackScore};
    }

    public int[] positionToRowColumn(int position){
        int column = position % BOARD_DIMENSION;
        int row = position / BOARD_DIMENSION;
        return new int[] {row, column};
    }

    public int rowColumnToPosition(int row, int column){

        return row * BOARD_DIMENSION + column;
    }

    public Board copy(){
        return new Board();
    }

}
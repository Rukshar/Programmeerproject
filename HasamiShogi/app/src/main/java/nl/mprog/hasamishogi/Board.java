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
    private int currentPlayer;

    public Board(){

        stonesOnBoard = new ArrayList();
        currentPlayer = Stone.WHITE_STONE_COLOR;
    }

    public Board(ArrayList<Stone> stonesOnBoard, int currentPlayer){
        this.stonesOnBoard = stonesOnBoard;
        this.currentPlayer = currentPlayer;
    }

    public ArrayList<Stone> getStonesOnBoard(int color){
        ArrayList<Stone> stones = new ArrayList();
        for (Stone stone : stonesOnBoard){
            if (stone.getStoneColor() == color){
                stones.add(stone);
            }
        }
        return stones;
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

    public boolean moveStoneTo(int oldposition, int newposition){
        Stone newstone = getStone(oldposition);
        newstone.setNewPosition(newposition);
        return true;
    }
    public boolean moveStoneTo(Stone stone, int position){
        Stone newstone = getStone(stone.getStonePosition());
        newstone.setNewPosition(position);
        return true;
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

    public ArrayList<Integer> getAllPossibleMoves(Stone stone){
        int stonePosition = stone.getStonePosition();
        ArrayList<Integer> possibleMoves = new ArrayList();

        for (int i = 0; i<Board.BOARD_DIMENSION*Board.BOARD_DIMENSION; i++){
            if (canMove(stonePosition, i)){
                possibleMoves.add(i);
            }
        }

        return possibleMoves;
    }

    public boolean canMove(int fromPosition, int toPosition){
        if (fromPosition == toPosition){
            return false;
        }

        if (!emptySpots(toPosition)){
            return false;
        }

        int[] oldRowColumn = positionToRowColumn(fromPosition);
        int[] newRowColumn = positionToRowColumn(toPosition);

        //check if it is a valid move (horizontal or vertical)
        if (oldRowColumn[0] != newRowColumn[0] && oldRowColumn[1] != newRowColumn[1]){
            return false;
        }


        //checks if there is no stone along the horizontal path
        for (int i = Math.min(oldRowColumn[0],newRowColumn[0]) + 1; i < Math.max(oldRowColumn[0], newRowColumn[0]); i++){
            int tempPosition = rowColumnToPosition(i, oldRowColumn[1]);
            if (!emptySpots(tempPosition)){
                return false;
            }
        }

        //checks if there is no stone along the vertical path
        for (int i = Math.min(oldRowColumn[1],newRowColumn[1]) + 1; i < Math.max(oldRowColumn[1], newRowColumn[1]); i++){
            int tempPosition = rowColumnToPosition(oldRowColumn[0], i);
            if (!emptySpots(tempPosition)){
                return false;
            }
        }

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
        ArrayList<Stone> newStones = new ArrayList();
        for (Stone stone : stonesOnBoard){
            newStones.add(stone.copy());
        }

        return new Board(newStones, currentPlayer);
    }

    public int getHeuristicValue(){
        int value = 0;

        for (Stone stone : stonesOnBoard){
            if (stone.getStoneColor() == Stone.BLACK_STONE_COLOR){
                value++;
            }
            else {
                value--;
            }
        }

        return value;
    }

    public void replaceStones(ArrayList<Stone> newStones){
        stonesOnBoard = newStones;
    }

}
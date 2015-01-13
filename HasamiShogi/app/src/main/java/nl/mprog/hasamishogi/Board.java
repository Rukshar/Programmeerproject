package nl.mprog.hasamishogi;

import java.sql.Types;
import java.util.ArrayList;

public class Board {

    public static final int BOARD_DIMENSION = 9;
    public static final int NUMBER_OF_STONES = 9;
    private int currentPlayer;

    private ArrayList<Stone> stones;

    public Board() {
        stones = new ArrayList();
        currentPlayer = Stone.WHITE_STONE;
    }

    public Board(ArrayList<Stone> stones){
        this.stones = stones;
        currentPlayer = Stone.WHITE_STONE;
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public void toggleCurrentPlayer(){
        if (currentPlayer == Stone.WHITE_STONE){
            currentPlayer = Stone.BLACK_STONE;
        }
        else {
            currentPlayer = Stone.WHITE_STONE;
        }
    }
    public void addStone(Stone newStone) {
        stones.add(newStone);
    }

    public Stone getStone(int position){
        for (Stone stone : stones){
            if (stone.getStonePosition() == position){
                return stone;
            }
        }
        return null;
    }

    public ArrayList<Stone> getStones() {
        return stones;
    }

    public boolean hasSelectedStone(){
        for (Stone stone : stones){
            if (stone.isSelected() == true){
                return true;
            }
        }
        return false;
    }

    public Stone getSelectedStone(){
        for (Stone stone : stones){
            if (stone.isSelected() == true){
                return stone;
            }
        }
        return null;
    }

    public boolean isEmpty(int position){
        for (Stone stone : stones){
            if (stone.getStonePosition() == position){
                return false;
            }
        }
        return true;
    }

    //with error checks: true -> valid, false -> invalid
    public boolean moveSelectedStoneTo(int position){
        //check if the stone tries to move somewhere on the board
        if (position < 0 && position >= BOARD_DIMENSION * BOARD_DIMENSION){
            return false;
        }

        Stone selectedStone = getSelectedStone();
        //extra check (should not be necessary)
        if (selectedStone == null)
            return false;

        int oldPosition = getSelectedStone().getStonePosition();

        //check if there are no other stones on this path
        if (!canMove(oldPosition, position)){
            return false;
        }

        //survived all the checks, so it must be safe to move the stone
        selectedStone.setStonePosition(position);
        return true;
    }

    public boolean canMove(int fromPosition, int toPosition){
        int[] oldRowColumn = positionToRowColumn(fromPosition);
        int[] newRowColumn = positionToRowColumn(toPosition);

        //check if it is a valid move (horizontal or vertical)
        if (oldRowColumn[0] != newRowColumn[0] && oldRowColumn[1] != newRowColumn[1]){
            return false;
        }

        //checks if there is no stone along the horizontal path
        for (int i = Math.min(oldRowColumn[0],newRowColumn[0]) + 1; i < Math.max(oldRowColumn[0], newRowColumn[0]); i++){
            int tempPosition = rowColumnToPosition(i, oldRowColumn[1]);
            //System.out.println(fromPosition + " - " + toPosition + " - " + tempPosition);
            if (!isEmpty(tempPosition)){
                return false;
            }
        }

        //checks if there is no stone along the vertical path
        for (int i = Math.min(oldRowColumn[1],newRowColumn[1]) + 1; i < Math.max(oldRowColumn[1], newRowColumn[1]); i++){
            int tempPosition = rowColumnToPosition(oldRowColumn[0], i);
            //System.out.println(fromPosition + " - " + toPosition + " - " + tempPosition);
            if (!isEmpty(tempPosition)){
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
                System.out.println(deltaX + "-" + deltaY + " out of bounds");
                break;
            }
            else if (isEmpty(tempPosition) || tempStone == null){
                System.out.println(deltaX + "-" + deltaY + " empty");
                break;
            }
            else if (placedStone.getStoneColor() != getStone(tempPosition).getStoneColor()){
                capturePositions[numberOfCaptures] = tempPosition;
                numberOfCaptures++;
            }
            else if (placedStone.getStoneColor() == getStone(tempPosition).getStoneColor()){
                //remove everything along the path
                System.out.println(deltaX + "-" + deltaY + " HIT");
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
            stones.remove(capturedStone);
        }
    }

    public int[] positionToRowColumn(int position){
        int column = position % BOARD_DIMENSION;
        int row = position / BOARD_DIMENSION;
        return new int[] {row, column};
    }

    public int rowColumnToPosition(int row, int column){
        return row * BOARD_DIMENSION + column;
    }

}

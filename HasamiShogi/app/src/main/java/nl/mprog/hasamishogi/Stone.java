package nl.mprog.hasamishogi;

/**
 * Rukshar Wagid Hosain
 * faraicha@live.nl
 * 10694676
 */

/**
 * THIS CLASS HOLDS ALL THE INFORMATION ABOUT ONE STONE ON THE BOARD
 */

public class Stone {
    public static final int WHITE_STONE_COLOR = 1;
    public static final int BLACK_STONE_COLOR = 2;

    private int stonePosition;
    private int stoneColor;
    private boolean stoneSelected;

    public Stone(int position, int color){
        stonePosition = position;
        stoneColor = color;
        stoneSelected = false;
    }

    public Stone(int position, int color, boolean selected){
        stonePosition = position;
        stoneColor = color;
        stoneSelected = selected;
    }

    public int getStonePosition(){
        return stonePosition;
    }

    public int getStoneColor(){
        return stoneColor;
    }

    public void setNewPosition(int newPosition){
        stonePosition = newPosition;
        deselect();
    }

    public void select(){
        stoneSelected = true;
    }

    public void deselect(){
        stoneSelected = false;
    }

    public boolean isSelected(){
        return stoneSelected;
    }

    public Stone copy(){
        return new Stone(stonePosition, stoneColor, stoneSelected);
    }
}

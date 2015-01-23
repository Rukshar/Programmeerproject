package nl.mprog.hasamishogi;

/**
 * Created by Faraicha on 16-1-2015.
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
        this.stoneSelected = selected;
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

    public void toggleSelected(){
        stoneSelected = !stoneSelected;
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

}

package nl.mprog.hasamishogi;


public class Stone {
    public static final int WHITE_STONE = 1;
    public static final int BLACK_STONE = 2;

    private int stoneColor;
    private int stonePosition;
    private boolean selected;

    public Stone(int color, int position){
        stoneColor = color;
        stonePosition = position;
        selected = false;
    }

    public Stone(int color, int position, boolean selected){
        stoneColor = color;
        stonePosition = position;
        this.selected = selected;
    }

    public int getStonePosition(){
        return stonePosition;
    }

    public int getStoneColor(){
        return stoneColor;
    }

    public void toggleSelected(){
        selected = !selected;
    }

    public void deselect(){
        selected = false;
    }

    public Stone copy(){
        return new Stone(stoneColor, stonePosition, this.selected);
    }

    public void select(){
        selected = true;
    }

    public boolean isSelected(){
        return selected;
    }

    public void setStonePosition(int newPosition){
        stonePosition = newPosition;
        deselect();
    }

}

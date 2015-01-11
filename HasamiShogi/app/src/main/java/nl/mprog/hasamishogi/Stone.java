package nl.mprog.hasamishogi;


public class Stone {

    public Stone(int color, int position){
        int stoneColor = color;
        int stonePosition = position;
    }

    // Returns the color of the stone
    public int getColor(int stoneColor){
        return stoneColor;
    }

    // Returns the position (index) of the stone on the board
    public int getPosition(int stonePosition){
        return stonePosition;
    }
}

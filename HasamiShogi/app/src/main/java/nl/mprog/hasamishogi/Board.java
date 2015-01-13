package nl.mprog.hasamishogi;

import java.util.ArrayList;

public class Board {

    private int boardDimension;

    public Board(){
        boardDimension = 9;
    }

    // Adds stone objects to an ArrayList and returns the white stone ArrayList
    public ArrayList<Stone> getWhiteStones() {
        ArrayList<Stone> allWhiteStones = new ArrayList();
        for(int i = 0; i < boardDimension; i++) {
            Stone whiteStone = new Stone(1, i);
            allWhiteStones.add(whiteStone);
        }
        return allWhiteStones;
    }

    // Adds stone objects to an ArrayList and returns the black stone ArrayList
    public ArrayList<Stone> getBlackStones(){
        ArrayList<Stone> allBlackStones = new ArrayList();
        for(int i = 0; i < boardDimension; i++){
            Stone blackStone = new Stone(2, i + 72);
            allBlackStones.add(blackStone);
        }
        return allBlackStones;
    }
}

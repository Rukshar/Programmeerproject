package nl.mprog.hasamishogi;

import java.util.ArrayList;


public class Board {

    public Board(int boardDimension, ArrayList stones){
        boardDimension = 9;

        // Adds stone objects to the board
        for (int i = 0; i < boardDimension; i++){
            Stone whiteStone = new Stone(1,i);
            Stone blackStone = new Stone(2,i);
            stones.add(whiteStone);
            stones.add(blackStone);
        }
    }

}

package nl.mprog.hasamishogi;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Faraicha on 23-1-2015.
 */
public class AI {
    public static final int EASY_CPU = 1;
    public static final int HARD_CPU = 2;

    private int level;
    private int color;
    private Random random;


    AI(int level, int color){
        this.level = level;
        this.color = color;
    }

    public void doTurn(Board board){
        if (level == EASY_CPU){
            doEasyTurn(board);
        }
        else {
            doHardTurn(board);
        }
    }

    public void doHardTurn(Board board){
        random = new Random();
        int[] results = minimax(board, 2, color, 0, 0);
        board.getStone(results[1]).setNewPosition(results[2]);
        board.removeCapturedStones(board.getStone(results[2]));
    }

    public void doEasyTurn(Board board) {
        Random random = new Random();

        //get all stones belonging to this cpu
        ArrayList<Stone> cpuStones = board.getStonesOnBoard(color);
        int bestHeuristic = Integer.MIN_VALUE;
        int bestMoveFrom = 0;
        int bestMoveTo = 0;

        //for every stone
        for (Stone stone : cpuStones) {

            //get all possible moves
            ArrayList<Integer> possibleMoves = board.getAllPossibleMoves(stone);

            //for every possible move of stone
            for (int move : possibleMoves){
                Board tempboard = board.copy();
                tempboard.moveStoneTo(stone, move);
                tempboard.removeCapturedStones(tempboard.getStone(move));
                int currentHeuristic = tempboard.getHeuristicValue();
                if (currentHeuristic > bestHeuristic || (currentHeuristic == bestHeuristic && random.nextInt(cpuStones.size() * possibleMoves.size()) <= 1)){
                    bestHeuristic = currentHeuristic;
                    bestMoveFrom = stone.getStonePosition();
                    bestMoveTo = move;
                }
            }
        }

        board.getStone(bestMoveFrom).setNewPosition(bestMoveTo);
        board.removeCapturedStones(board.getStone(bestMoveTo));
        System.out.println("CPU MOVE FROM: " + bestMoveFrom + " TO: " + bestMoveTo);

    }

    public void f(String n){
        System.out.println(n);
    }

    public int[] minimax (Board board, int depth, int currentPlayer, int fromPos, int toPos){
        if (depth == 0){
            return new int[] {board.getHeuristicValue(), fromPos, toPos};
        }

        ArrayList<Stone> playerStones = board.getStonesOnBoard(currentPlayer);

        //cpu turn
        if (currentPlayer == color){
            int[] bestValue = new int[]{Integer.MIN_VALUE, 0, 0};
            Board bestBoard = null;

            //for every stone
            for (Stone stone : playerStones) {

                //get all possible moves
                ArrayList<Integer> possibleMoves = board.getAllPossibleMoves(stone);

                //for every possible move of stone
                int stonepos = stone.getStonePosition();
                for (int move : possibleMoves){
                    Board tempboard = board.copy();
                    Stone tempstone = tempboard.getStone(stonepos);
                    tempstone.setNewPosition(move);
                    tempboard.removeCapturedStones(tempstone);
                    int[] currentHeuristic = minimax(tempboard, depth - 1, Stone.WHITE_STONE_COLOR, stonepos, move);
                    if (currentHeuristic[0] > bestValue[0] || (currentHeuristic[0] == bestValue[0] && random.nextInt(1000) <= 40)){
                        bestValue = currentHeuristic;
                        bestValue[1] = stone.getStonePosition();
                        bestValue[2] = move;
                    }
                }
            }
            return bestValue;
        }
        //player turn
        else {
            int[] bestValue = new int[]{Integer.MAX_VALUE, 0, 0};
            //for every stone
            for (Stone stone : playerStones) {

                //get all possible moves
                ArrayList<Integer> possibleMoves = board.getAllPossibleMoves(stone);

                //for every possible move of stone
                int stonepos = stone.getStonePosition();
                for (int move : possibleMoves){
                    Board tempboard = board.copy();
                    Stone tempstone = tempboard.getStone(stonepos);
                    tempstone.setNewPosition(move);
                    tempboard.removeCapturedStones(tempstone);
                    int[] currentHeuristic = minimax(tempboard, depth - 1, Stone.BLACK_STONE_COLOR, stonepos, move);
                    if (currentHeuristic[0] < bestValue[0]){
                        bestValue = currentHeuristic;
                    }
                }
            }
            return bestValue;
        }
    }

}

package nl.mprog.hasamishogi;

/**
 * Rukshar Wagid Hosain
 * faraicha@live.nl
 * 10694676
 */

/**
 * THIS CLASS HOLDS THE GAMEVIEW FOR THE TWO PLAYER OPTION
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class GameView extends View{

    private float boardWidth;
    private Paint statPaint, textPaint,textPaintBold, brownPaint, grayPaint, whitePaint;
    private Bitmap bigWhiteStone,bigBlackStone,whiteStone,blackStone;
    private Board board;
    private int whiteScore, blackScore;

    public GameView(Context context){
        super(context);
        getScreenWidth(context);

        // Load paint colors/settings
        statPaint = new Paint();
        statPaint.setColor(Color.LTGRAY);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.LEFT);

        textPaintBold = new Paint();
        textPaintBold.setColor(Color.DKGRAY);
        textPaintBold.setTextSize(50);
        textPaintBold.setTextAlign(Paint.Align.LEFT);

        brownPaint = new Paint();
        brownPaint.setColor(Color.rgb(210,180,140));

        grayPaint = new Paint();
        grayPaint.setColor(Color.DKGRAY);

        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);

        // Load bitmaps for stones
        int stoneDimension = (int) (boardWidth/Board.NUMBER_OF_STONES);
        bigWhiteStone = BitmapFactory.decodeResource(this.getResources(), R.drawable.whitestone);
        whiteStone = Bitmap.createScaledBitmap(bigWhiteStone, stoneDimension, stoneDimension, true);
        bigBlackStone = BitmapFactory.decodeResource(this.getResources(), R.drawable.blackstone);
        blackStone = Bitmap.createScaledBitmap(bigBlackStone, stoneDimension, stoneDimension, true);

        // Initialize Board object
        board = new Board();

        // Place Stones on Board
        for(int i = 0; i < Board.NUMBER_OF_STONES; i++){
            board.addStones(new Stone(i + 72, Stone.WHITE_STONE_COLOR));
            board.addStones(new Stone(i, Stone.BLACK_STONE_COLOR));
        }
    }

    // Get screenWidth of device
    private void getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        boardWidth = screenWidth;
    }

    // Draws rectangle for statistics such as player and score
    private void drawStatRect(Canvas canvas){
        canvas.drawRect(0, 0, boardWidth, boardWidth/3, statPaint);
    }

    // Draws the text in the statistics rectangle
    private void drawRectText(Canvas canvas) {
        int[] scores = board.getScore();
        whiteScore = scores[0];
        blackScore = scores[1];
        String stringWhiteScore = Integer.toString(whiteScore);
        String stringBlackScore = Integer.toString(blackScore);
        int currentPlayer = board.getCurrentPlayer();
        if(currentPlayer == Stone.WHITE_STONE_COLOR){
            canvas.drawText("Player 2: " + stringBlackScore, boardWidth/2, boardWidth/6, textPaint);
            canvas.drawText("Player 1: " + stringWhiteScore, 0, boardWidth/6, textPaintBold);
        }else{
            canvas.drawText("Player 1: " + stringWhiteScore, 0, boardWidth/6, textPaint);
            canvas.drawText("Player 2: " + stringBlackScore, boardWidth/2, boardWidth/6, textPaintBold);
        }
        winGame();
    }

    public void winGame(){
        if (whiteScore <= 4) {
            Intent intent = new Intent(this.getContext(), WinGameActivity.class);
            intent.putExtra("winner", "Player 2");
            this.getContext().startActivity(intent);
        }
        if (blackScore <= 4) {
            Intent intent = new Intent(this.getContext(), WinGameActivity.class);
            intent.putExtra("winner", "Player 1");
            this.getContext().startActivity(intent);
        }
    }

    // Draws the 9x9 board with border and lines
    private void drawBoard(Canvas canvas){
        canvas.drawRect(0, boardWidth/3, boardWidth, boardWidth + (boardWidth/3), grayPaint);
        canvas.drawRect(10,(boardWidth/3)+10, boardWidth - 10, boardWidth + (boardWidth/3)-10, brownPaint);

        for(float i = boardWidth/9; i < boardWidth; i += boardWidth/9){
            canvas.drawLine(i, boardWidth/3, i, boardWidth + (boardWidth/3), grayPaint);
            canvas.drawLine(0, i + (boardWidth/3), boardWidth, i + (boardWidth/3), grayPaint);
        }
    }

    // Draws the stones
    private void drawStones(Canvas canvas){
        ArrayList<Stone> stones = board.getStonesOnBoard();
        float offset = whiteStone.getWidth() / 2;
        for(int i = 0; i < stones.size(); i++){
            Stone tempStone = stones.get(i);
            int[] coordinates = positionToCoordinates(tempStone.getStonePosition());
            int x = coordinates[0];
            int y = coordinates[1];
            if(tempStone.getStoneColor() == Stone.WHITE_STONE_COLOR){
                canvas.drawBitmap(whiteStone, x, y + (boardWidth/3), null);
            }else{
                canvas.drawBitmap(blackStone, x, y + (boardWidth/3), null);
            }
            if (tempStone.isSelected() && tempStone.getStoneColor() == Stone.WHITE_STONE_COLOR) {
                canvas.drawCircle((x + offset), (y + offset + (boardWidth/3)), (float) whiteStone.getWidth() / 4 + 5, whitePaint);
            }
            else if(tempStone.isSelected() && tempStone.getStoneColor() == Stone.BLACK_STONE_COLOR){
                canvas.drawCircle((x + offset), (y + offset + (boardWidth/3)), (float) whiteStone.getWidth() / 4 + 5, grayPaint);
            }
        }
    }

    // Returns the coordinates of an index on the board
    private int[] positionToCoordinates(int position){
        int squareOnBoard = (int) (boardWidth/Board.BOARD_DIMENSION);
        int xPosition = position%Board.BOARD_DIMENSION;
        int xCoordinate = xPosition * squareOnBoard;
        int yPosition = (position / Board.BOARD_DIMENSION) + 1;
        int yCoordinate = ((yPosition * squareOnBoard) - squareOnBoard);
        return new int[] {xCoordinate,yCoordinate};

    }

    // Returns the index belonging to coordinates on the board
    private int coordinatesToPosition(float x, float y){
        int squareOnBoard = (int) (boardWidth/Board.BOARD_DIMENSION);
        int xIndex = (int) x / squareOnBoard;
        int deltaY = (int) ((int) y - (boardWidth/3));
        int yIndex = (deltaY / squareOnBoard) * Board.BOARD_DIMENSION;
        int position = xIndex + yIndex;
        return position;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        drawStatRect(canvas);
        drawRectText(canvas);
        drawBoard(canvas);
        drawStones(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int pressedPosition = coordinatesToPosition(x, y);
                if(!board.emptySpots(pressedPosition)){
                    Stone touchedStone = board.getStone(pressedPosition);
                    if(board.hasSelectedStone() == false && touchedStone.getStoneColor() == board.getCurrentPlayer()){
                        touchedStone.select();
                    }else{
                        touchedStone.deselect();
                    }
                }else{
                    if(board.hasSelectedStone()){
                        if(board.moveSelectedStoneTo(pressedPosition)){
                            Stone stoneCaptured = board.getStone(pressedPosition);
                            board.removeCapturedStones(stoneCaptured);
                            board.toggleCurrentPlayer();
                        }
                    }
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        return true;
    }
}
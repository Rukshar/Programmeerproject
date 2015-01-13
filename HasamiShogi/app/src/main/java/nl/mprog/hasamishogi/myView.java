package nl.mprog.hasamishogi;

import android.content.Context;
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

public class myView extends View{
    private Paint grayPaint, brownPaint;
    private float boardWidth;
    private Board currentBoard;
    private Bitmap whiteStone, blackStone, bigWhiteStone, bigBlackStone;

    public myView(Context context){
        super(context);
        getScreenSize(context);

        currentBoard = new Board();

        //initialize stone images
        int stoneDimension = (int) (boardWidth/Board.BOARD_DIMENSION);
        bigWhiteStone = BitmapFactory.decodeResource(this.getResources(), R.drawable.whitestone);
        whiteStone = Bitmap.createScaledBitmap(bigWhiteStone, stoneDimension, stoneDimension, true);
        bigBlackStone = BitmapFactory.decodeResource(this.getResources(), R.drawable.blackstone);
        blackStone = Bitmap.createScaledBitmap(bigBlackStone, stoneDimension, stoneDimension, true);
        bigBlackStone = blackStone.copy(Bitmap.Config.ARGB_8888, true);

        //create stones
        int squaresOnBoard = (Board.BOARD_DIMENSION*Board.BOARD_DIMENSION);
        for(int i = 0; i < Board.NUMBER_OF_STONES; i++) {
            currentBoard.addStone(new Stone(Stone.WHITE_STONE, i));
            currentBoard.addStone(new Stone(Stone.BLACK_STONE, squaresOnBoard - 1 - i));
        }

        //set all paint colors
        grayPaint = new Paint();
        grayPaint.setColor(Color.DKGRAY);
        grayPaint.setStrokeWidth(10);

        brownPaint = new Paint();
        brownPaint.setStrokeWidth(0);
        brownPaint.setColor(Color.rgb(210, 180, 140));
    }

    // Returns device's screen width
    private void getScreenSize(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        boardWidth = screenWidth;
    }

    // Draws the 9x9 board
    private void drawBoard(Canvas canvas){
        // Draws a gray border of the board
        canvas.drawRect(0, 0, boardWidth, boardWidth, grayPaint);

        // Fills the rectangle with a brown color
        canvas.drawRect(10, 10, boardWidth - 10, boardWidth - 10, brownPaint);

        // Draws the horizontal and vertical lines on the board
        for(float i = boardWidth/Board.BOARD_DIMENSION; i < boardWidth; i += boardWidth/Board.BOARD_DIMENSION){
            canvas.drawLine(i, 0, i, boardWidth,grayPaint);
            canvas.drawLine(0, i, boardWidth, i, grayPaint);
        }
    }

    // Draws the stones on the board
    private void drawStones(Canvas canvas){
        // Draws the stones
        float offset = whiteStone.getWidth() / 2;

        ArrayList<Stone> stones = currentBoard.getStones();
        for(int i = 0; i < stones.size(); i ++){
            Stone currentStone = stones.get(i);
            int[] stoneCoordinates = positionToCoordinates(currentStone.getStonePosition());
            if (currentStone.getStoneColor() == Stone.BLACK_STONE){
                canvas.drawBitmap(blackStone, stoneCoordinates[0], stoneCoordinates[1], null);
            }
            else {
                canvas.drawBitmap(whiteStone, stoneCoordinates[0], stoneCoordinates[1], null);
            }
            if (currentStone.isSelected()) {
                canvas.drawCircle((float) (stoneCoordinates[0] + offset), (float) (stoneCoordinates[1] + offset), (float) whiteStone.getWidth() / 4 + 5, grayPaint);
            }
        }
    }

    // Gets an index on the board and returns its x and y coordinate
    private int[] positionToCoordinates(int position){
        int squareOnBoard = (int) (boardWidth/Board.BOARD_DIMENSION);
        int xPosition = position%Board.BOARD_DIMENSION;
        int xCoordinate = xPosition * squareOnBoard;
        int yPosition = (position / Board.BOARD_DIMENSION) + 1;
        int yCoordinate = (yPosition * squareOnBoard) - squareOnBoard;
        return new int[] {xCoordinate,yCoordinate};
    }

    // Gets coordinates on the board and returns the index belonging to the coordinates
    private int coordinatesToPosition(float x, float y){
        int squareOnBoard = (int) (boardWidth/Board.BOARD_DIMENSION);
        int xIndex = (int) x / squareOnBoard;
        int yIndex = ((int) y / squareOnBoard) * Board.BOARD_DIMENSION;
        int position = xIndex + yIndex;
        return position;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawStones(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int touchedPosition = coordinatesToPosition(eventX, eventY);

                // if there is a stone at that position
                if (!currentBoard.isEmpty(touchedPosition)) {
                    Stone touchedStone = currentBoard.getStone(touchedPosition);
                    //if there is no stone selected yet
                    if (currentBoard.hasSelectedStone() == false && touchedStone.getStoneColor() == currentBoard.getCurrentPlayer())
                        touchedStone.select();
                        //if there is an other selected stone, we can simply deselect all
                    else {
                        touchedStone.deselect();
                    }
                }
                // if there is an empty square at that position
                else {
                    //if there is a previously selected stone
                    if (currentBoard.hasSelectedStone()) {
                        //move that one to the empty position
                        if (currentBoard.moveSelectedStoneTo(touchedPosition)){
                            //the stone is moved successfully
                            currentBoard.removeCapturedStones(currentBoard.getStone(touchedPosition));
                            currentBoard.toggleCurrentPlayer();
                            //ARTIFICIAL INTELLIGENCE CAN BE PLACED HERE -> SINCE OUR TURN IS OVER
                        }
                    }
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                // nothing to do
                break;
            default:
                return false;
        }
        return true;
    }
}

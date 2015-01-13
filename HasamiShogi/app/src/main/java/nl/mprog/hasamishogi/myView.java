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
    Paint paint = new Paint();
    private float boardWidth;
    private int boardDimension = 9;
    private ArrayList<Stone> allWhiteStones;
    private ArrayList<Stone> allBlackStones;

    public myView(Context context){
        super(context);
        boardWidth = getScreenSize(context);
    }

    // Returns device's screen width
    private int getScreenSize(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        return screenWidth;
    }

    // Draws the 9x9 board
    private void drawBoard(Canvas canvas){
        // Draws a gray border of the board
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(10);
        canvas.drawRect(0, 0, boardWidth, boardWidth, paint);

        // Fills the rectangle with a brown color
        paint.setStrokeWidth(0);
        paint.setColor(Color.rgb(210, 180, 140));
        canvas.drawRect(10, 10, boardWidth - 10, boardWidth - 10, paint);

        // Draws the horizontal and vertical lines on the board
        paint.setColor(Color.DKGRAY);
        for(float i = boardWidth/boardDimension; i < boardWidth; i += boardWidth/boardDimension){
            canvas.drawLine(i, 0, i, boardWidth,paint);
            canvas.drawLine(0, i, boardWidth, i, paint);
        }
    }

    // Returns the stones as bitmaps
    private Bitmap[] getImageStones(){
        int stoneDimension = (int) (boardWidth/boardDimension);
        Bitmap whiteStone = BitmapFactory.decodeResource(this.getResources(), R.drawable.whitestone);
        Bitmap blackStone = BitmapFactory.decodeResource(this.getResources(), R.drawable.blackstone);
        Bitmap smallWhiteStone = Bitmap.createScaledBitmap(whiteStone, stoneDimension, stoneDimension, true);
        Bitmap smallBlackStone = Bitmap.createScaledBitmap(blackStone, stoneDimension, stoneDimension, true);
        return new Bitmap[] {smallWhiteStone, smallBlackStone};
    }

    // Draws the stones on the board
    private void drawStones(Canvas canvas){
        Bitmap[] imageOfStone = getImageStones();
        Bitmap white = imageOfStone[0];
        Bitmap black = imageOfStone[1];
        Board board = new Board();
        allWhiteStones = board.getWhiteStones();
        allBlackStones = board.getBlackStones();

        // Draws the white stones
        for(int i = 0; i < allWhiteStones.size(); i ++){
            Stone whiteStone = allWhiteStones.get(i);
            int whiteStonePosition = whiteStone.stonePosition;
            int[] xCoordinateWhite = positionToCoordinates(whiteStonePosition);
            canvas.drawBitmap(white, xCoordinateWhite[0], 0, paint);
        }

        // Draws the black stones
        for(int i = 0; i < allBlackStones.size(); i++){
            Stone blackStone = allBlackStones.get(i);
            int blackStonePosition = blackStone.stonePosition;
            int[] xCoordinateBlack = positionToCoordinates(blackStonePosition);
            canvas.drawBitmap(black, xCoordinateBlack[0], boardWidth - (boardWidth/boardDimension), paint);
        }
    }

    // Gets an index on the board and returns its x and y coordinate
    private int[] positionToCoordinates(int position){
        int squareOnBoard = (int) (boardWidth/boardDimension);
        int xPosition = position%boardDimension;
        int xCoordinate = xPosition * squareOnBoard;
        int yPosition = (position / boardDimension) + 1;
        int yCoordinate = (yPosition * squareOnBoard) - squareOnBoard;
        return new int[] {xCoordinate,yCoordinate};
    }

    // Gets coordinates on the board and returns the index belonging to the coordinates
    private int coordinatesToPosition(float x, float y){
        int squareOnBoard = (int) (boardWidth/boardDimension);
        int xIndex = (int) x / squareOnBoard;
        int yIndex = ((int) y / squareOnBoard) * boardDimension;
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
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        int position = coordinatesToPosition(x,y);
        System.out.println(position);
        Stone stone = allWhiteStones.remove(position);
        System.out.println(allWhiteStones.size());

        return true;
    }
}

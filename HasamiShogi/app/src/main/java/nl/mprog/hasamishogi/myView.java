package nl.mprog.hasamishogi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;


public class myView extends View {
    Paint paint = new Paint();
    private float boardWidth;
    private int boardDimension = 9;

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
    private Bitmap[] getStones(){
        int stoneDimension = (int) (boardWidth/boardDimension);
        Bitmap whiteStone = BitmapFactory.decodeResource(this.getResources(), R.drawable.whitestone);
        Bitmap blackStone = BitmapFactory.decodeResource(this.getResources(), R.drawable.blackstone);
        Bitmap smallWhiteStone = Bitmap.createScaledBitmap(whiteStone, stoneDimension, stoneDimension, true);
        Bitmap smallBlackStone = Bitmap.createScaledBitmap(blackStone, stoneDimension, stoneDimension, true);
        return new Bitmap[] {smallWhiteStone, smallBlackStone};
    }

    // Draws the stones on the board
    private void drawStones(Canvas canvas){
        Bitmap[] imageOfStone = getStones();
        Bitmap white = imageOfStone[0];
        Bitmap black = imageOfStone[1];

        for(float i = 0; i < boardWidth; i += boardWidth/boardDimension){
            canvas.drawBitmap(white, i, 0, paint);
            canvas.drawBitmap(black, i, boardWidth - (boardWidth/boardDimension), paint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawStones(canvas);
    }

}

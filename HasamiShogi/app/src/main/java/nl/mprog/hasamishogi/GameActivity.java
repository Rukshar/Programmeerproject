package nl.mprog.hasamishogi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;



public class GameActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new myView(this));
        //loadGame();
    }

    // Returns the device's screen dimensions
    private int[] getScreenSize(){
        System.out.println("here");
        DisplayMetrics metrics = getBaseContext().getResources().getDisplayMetrics();
        System.out.println(metrics + "metrics");
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        System.out.println(screenWidth + "de screenwidth");
        return new int[] {screenWidth, screenHeight};
    }

    // Returns a squared board
    public Bitmap loadBoard(){
        int screenDimensions[] = getScreenSize();
        Bitmap board = BitmapFactory.decodeResource(this.getResources(), R.drawable.board);
        Bitmap squaredBoard = Bitmap.createScaledBitmap(board,screenDimensions[0],screenDimensions[0],true);
        return squaredBoard;
    }

    // Returns the stones
    private Bitmap[] loadStones(){
       Bitmap whiteStone = BitmapFactory.decodeResource(this.getResources(), R.drawable.whitestone);
       Bitmap blackStone = BitmapFactory.decodeResource(this.getResources(), R.drawable.blackstone);
       Bitmap smallWhiteStone = Bitmap.createScaledBitmap(whiteStone,100,100,true);
       Bitmap smallBlackStone = Bitmap.createScaledBitmap(blackStone, 100,100,true);
       return new Bitmap[] {smallWhiteStone,smallBlackStone};
    }

    private Bitmap overlay(Bitmap board, Bitmap whiteStones, Bitmap blackStones){
        Bitmap bmOverlay = Bitmap.createBitmap(board.getWidth(), board.getHeight(), board.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(board, new Matrix(), null);
        canvas.drawBitmap(whiteStones, new Matrix(), null);
        canvas.drawBitmap(blackStones, new Matrix(), null);
        return bmOverlay;
    }

    public void loadGame(){
        Bitmap board = loadBoard();
        Bitmap stones [] = loadStones();
        Bitmap whiteStone = stones[0];
        Bitmap blackStone = stones[1];
        overlay(board, whiteStone, blackStone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

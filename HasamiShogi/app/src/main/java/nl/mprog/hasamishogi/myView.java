package nl.mprog.hasamishogi;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class myView extends View {
    public myView(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        GameActivity gameContents = new GameActivity();
        gameContents.loadGame();

    }

}

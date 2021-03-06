package nl.mprog.hasamishogi;

/**
 * Rukshar Wagid Hosain
 * faraicha@live.nl
 * 10694676
 */

/**
 * THIS CLASS SETS THE GAMEACTIVITY TO A GAMEVIEW
 */

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class GameActivity extends ActionBarActivity {

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();

        if(extras == null){
            setContentView(new GameView(this));
        }else{
            setContentView(new CPUGameView(this));
        }
    }

    // If back button on device is pressed, go back to menu
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}

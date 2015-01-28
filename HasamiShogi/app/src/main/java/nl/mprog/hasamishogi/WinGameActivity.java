package nl.mprog.hasamishogi;

/**
 * Rukshar Wagid Hosain
 * faraicha@live.nl
 * 10694676
 */

/**
 * THIS CLASS IS THE WINACTIVITY
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WinGameActivity extends Activity {
    private String winner;
    private Boolean againstCpu;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_game);
        Intent intent = getIntent();
        winner = intent.getStringExtra("winner");
        againstCpu = intent.getBooleanExtra("cpu", false);
        setTextTo();
        playAgain();
        toMenu();
    }

    public void setTextTo() {
        TextView player = (TextView) findViewById(R.id.winner);
        player.setText(winner + " has won");
    }

    public void playAgain(){
        Button playAgain = (Button) findViewById(R.id.playagain);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                if(againstCpu == true){
                    intent.putExtra("cpu", true);
                }
                startActivity(intent);
            }
        });
    }

    public void toMenu(){
        Button menu = (Button) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}

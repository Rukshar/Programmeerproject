package nl.mprog.hasamishogi;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;


public class SettingsActivity extends ActionBarActivity {
    private RadioButton cpuHard;
    private RadioButton cpuEasy;
    private RadioButton currentChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        cpuHard = (RadioButton) findViewById(R.id.radioButtonHard);
        cpuEasy = (RadioButton) findViewById(R.id.radioButtonEasy);
        Settings();
    }

    public void Settings(){
        currentChecked = cpuHard;
        currentChecked.setChecked(true);
        cpuHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentChecked == cpuHard){
                    currentChecked.setChecked(true);
                }else{
                    currentChecked.setChecked(false);
                    currentChecked = cpuHard;
                    currentChecked.setChecked(true);
                }
            }
        });
        cpuEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentChecked == cpuEasy){
                    currentChecked.setChecked(true);
                }else{
                    currentChecked.setChecked(false);
                    currentChecked = cpuEasy;
                    currentChecked.setChecked(true);
                }
            }
        });
    }
}

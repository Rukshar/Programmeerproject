package nl.mprog.hasamishogi;

/**
 * Rukshar Wagid Hosain
 * faraicha@live.nl
 * 10694676
 */

/**
 * THIS CLASS SAVES ALL THE SETTINGS
 */

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends ActionBarActivity {
    public static int checkedIndex;
    private final String CPU_LEVEL = "CPU LEVEL";
    private final String PREFS = "myPrefs";
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(radioGroupCheckedListener);
        loadPreferences();
    }

    RadioGroup.OnCheckedChangeListener radioGroupCheckedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);
            checkedIndex = radioGroup.indexOfChild(checkedRadioButton);
            savePreferences(CPU_LEVEL, checkedIndex);
        }
    };

    private void savePreferences(String level, int index){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(level, index);
        editor.commit();
    }

    private void loadPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        int savedCpuLevel = sharedPreferences.getInt(CPU_LEVEL, 0);
        RadioButton savedCheckedRadioButton = (RadioButton)radioGroup.getChildAt(savedCpuLevel);
        savedCheckedRadioButton.setChecked(true);
    }
}

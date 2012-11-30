package com.example.tutorialapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
    public final static String KEY_PREF_THEME = "pref_theme";
    public final static String KEY_NO_OF_TEAMS = "pref_teams_number";
    public final static String KEY_ASK_FOR_TEAMS = "pref_ask_teams";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {

    	// Assign a theme according to settings or default
        setTheme(MainActivity.CURRENT_THEME);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onBackPressed() {
    	// When the user tries to go back, the activity 
    	//notifies that this won't change the current theme
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(getResources().getText(R.string.warning_settings))
    	       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                SettingsActivity.this.finish();
    	           }
    	       });
    	builder.create().show();
//    	super.onBackPressed();
    }
}

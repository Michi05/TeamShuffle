package com.example.tutorialapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;

public class SettingsActivity extends PreferenceActivity {
	// These are the keys used in the preference.xml file
    public final static String KEY_ASK_FOR_TEAMS = "pref_ask_teams";
    public final static String KEY_NO_OF_TEAMS = "pref_teams_number";
    public final static String KEY_PREF_THEME = "pref_theme";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// Assign a theme according to settings or default
        setTheme(MainActivity.CURRENT_THEME);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        Preference pref = (Preference) findPreference("pref_no_of_teams");
        
        // First of all set the summary to the initial number of teams
    	int default_n_teams = getResources().getInteger(R.integer.no_of_Teams_default);
    	int no_of_teams = pref.getSharedPreferences().getInt(SettingsActivity.KEY_NO_OF_TEAMS, default_n_teams);
    	pref.setSummary(String.valueOf(no_of_teams) + " teams as default.");
    	
    	// Secondly add the functionality to change it
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			
			public boolean onPreferenceClick(Preference arg0) {
				// Increment the current value
		    	SharedPreferences settings = arg0.getSharedPreferences();
		    	int default_n_teams = getResources().getInteger(R.integer.no_of_Teams_default);
		    	int no_of_teams = settings.getInt(SettingsActivity.KEY_NO_OF_TEAMS, default_n_teams)%getResources().getInteger(R.integer.maxTeams) + 1;

	        	arg0.setSummary(String.valueOf(no_of_teams) + " teams as default.");
	        	
	        	// Save incremented value
		    	SharedPreferences.Editor settingsEditor = settings.edit();
		    	settingsEditor.putInt(SettingsActivity.KEY_NO_OF_TEAMS, no_of_teams);
		    	settingsEditor.commit();

				return false;
			}
		});
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

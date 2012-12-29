package com.example.tutorialapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {
	LinearLayout layout;
	ArrayList<ArrayList<String>> teams;
	int noOfTeams = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {

    	// Assign a theme and number of teams according to settings or default
        setTheme(MainActivity.CURRENT_THEME);
        loadSettings();
        
        super.onCreate(savedInstanceState);

        // Get the message from the intent
        Intent intent = getIntent();
        ArrayList<String> players = intent.getStringArrayListExtra(MainActivity.EXTRA_DATA);
        
		teams = (ArrayList<ArrayList<String>>) getLastNonConfigurationInstance();
        if (teams == null) {
        	// First randomize list sorting locally
        	//Time is the seed (maybe not ideal for security but perfect for this)
	        Random generator = new Random(System.currentTimeMillis());
        	Collections.shuffle(players, generator);
        	
        	// After that, the team for each player 'i' is "i % noOfTeams"
        	//(...+1 just for human comprehension)
        	teams = new ArrayList<ArrayList<String>>();
        	for (int i=0; i< noOfTeams; i++) // Create lists
        		teams.add(new ArrayList<String>());
        	for (int i=0; i< players.size(); i++) // Fill lists
        		teams.get(i%noOfTeams).add(players.get(i));
        }
        // Set the text view as the activity layout
        setContentView(R.layout.activity_display_message);
        setUpListView(teams); // ALWAYS AFTER "setContentView"
    }

// Precondition: The "setContentView" method must be already run
    private void setUpListView(ArrayList<ArrayList<String>> teams){
    	// A list of ArrayAdapters is needed for the layout lists
        ArrayList<ArrayAdapter<String>> powerAdapterNineThousen = new ArrayList<ArrayAdapter<String>>();
    	for (int i=0; i<teams.size(); i++)
    		powerAdapterNineThousen.add(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teams.get(i)));

		String word_team = getResources().getString(R.string.word_team);
		float text_size = getResources().getDimension(R.dimen.Title1);
		
		// Create layout configuration
		LayoutParams grid01_layout = new LayoutParams();
		grid01_layout.width = LayoutParams.MATCH_PARENT;
		grid01_layout.height = LayoutParams.WRAP_CONTENT;

        LinearLayout left_layout = (LinearLayout) findViewById(R.id.teamColumnLeft);
        for (int i=0; i<teams.size(); i+=2) { // Even team numbers
        	// Create views
            TextView team_title = new TextView(this);
    		GridView gridView01 = new GridView(this);

    		// First of all the team number:
            team_title.setText(word_team.concat(String.valueOf(i+1)));
            team_title.setTextSize(text_size);

            if ((i/2)%2==0)
    			gridView01.setBackgroundColor(Color.GRAY);
    		else
    			gridView01.setBackgroundColor(Color.LTGRAY);
    		gridView01.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teams.get(i)));
    		gridView01.setLayoutParams(grid01_layout);
    		
    		// Add both Views to the viewGroup in the correct order
    		left_layout.addView(team_title);
    		left_layout.addView(gridView01, grid01_layout);
        }
        
        LinearLayout right_layout = (LinearLayout) findViewById(R.id.teamColumnRight);
        for (int i=1; i<teams.size(); i+=2) { // Odd team numbers
        	// Create views
            TextView team_title = new TextView(this);
    		GridView gridView01 = new GridView(this);

    		// First of all the team number:
            team_title.setText(word_team.concat(String.valueOf(i+1)));
            team_title.setTextSize(text_size);

            // Config the list view
    		if ((i/2)%2==1)
    			gridView01.setBackgroundColor(Color.GRAY);
    		else
    			gridView01.setBackgroundColor(Color.LTGRAY);
    		gridView01.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teams.get(i)));

    		// Add both Views to the viewGroup in the correct order
    		right_layout.addView(team_title);
    		right_layout.addView(gridView01);
        }
        
        // TODO: I'm following this right now: http://stackoverflow.com/questions/3506103/how-can-i-automatically-size-listview-so-it-doesnt-scroll
//        Log.e("TeamShuffle Exception", "Found the following exception: " + e);

    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        final ArrayList<ArrayList<String>> data = teams;
        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_display_message, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
	        case R.id.menu_settings:
	        	Intent intent = new Intent(this, SettingsActivity.class);
	            startActivity(intent);
	        	return true;
            case R.id.menu_back:
            	finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void loadSettings(){
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
 	   int default_n_teams = getResources().getInteger(R.integer.no_of_Teams_default);
 	   noOfTeams = settings.getInt(SettingsActivity.KEY_NO_OF_TEAMS, default_n_teams);
    }
    


}

package com.example.tutorialapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.example.tutorialapp.R.dimen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

// TODO: Feature: remember old names. But being able to remove them or clean.

public class DisplayMessageActivity extends Activity {
	LinearLayout layout;
	ArrayList<ArrayList<String>> teams;
    @Override
    public void onCreate(Bundle savedInstanceState) {

    	// Assign a theme according to settings or default
        setTheme(MainActivity.CURRENT_THEME);
        
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
        	final int noOfTeams = 5;
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
    // TODO: poner la opción CLOSE en el menú inferior dentro de la segunda actividad

// Precondition: The "setContentView" method must be already run
    private void setUpListView(ArrayList<ArrayList<String>> teams){
    	// A list of ArrayAdapters is needed for the layout lists
        ArrayList<ArrayAdapter<String>> powerAdapterNineThousen = new ArrayList<ArrayAdapter<String>>();
    	for (int i=0; i<teams.size(); i++)
    		powerAdapterNineThousen.add(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teams.get(i)));

        // Defining layout parameters to use:
		LinearLayout.LayoutParams title_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		String word_team = getResources().getString(R.string.word_team);
		float text_size = getResources().getDimension(R.dimen.Title1);

        LinearLayout left_layout = (LinearLayout) findViewById(R.id.teamColumnLeft);
        for (int i=0; i<teams.size(); i+=2) { // Odd team numbers
        	// First of all the team number:
            TextView team_title = new TextView(this);
            team_title.setLayoutParams(title_params);
            team_title.setText(word_team.concat(String.valueOf(i+1)));
            team_title.setTextSize(text_size);
            left_layout.addView(team_title);
        	
    		ListView listView01 = new ListView(this);
    		listView01.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teams.get(i)));
    		if ((i/2)%2==0)
    			listView01.setBackgroundColor(Color.GRAY);
    		else
    			listView01.setBackgroundColor(Color.LTGRAY);
    		listView01.setDividerHeight(0);
//    		listView01.setLayoutParams(layout_params);

    		// LayoutParams object must be created and set and included in the "addView" call
    		left_layout.addView(listView01, layout_params);
        }
        
        LinearLayout right_layout = (LinearLayout) findViewById(R.id.teamColumnRight);
        for (int i=1; i<teams.size(); i+=2) { // Even team numbers
        	// First of all the team number:
            TextView team_title = new TextView(this);
            team_title.setLayoutParams(title_params);
            team_title.setText(word_team.concat(String.valueOf(i+1)));
            team_title.setTextSize(text_size);
            right_layout.addView(team_title);
        	
    		ListView listView01 = new ListView(this);
    		listView01.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teams.get(i)));
    		if ((i/2)%2==1)
    			listView01.setBackgroundColor(Color.GRAY);
    		else
    			listView01.setBackgroundColor(Color.LTGRAY);
    		listView01.setDividerHeight(0);
//    		listView01.setLayoutParams(layout_params);

    		right_layout.addView(listView01, layout_params);
        }

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
            case R.id.menu_close:
                finish(); // TODO: This is only for the activity; i.e. it means back
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

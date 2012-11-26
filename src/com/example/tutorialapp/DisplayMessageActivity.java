package com.example.tutorialapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
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
        	final int noOfTeams = 2;
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
    
    private LinearLayout createLayout(){
    	LinearLayout layout = new LinearLayout(this);

    	// Set up layout
        layout.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
//        layout.setBackgroundColor(R.color.blue);
    	
    	// Write a title
        TextView question = new TextView(this);
        question.setLayoutParams(new ViewGroup.LayoutParams(-1,-2));
        question.setTextColor(Color.GREEN); // getResources().getColor(R.);
        question.setTextSize(1,14);
        question.setText("This is question1");
        layout.addView(question);
    	
    	return layout;
    }
    // TODO: poner la opción CLOSE en el menú inferior dentro de la segunda actividad

// Precondition: The "setContentView" method must be already run
    private void setUpListView(ArrayList<ArrayList<String>> teams){
    	// A list of ArrayAdapters is needed for the layout lists
        ArrayList<ArrayAdapter<String>> powerAdapterNineThousen = new ArrayList<ArrayAdapter<String>>();
    	for (int i=0; i<teams.size(); i++)
    		powerAdapterNineThousen.add(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teams.get(i)));

        // TODO: linear_layout tiene que ser horizontal imbécil ¬¬ y copiar las propiedades de los listView (del código)
        LinearLayout linear_layout = (LinearLayout) findViewById(R.id.listViewLayout);
    	for (int i=0; i<teams.size(); i++) {
    		ListView listView01 = new ListView(this);
    		listView01.setClickable(false);
    		listView01.setLongClickable(false);
    		listView01.setDuplicateParentStateEnabled(false);
    		listView01.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teams.get(i)));

    		// LayoutParams object must be created and set before
    		LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT ,1);
    		linear_layout.addView(listView01, layout_params);
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

}
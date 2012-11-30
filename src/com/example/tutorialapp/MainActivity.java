package com.example.tutorialapp;

import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public final static String EXTRA_DATA = "com.example.myfirstapp.PLAYER_LIST";
    public final static String PREFS_NAME = "team_shuffle_settings";
    
    public final static int DEFAULT_THEME = R.style.theme_sportive;
    public static int CURRENT_THEME = R.style.theme_sportive;
    public int no_of_teams;
    public boolean ask_for_teams;

    private ArrayList<String> players;
    private ArrayAdapter<String> listAdapter;
    private ListView listView;
    
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        // Restore application settings
    	restoreSettings();

    	// Assign a theme according to settings or default
        setTheme(CURRENT_THEME);
        
        // Calling the super class and finally setting the layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		players = (ArrayList<String>) getLastNonConfigurationInstance();
        if (players == null) {
        	players = new ArrayList<String>();
        	players.clear();
        }
        
        setUpListView();
    }
    
    // Method to restore all the application settings
    private void restoreSettings(){
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        // Restoring the number of teams for each shuffling
    	String default_n_teams = getResources().getString(R.string.default_n_teams);
        no_of_teams = Integer.valueOf(settings.getString(SettingsActivity.KEY_NO_OF_TEAMS, default_n_teams));
        
        // Restoring the user choice about asking the no. of teams
    	String default_ask_teams = getResources().getString(R.string.default_ask_teams);
        ask_for_teams = Boolean.getBoolean(settings.getString(SettingsActivity.KEY_ASK_FOR_TEAMS, default_ask_teams));
    	
    	// Restoring the theme to show
        String theme_name = settings.getString(SettingsActivity.KEY_PREF_THEME, "_sportive");
        if (theme_name.contains("_dark"))
        	CURRENT_THEME = R.style.theme_dark;
        else if (theme_name.contains("_light"))
        	CURRENT_THEME = R.style.theme_light;
        else if (theme_name.contains("_subtle"))
        	CURRENT_THEME = R.style.theme_subtle;
        else if (theme_name.contains("_sportive"))
        	CURRENT_THEME = R.style.theme_sportive;
    }
    
    // UNUSED Method to store settings after changes
    private void storeSettings(){
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("pref_theme", R.style.theme_light);

        // Commit the edits!
        editor.commit();
    }
    
    private void setUpListView(){
    	listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, players);
        listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(listAdapter);

        // Registering so a context menu is
        //launched for the items in the list.
        registerForContextMenu(listView);
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        final ArrayList<String> data = players;
        return data;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /** Called when the user clicks to add a new player */
    public void addPlayer(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        if (message.compareTo("") != 0) {
	        editText.setText("");
	        editText.clearComposingText();
	        players.add(message);
    		listAdapter.notifyDataSetChanged();
        }
    }
    
    /** Called when the user clicks to make teams finally */
    public void makeTeams(View view) {
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_DATA, players);
        startActivity(intent);
    }
    
    // For the context menu for handling each item in the list
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	if (R.id.listView1 == v.getId()) {
	    	MenuInflater inflater = getMenuInflater();
	    	inflater.inflate(R.menu.main_list_floating, menu);
    	}
    }

    // Defining options for the list context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_flt_delete:
                for (int i=0; i< listView.getCount(); i++)
                	if (listView.getItemIdAtPosition(i) == info.id) {
                		players.remove(i);
                		listAdapter.notifyDataSetChanged();
                		return true;
                	}
                return false;
            case R.id.menu_flt_handic:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    
    // For other activities to be able to know the current themeId
    public int getThemeId(){
    	return CURRENT_THEME;
    }
}

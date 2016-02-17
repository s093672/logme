package com.example.logme;

import com.example.logme.Total;
import com.example.logme.Weekly;
import com.example.logme.db.App;
import com.example.logme.db.DBHandler;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class LogMeActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_me);
		/*Intent pushIntent = new Intent(this, MyService.class);
		this.startService(pushIntent);
		*/
		/*
		DBHandler db = new DBHandler(this);
		
		db.addStartApp(new App("0", "21:40:23"));
		db.addEndApp(new App("test", "1000", "08/05/2014", "21:53:23"));
		
		
		db.addStartApp(new App("3000", "19:20:23"));
		db.addEndApp(new App("facebook", "6000000", "02/05/2014", "20:00:00"));
		
		db.addStartApp(new App("0", "19:20:23"));
		db.addEndApp(new App("jin", "600000", "01/01/2014", "20:00:00"));
		
		db.addStartApp(new App("0", "19:20:23"));
		db.addEndApp(new App("peter", "12000", "02/02/2014", "20:00:00"));
		*/
		
		TabHost tabHost = getTabHost();

		// Tab for Daily
		TabSpec dailyspec = tabHost.newTabSpec("Daily");
		dailyspec.setIndicator("Daily");
		Intent dailyIntent = new Intent(this, Daily.class);
		dailyspec.setContent(dailyIntent);

		// Tab for Weekly
		TabSpec weeklyspec = tabHost.newTabSpec("Weekly");        
		weeklyspec.setIndicator("Weekly");
		Intent weeklyIntent = new Intent(this, Weekly.class);
		weeklyspec.setContent(weeklyIntent);

		// Tab for Total
		TabSpec totalspec = tabHost.newTabSpec("Total");
		totalspec.setIndicator("Total");
		Intent totalIntent = new Intent(this, Total.class);
		totalspec.setContent(totalIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(dailyspec); // Adding daily tab
		tabHost.addTab(weeklyspec); // Adding weekly tab
		tabHost.addTab(totalspec); // Adding total tab

		// Choosing which tab to show
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
		String from = extras.getString("from");
		if(from.equals("graphWeekly")){
			tabHost.setCurrentTab(1);
		}}
		else{
		tabHost.setCurrentTab(0);
		}
	}
	//For testing purpose only! 	
	/*
	DBHandler db = new DBHandler(this);
	
	// Reading all contacts
    Log.d("Reading: ", "Reading all contacts.."); 
    List<App> apps = db.getAllApps();       
     
    for (App cn : apps) {
        String log = "Id: "+cn.getId()+" ,AppName: " + cn.getName() + " ,Date: " + cn.getDate() + " ,startTimeofday: " + cn.getStartTimeofday() + " ,endTimeofday: " + cn.getEndTimeofday() + " ,Timespend: " + cn.getTimeSpend();
            // Writing Contacts to log
    Log.d("Databaselogging1", log);
}*/

}
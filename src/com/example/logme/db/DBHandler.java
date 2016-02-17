package com.example.logme.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {
	
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "appsManager";

    // Apps table name
    private static final String TABLE_APPS = "apps";

    // Apps Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_APPNAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_STARTTIMEOFDAY = "starttimeofday";
    private static final String KEY_ENDTIMEOFDAY = "endtimeofday";
    private static final String KEY_TIMESPEND = "timespend";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_APPS_TABLE = "CREATE TABLE " + TABLE_APPS + "(" 
				+ KEY_ID + " INTEGER PRIMARY KEY," 
				+ KEY_APPNAME + " TEXT," 
				+ KEY_DATE + " TEXT," 
				+ KEY_STARTTIMEOFDAY + " TEXT,"
				+ KEY_ENDTIMEOFDAY + " TEXT,"
				+ KEY_TIMESPEND + " TEXT" + ")";
		db.execSQL(CREATE_APPS_TABLE);
	}

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    
    
    long startTime; 
    String startTimeOfDay;
    
	public void addStartApp(App app) {
		startTime = Long.parseLong(app.getTimeStamp(),10);
		startTimeOfDay = app.getStartTimeofday();
	}
    
    //Add an apps info to the database
    public void addEndApp(App app){
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	//Now we need to specify what should be added to each column
    	ContentValues values = new ContentValues();
    	values.put(KEY_APPNAME,app.getName());
    	values.put(KEY_DATE, app.getDate());
    	values.put(KEY_STARTTIMEOFDAY, startTimeOfDay);
    	values.put(KEY_ENDTIMEOFDAY, app.getEndTimeofday());
    	long endTime = Long.parseLong(app.getTimeStamp(),10);
    	long timeSpend = (endTime - startTime)/1000;
    	values.put(KEY_TIMESPEND, timeSpend);
    	
        // Inserting Row
        db.insert(TABLE_APPS, null, values);
        db.close(); // Closing database connection
    }
    
    // Gets a total count on every entry in the database
    public int getAppsCount() {
        String countQuery = "SELECT * FROM " + TABLE_APPS;
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }
    
    // Get all logs
    public List<App> getAllApps() {
        List<App> appList = new ArrayList<App>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_APPS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
       
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                App app = new App();
                app.setId(Integer.parseInt(cursor.getString(0)));
                app.setName(cursor.getString(1));
                app.setDate(cursor.getString(2));
                app.setStartTimeofday(cursor.getString(3));
                app.setEndTimeofday(cursor.getString(4));
                app.setTimeSpend(cursor.getString(5));
                
                // Adding app to list
                appList.add(app);
            } while (cursor.moveToNext());
        }
 
        // return app list
        return appList;
    }
    
    // Get all logs for a specific day
    public List<App> getAllAppsFromDate(String date) {
        List<App> appList = new ArrayList<App>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_APPS +" WHERE date=" + "'" + date + "'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                App app = new App();
                app.setId(Integer.parseInt(cursor.getString(0)));
                app.setName(cursor.getString(1));
                app.setDate(cursor.getString(2));
                app.setStartTimeofday(cursor.getString(3));
                app.setEndTimeofday(cursor.getString(4));
                app.setTimeSpend(cursor.getString(5));
                
                // Adding app to list
                appList.add(app);
            } while (cursor.moveToNext());
        }
 
        // return app list
        return appList;
    }
    
 // Get all logs for a specific day
    public int getAppTotalFromDate(String date, String appname) {
        // Select All Query
    	String selectQuery = "SELECT  SUM(timespend) FROM " + TABLE_APPS +" WHERE date=" + "'" + date + "'" + " AND " + "name=" + "'" + appname + "'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        int result = 0;
        if (cursor.moveToFirst()) {
            do {
            	result = cursor.getInt(0);
                // Adding app to list
            } while (cursor.moveToNext());
        }
        return result;
    }
    
 // Get all logs for a specific day
    public List<App> getAllEntryOfAppFromDate(String date, String appname) {
    	List<App> appList = new ArrayList<App>();
        // Select All Query
    	String selectQuery = "SELECT  * FROM " + TABLE_APPS +" WHERE date=" + "'" + date + "'" + " AND " + "name=" + "'" + appname + "'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
            	App app = new App();
                app.setId(Integer.parseInt(cursor.getString(0)));
                app.setId(Integer.parseInt(cursor.getString(0)));
                app.setName(cursor.getString(1));
                app.setDate(cursor.getString(2));
                app.setStartTimeofday(cursor.getString(3));
                app.setEndTimeofday(cursor.getString(4));
                app.setTimeSpend(cursor.getString(5));
                
                appList.add(app);
            } while (cursor.moveToNext());
        }
        return appList;
    }
    
    // Get all logs for a specific day
    public List<String> getDistinctAppsforDate(String date) {
    	List<String> appnameList = new ArrayList<String>();
    	// Select All Query
    	String selectQuery = "SELECT  DISTINCT name FROM " + TABLE_APPS +" WHERE date=" + "'" + date + "'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        String result;
        if (cursor.moveToFirst()) {
            do {
            	result = cursor.getString(0);
                // Adding app to list
            	appnameList.add(result);
            } while (cursor.moveToNext());
        }
        return appnameList;
    }
    
    public List<App> getAllAppsFromWeek() {
    	List<App> appList = new ArrayList<App>();
 
		for(int i = -6; i <= 0; i++){
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_YEAR, i);
			Date daysBeforeDate = cal.getTime();
			
			int pastDate = daysBeforeDate.getDate();
			int pastMonth = (daysBeforeDate.getMonth()+1);
			int year = cal.get(Calendar.YEAR);
			
			String endDate = converter(pastDate) + "/" + converter(pastMonth) + "/" + year;
			Log.d("endDateString", endDate);
			String selectQuery = "SELECT  * FROM " + TABLE_APPS +" WHERE date=" + "'" + endDate + "'";
			Log.d("querystring", selectQuery);
			 
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);
	 
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	                App app = new App();
	                app.setId(Integer.parseInt(cursor.getString(0)));
	                app.setId(Integer.parseInt(cursor.getString(0)));
	                app.setName(cursor.getString(1));
	                app.setDate(cursor.getString(2));
	                app.setStartTimeofday(cursor.getString(3));
	                app.setEndTimeofday(cursor.getString(4));
	                app.setTimeSpend(cursor.getString(5));
	                
	                // Adding app to list
	                appList.add(app);
	            } while (cursor.moveToNext());
	        }
	        cursor.close();
		}
    	return appList;
    }
    
    public List<String> getDistinctAppNamesFromWeek(String currentDate, String endDate) {
    	//List<App> appList = new ArrayList<App>();
    	List<String> appnameList = new ArrayList<String>();
 
		String selectQuery = "SELECT  DISTINCT name FROM " + TABLE_APPS +" WHERE date" + " BETWEEN " + "'" + endDate + "'" + " AND " + "'" + currentDate + "'";
		Log.d("Weekly", selectQuery);
			 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    Log.d("Weekly", "After cursor... ");
	    
	    // looping through all rows and adding to list
	    String result;
	    if (cursor.moveToFirst()) {
            do {
            	result = cursor.getString(0);
            	Log.d("Weekly", "result..." + result);
                // Adding app to list
            	appnameList.add(result);
            } while (cursor.moveToNext());
        }
		
    	return appnameList;
    }
       
    public String converter(int input){
    	String output = "";
    	if (input < 10 ){
			output = "0" + input;
		}else{
			output = "" + input;
		}
    	return output;
    }
    
    public int getAppTimeFromWeeks(String appname, String currentDate, String endDate) {
    	// Select All Query
    	String selectQuery = "SELECT  SUM(timespend) FROM " + TABLE_APPS +" WHERE " + "date" + " BETWEEN " + "'"+ endDate +"'" + " AND " + "'" + currentDate + "'" +" AND "+  "name=" + "'" + appname + "'";
    	Log.v("FUCKYOU", selectQuery);
    	
    	Log.v("FUCKYOU", "Before cursor...");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.v("FUCKYOU", "after cursor...");
        
        int result = 0;
        if (cursor.moveToFirst()) {
            do {
            	result = cursor.getInt(0);
                // Adding app to list
            } while (cursor.moveToNext());
        }
        return result;
    }
    
    public List<String> getTotalApps() {

    	List<String> appnameList = new ArrayList<String>();
    	// Select All Query
    	String selectQuery = "SELECT  DISTINCT name FROM " + TABLE_APPS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        String result;
        if (cursor.moveToFirst()) {
            do {
            	result = cursor.getString(0);
                // Adding app to list
            	appnameList.add(result);
            } while (cursor.moveToNext());
        }
        return appnameList;
    }
    
    public int getTotalAppsSum(String appname) {
        // Select All Query
    	String selectQuery = "SELECT  SUM(timespend) FROM " + TABLE_APPS +" WHERE " +"name=" + "'" + appname + "'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        int result = 0;
        if (cursor.moveToFirst()) {
            do {
            	result = cursor.getInt(0);
                // Adding app to list
            } while (cursor.moveToNext());
        }
        return result;
    }
}

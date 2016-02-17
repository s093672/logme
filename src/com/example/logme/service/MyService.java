package com.example.logme.service;

import java.util.Calendar;
import java.util.List;

import com.example.logme.db.App;
import com.example.logme.db.DBHandler;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service{
	
	DBHandler db;
	Boolean flag = true;
	String preName = "";
	String curName = "";
	int counter = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("StartServiceAtBoot", "StartAtBootService Created");
		
		//initialise the database
		db = new DBHandler(this);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("StartServiceAtBoot", "StartAtBootService -- onStartCommand()");	
		
		final Handler mHandler = new Handler();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try{
						//2 sec sleep
						Thread.sleep(2000);
						
						mHandler.post(new Runnable() {
							
							@Override
							public void run() {
								/* Jins Version
								ActivityManager actMngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
								
								List<RunningTaskInfo> runningAppTasks = actMngr.getRunningTasks(1);
								curName = runningAppTasks.get(0).topActivity.getPackageName();*/
								ActivityManager actMngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
								List<RunningAppProcessInfo> runningAppProcesses = actMngr.getRunningAppProcesses();
								
//								//log the first entry
						        curName = runningAppProcesses.get(0).processName;
						        
						        curName = getAppName(curName);
						        
						        //first time an app is detected in the foregound
						        if(flag){
						        	db.addStartApp(new App(getTimeStamp(), getTimeofday()));
						        	flag = false;
						        }
						        //when a new app comes to the foreground
						        if (!curName.equals(preName)) {
						        	//Log end time of previous app
						        	db.addEndApp(new App(preName, getTimeStamp(), getDate(), getTimeofday()));
						        	//Log start time of new app
						        	db.addStartApp(new App(getTimeStamp(), getTimeofday()));
								}
						        
						        preName = curName;
							}
						});
					} catch (Exception e){	
					}
				}
			}
		}).start();
		
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("StartServiceAtBoot", "StartAtBootService Destroyed");
		
		// Special case if closing service
		// Added 05-05-2014 20:37 - Not tested ! 
		db.addEndApp(new App(preName, getTimeStamp(), getDate(), getTimeofday()));
	}
	
	public String getTimeofday(){
		Calendar cal = Calendar.getInstance();
		
		String sHour, sMin, sSec;
		int hour, min, sec, timeInSec;
		hour = cal.get(Calendar.HOUR_OF_DAY)*(60*60);
		min = cal.get(Calendar.MINUTE)*60;
		sec = cal.get(Calendar.SECOND);
		/*
		if(hour< 10){
			sHour = "0" + hour;
		}else{
			sHour = "" + hour;
		}
		
		if(min< 10){
			sMin = "0" + min;
		}else{
			sMin = "" + min;
		}
		
		if(sec< 10){
			sSec = "0" + sec;
		}else{
			sSec = "" + sec;
		}
		*/
		
		timeInSec = hour + min + sec;
		String timeofday = "" + timeInSec;
		Log.d("dateoftheday", timeofday);
		return timeofday;
	}
	
	public String getDate(){
		Calendar cal = Calendar.getInstance();
		String date, sDay,sMonth;
		int day, month, year;
		
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH)+1;
		year = cal.get(Calendar.YEAR);
		
		if(day < 10){
			sDay = "0" + day;
		}else{
			sDay = "" + day;
		}
		
		if(month < 10){
			sMonth = "0" + month;
		}else{
			sMonth = "" + month;
		}

		date = sDay + "/" + sMonth + "/" +  year;
		
		Log.d("dateoftheday", date);
    	return date;
	}
	
	public String getTimeStamp(){
		long tmp = System.currentTimeMillis();
		String timestamp = "" + tmp; 
		return timestamp;
	}
	
	public String getAppName(String packageName){
		
		PackageManager pm = getApplicationContext().getPackageManager();
		ApplicationInfo ai;
		try {
			ai = pm.getApplicationInfo(packageName, 0);
		}catch (NameNotFoundException e){
			ai = null;
		}
		String appName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
		
		return appName;
	}
}

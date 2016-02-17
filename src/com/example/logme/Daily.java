package com.example.logme;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.logme.graphDaily;
import com.example.logme.db.DBHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Daily extends Activity {


	@Override
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily);
		
		//initialise the database
		DBHandler db = new DBHandler(this);
		String currentDate = getDate();
	    List<String> lTitle = db.getDistinctAppsforDate(currentDate);       
	    List<Double> lTime = new ArrayList<Double>();
	    for (String cn : lTitle) {
	    	
	    	double totalTime = db.getAppTotalFromDate(currentDate, cn);
	    	
	    	lTime.add(totalTime);
	    	
		}
	    
	    String[] title = lTitle.toArray(new String[lTitle.size()]);
	    Double[] time = lTime.toArray(new Double[lTime.size()]);

	    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.daily);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;

		//Sorting the data to show the most used apps at the top.
		Double tmp1 = 0.0;
		String tmp2 = "";
		for(int i = 0;i<time.length;i++){
			for(int j = (time.length-1);j>=(i+1);j--){
				if(time[j]>time[j-1]){
					tmp1 = time[j];
					tmp2 = title[j];
					time[j]=time[j-1];
					title[j]=title[j-1];
					time[j-1]=tmp1;
					title[j-1]=tmp2;
				}
			}
		}

		//Calculating the sum of time used
		//TODO
		double sum=0;
		for (int i = 0; i < time.length; i++) {
			sum+=time[i];
		}

		//Adding views for each app. Text view for title and linearLayout for progressbar
		for (int i = 0; i < title.length; i++) {
			final String appName = title[i];
			TextView tempText = new TextView(this);
			tempText.setText(title[i]);
			tempText.setX(width/10);
			tempText.setTextSize(20);
			

			//If the user clicks on the text or bar the user will be send to the weekly graph
			tempText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent myIntent=new Intent(v.getContext(),graphDaily.class);
					myIntent.putExtra("appname", appName);
					startActivity(myIntent);
				}
			});

			//Drawing the progressbar
			int barWidth = width-(width/5);
			linearLayout.addView(tempText);
			LinearLayout tempLinear = new LinearLayout(this);
			Paint paint = new Paint();
			Paint paintBackground = new Paint();
			Paint paintText = new Paint();
			paint.setColor(Color.parseColor("#77dbdf"));
			paintBackground.setColor(Color.WHITE);
			paintText.setColor(Color.BLACK);
			paintText.setTextSize(30);
			paintText.setTextAlign(Align.CENTER);
			Bitmap bg = Bitmap.createBitmap(width, 75, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bg);
			RectF r1 = new RectF(width/10,0,barWidth+(width/10),75);
			canvas.drawRoundRect(r1, 10, 10, paintBackground);
			//TODO
			RectF r2 = new RectF(width/10,0,(float) (width/10+time[i]/sum*barWidth),75);
			canvas.drawRoundRect(r2, 10, 10, paint);
			//TODO
			String hours = "";
			String min = "";
			String sec = "";
			if(!Integer.toString((int) (time[i]/3600)).equals("0")){
				hours = Integer.toString((int) (time[i]/3600))+" h ";
			}
			if(!Integer.toString((int) (time[i]%3600)/60).equals("0")){
				min = Integer.toString((int) (time[i]%3600)/60)+" m ";
			}
			if(!Integer.toString((int) (time[i]%3600)%60).equals("0")){
				sec = Integer.toString((int) (time[i]%3600)%60)+" s ";
			}
			canvas.drawText(hours+min+sec, width/2, 50, paintText);
			tempLinear.draw(canvas);
			tempLinear.setBackgroundDrawable(new BitmapDrawable(bg));
			tempLinear.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent myIntent=new Intent(v.getContext(),graphDaily.class);
					myIntent.putExtra("appname", appName);
					startActivity(myIntent);
				}
			});
			linearLayout.addView(tempLinear);

		}
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
}
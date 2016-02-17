package com.example.logme;

import java.util.Calendar;

import com.example.logme.LogMeActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class graphWeekly extends Activity {
	@Override
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph_weekly);
		
		//Width and height of the window used later
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;

		Button back = (Button)findViewById(R.id.backButtonWeekly);
		Bundle extras = getIntent().getExtras();
		
		//The name of the app shown in the graph
		final String title = extras.getString("appname");


		//TODO get data for selected appname


		if(title.equals("Facebook")){
			System.err.println("yehhaaa");
		}

		/*If back button is clicked the app will
		 *go to the main screen. Using putExtra
		 *the main screen knows which tab to show
		 **/
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent=new Intent(v.getContext(),LogMeActivity.class);
				myIntent.putExtra("from", "graphWeekly");
				startActivity(myIntent);
			}
		});
		
		//Gridlayout is set to 9 columns. 2 for the y-axes and 7 for each day in the last week
		GridLayout graph = (GridLayout) findViewById(R.id.graphWeekly);
		graph.setColumnCount(9);
		int columnSize = width/9;
		
		//Lower border of the screen
		int lowerBorder = height*3/5;

		//TODO Get data
		String[] date = {"5/5","6/5","7/5","8/5","9/5","10/5","11/5"};
		double[] time = {3920,8239,5920,6902,2728,6932,9623};

		//Finding the max value of the 7 days
		Double max = 0.0;
		for ( int i = 0; i < time.length; i++) {

			//TODO lav om til timer hvis det er milisekunder
			time[i] = time[i]/3600;
			if ( time[i] > max) {
				max = time[i];
			}
		}
		
		//Setting title of graph
		TextView textTitle = (TextView) findViewById(R.id.titleOfAppWeekly);
		textTitle.setText(title);
		
		//Drawing the left y axis
		LinearLayout.LayoutParams leftTextParams = new LinearLayout.LayoutParams(columnSize,height);
		LinearLayout leftText = new LinearLayout(this);
		Paint paintText = new Paint();
		paintText.setColor(Color.GRAY);
		paintText.setTextSize(20);
		paintText.setTextAlign(Align.CENTER);
		Bitmap bitmapTextBar = Bitmap.createBitmap(columnSize,height, Bitmap.Config.ARGB_8888);
		Canvas canvasLeftText = new Canvas(bitmapTextBar);
		//The max value make sure that the value of the axis is balanced
		int s = max.intValue();
		for (int i = 0; i < (s+1); i++) {
			canvasLeftText.drawText(Integer.toString(s-(s-(i*1))), columnSize/2, (float) (lowerBorder-(i*(lowerBorder/max))), paintText);
		}
		canvasLeftText.drawText("hours", columnSize/2, 15, paintText);
		leftText.draw(canvasLeftText);
		leftText.setBackgroundDrawable(new BitmapDrawable(bitmapTextBar));
		graph.addView(leftText,0,leftTextParams);


		//Drawing the 7 days bars
		for (int i = 0; i < date.length; i++) {
			final String dateName = date[i];
			LinearLayout column = new LinearLayout(this);
			LinearLayout.LayoutParams columnParams = new LinearLayout.LayoutParams(columnSize,height);
			Paint paint = new Paint();
			paint.setColor(Color.parseColor("#77dbdf"));
			Bitmap bitmapBar = Bitmap.createBitmap(columnSize,height, Bitmap.Config.ARGB_8888);
			Canvas canvasBar = new Canvas(bitmapBar);
			//This is where the size of the rectangle is calculated
			float drawUpTo = (float) ((lowerBorder)-(time[i]/(max/(lowerBorder))));
			canvasBar.drawRect(10,drawUpTo, columnSize-10,lowerBorder, paint);
			canvasBar.drawText(date[i], columnSize/2, (lowerBorder)+30, paintText);
			column.draw(canvasBar);
			column.setBackgroundDrawable(new BitmapDrawable(bitmapBar));
			
			//Putting listeners on the bars to go to the specific day
			column.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent myIntent=new Intent(v.getContext(),graphDaily.class);
					myIntent.putExtra("appname", title);
					myIntent.putExtra("date", dateName);
					myIntent.putExtra("from", "graphWeekly");
					startActivity(myIntent);
				}
			});
			graph.addView(column,(i+1),columnParams);
		}

		//Drawing the right y axis
		LinearLayout.LayoutParams RightTextParams = new LinearLayout.LayoutParams(columnSize,height);
		LinearLayout RightText = new LinearLayout(this);
		Canvas canvasRightText = new Canvas(bitmapTextBar);
		for (int i = 0; i < (s+1); i++) {
			canvasRightText.drawText(Integer.toString(s-(s-(i*1))), columnSize/2, (float) (lowerBorder-(i*(lowerBorder/max))), paintText);
		}
		canvasRightText.drawText("hours", columnSize/2, 15, paintText);
		RightText.draw(canvasRightText);
		RightText.setBackgroundDrawable(new BitmapDrawable(bitmapTextBar));
		graph.addView(RightText,8,RightTextParams);
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

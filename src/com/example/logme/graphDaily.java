package com.example.logme;

import java.util.ArrayList;
import java.util.List;

import com.example.logme.LogMeActivity;
import com.example.logme.graphWeekly;
import com.example.logme.db.App;
import com.example.logme.db.DBHandler;

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
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class graphDaily extends Activity {
	@Override
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph_daily);
		

		
		//Width and height of the window used later
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;

		Button back = (Button)findViewById(R.id.backButtonDaily);
		Bundle extras = getIntent().getExtras();
		final String appName = extras.getString("appname");
		String date = extras.getString("date");
		final String from = extras.getString("from");
		
		
		//Database initialisation
		DBHandler db = new DBHandler(this);
		
		List<App> entryList = db.getAllEntryOfAppFromDate("11/05/2014", appName);
		List<Double> lTime = new ArrayList<Double>();
		
		for(App app: entryList){
			double startTime = Double.parseDouble(app.getStartTimeofday());
			double endTime = Double.parseDouble(app.getEndTimeofday());
			lTime.add(startTime);
			lTime.add(endTime);
		}
		Double[] time = lTime.toArray(new Double[lTime.size()]);
		
		if(date==null){
			date="Today";
		}

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(from==null){
				Intent myIntent=new Intent(v.getContext(),LogMeActivity.class);
				startActivity(myIntent);
			}else{
				Intent myIntent=new Intent(v.getContext(),graphWeekly.class);
				myIntent.putExtra("appname", appName);
				startActivity(myIntent);
				}
			}
		});

		//Lower border of the graph
		int lowerBorder = height*2/5;
		//Setting the title and date of the graph
		TextView text = (TextView) findViewById(R.id.titleOfAppDaily);
		text.setText(appName+"\n"+date);

		
		
		
		//Setting the scrollview
		ScrollView scrollView = (ScrollView) findViewById(R.id.daily_scrollview);
		LayoutParams scrollViewParams = scrollView.getLayoutParams();
		scrollViewParams.width = width/5;
		LinearLayout scrollViewLinear = (LinearLayout) findViewById(R.id.daily_scrollviewLinear);
		
		//Adding textView for each time the app have been used
		for (int i = 0; i < time.length; i=i+2) {
			TextView appUsed = new TextView(this);
			appUsed.setText(time[i]+" - "+time[i+1]);
			//TODO
			appUsed.setGravity(Gravity.CENTER);
			scrollViewLinear.addView(appUsed);
		}


		//Drawing the text for the graph
		LinearLayout graphView = (LinearLayout) findViewById(R.id.graph_daily);
		LayoutParams graphViewParams = graphView.getLayoutParams();
		graphViewParams.width = width*4/5;
		graphViewParams.height = height;
		double canvasWidth = width-(width/5);
		double canvasStart = width/10;
		int textSize = 20;
		Paint paintTextGraph = new Paint();
		paintTextGraph.setColor(Color.GRAY);
		paintTextGraph.setTextSize(textSize);
		paintTextGraph.setTextAlign(Align.CENTER);
		Bitmap bitmapGraph = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
		Canvas canvasGraph = new Canvas(bitmapGraph);
		//Drawing the hours from 0-24 = a whole day
		for (int i = 0; i <= 24; i++) {
			canvasGraph.drawText(Integer.toString(i), (float) (canvasStart+((canvasWidth/24)*(i))),lowerBorder+textSize, paintTextGraph);
		}
		canvasGraph.drawText("hours", (float) (canvasStart+(canvasWidth/2)),lowerBorder+(textSize*2), paintTextGraph);

		//Drawing the graph
		Paint paintGraph = new Paint();
		Paint paintBackground = new Paint();
		paintGraph.setColor(Color.parseColor("#77dbdf"));
		paintBackground.setColor(Color.WHITE);
		canvasGraph.drawRect((float) canvasStart,lowerBorder-height/5,(float) (canvasStart+canvasWidth), lowerBorder, paintBackground);
		double secSize = canvasWidth/(24*3600);
		//Drawing the rectangles defining app start and stop
		for (int i = 0; i < time.length; i=i+2) {
			double start = time[i]*secSize;
			double stop = time[i+1]*secSize;
			canvasGraph.drawRect((float) (start+canvasStart),lowerBorder-height/5, (float) (stop+canvasStart), lowerBorder, paintGraph);
		}
		graphView.draw(canvasGraph);
		graphView.setBackgroundDrawable(new BitmapDrawable(bitmapGraph));
		
	}	
}

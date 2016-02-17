package com.example.logme;

import java.util.ArrayList;
import java.util.List;

import com.example.logme.db.DBHandler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Total extends Activity {

	@Override
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.total);
		
		//initialise the database
		DBHandler db = new DBHandler(this);
		List<String> lTitle = db.getTotalApps();       
		List<Double> lTime = new ArrayList<Double>();
		for (String cn : lTitle) {
		   	
		   	double totalTime = db.getTotalAppsSum(cn);
			    	
		   	lTime.add(totalTime);
			    	
		}
			    
		String[] title = lTitle.toArray(new String[lTitle.size()]);
		Double[] time = lTime.toArray(new Double[lTime.size()]);
		System.err.println("Length of title = " + title.length);
	    System.err.println("Length of time = " + time.length);


		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;

//		//TODO Get data
//		String[] title = {"Facebook","Youtube","Candy Crush"};
//		double[] time = {46920,28239,32920};

		//Sorting the data to show the most used apps at the top.
				double tmp1 = 0;
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

				LinearLayout linearLayout = (LinearLayout) findViewById(R.id.total);
				//Adding views for each app. Text view for title and linearLayout for progressbar
				for (int i = 0; i < title.length; i++) {
					TextView textView = new TextView(this);
					textView.setText(title[i]);
					textView.setX(width/10);
					textView.setTextSize(20);
					linearLayout.addView(textView);

					//Progressbar
					int barWidth = width-(width/5);
					LinearLayout progressbarView = new LinearLayout(this);
					Paint paint = new Paint();
					Paint paintBackground = new Paint();
					Paint paintText = new Paint();
					paint.setColor(Color.parseColor("#77dbdf"));
					paintBackground.setColor(Color.WHITE);
					paintText.setColor(Color.BLACK);
					paintText.setTextSize(30);
					paintText.setTextAlign(Align.CENTER);
					Bitmap bitmapProgressBar = Bitmap.createBitmap(width, 75, Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(bitmapProgressBar);
					RectF r1 = new RectF(width/10,0,barWidth+(width/10),75);
					canvas.drawRoundRect(r1, 10, 10, paintBackground);
					//TODO
					RectF r2 = new RectF(width/10,0,(float) (width/10+time[i]/sum*barWidth),75);
					canvas.drawRoundRect(r2, 10, 10, paint);
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
					progressbarView.draw(canvas);
					progressbarView.setBackgroundDrawable(new BitmapDrawable(bitmapProgressBar));
					linearLayout.addView(progressbarView);
				}
			}

		}
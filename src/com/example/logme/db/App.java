package com.example.logme.db;

public class App{
	
	int _id;
	String _appname;
	String _timestamp;
    String _date;
    String _starttimeofday;
    String _endtimeofday;
    String _timespend;
	
	
	public App (){
		
	}
	
	public App(int id, String appname, String timestamp, String date, String starttimeofday, String endtimeofday, String timespend) {
		this._id = id;
		this._appname = appname;
		this._timestamp = timestamp;
		this._date = date;
		this._starttimeofday = starttimeofday;
		this._endtimeofday = endtimeofday;
		this._timespend = timespend;
	}
	
	public App(String appname, String timestamp, String date, String endtimeofday) {
		this._appname = appname;
		this._timestamp = timestamp;
		this._date = date;
		this._endtimeofday = endtimeofday;
	}
	
	public App(String timestamp, String starttimeofday) {
		this._timestamp = timestamp;
		this._starttimeofday = starttimeofday;
	}
	
	
	public String getTimeSpend() {
		return this._timespend;
	}
	
	public void setTimeSpend(String timespend) {
		this._timespend = timespend;
	}
	
	public String getStartTimeofday() {
		return this._starttimeofday;
	}
	
	public void setStartTimeofday(String starttimeofday) {
		this._starttimeofday = starttimeofday;
	}
	
	public String getEndTimeofday() {
		return this._endtimeofday;
	}
	
	public void setEndTimeofday(String endtimeofday) {
		this._endtimeofday = endtimeofday;
	}
	
	public String getDate() {
		return this._date;
	}
	
	public void setDate(String date) {
		this._date = date;
	}
	
	public int getId() {
		return this._id;
	}
	
	public void setId(int id) {
		this._id = id;
	}
	
	public String getName() {
		return this._appname;
	}
	
	public void setName(String appname) {
		this._appname = appname;
	}
	
	public String getTimeStamp() {
		return this._timestamp;
	}
	
	public void setTimeStamp(String timestamp) {
		this._timestamp = timestamp;
	}
}

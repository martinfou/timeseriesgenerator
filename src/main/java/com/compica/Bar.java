package com.compica;

public class Bar {
	private String date;
	private String time;
	private double open;
	private double high;
	private double low;
	private double close;
	private int volume;
	
	
	
	public Bar(String date, String time, double open, double high, double low, double close) {
		this.date = date;
		this.time = time;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	@Override
	public String toString() {
		return this.date + "," + this.time + ","+ open + "," + high + "," + low + "," + close + "," +  volume + ",";
	}
	
	
	
}

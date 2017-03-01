package com.compica;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.batch.item.ItemProcessor;


public class TicItemProcessor implements ItemProcessor<Tic, Bar> {

	private double open;
	private double high;
	private double low;
	private double close;
	private boolean isNewBar=true;
	
	private DateTime nextBarTime = null;

	@Override
	public Bar process(Tic tic) throws Exception {

		// parse the string
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy.MM.dd HH:mm");
		DateTime currentBarTime = formatter.parseDateTime(tic.getDate()+" "+tic.getTime());
		if(currentBarTime.equals(nextBarTime)){
			isNewBar=true;
			nextBarTime = currentBarTime.plusMinutes(5);
			return new Bar(tic.getDate(),tic.getTime(),open, high, low, close);	
		}
		if(nextBarTime==null){
			nextBarTime = currentBarTime.plusMinutes(5);
		}
		if(isNewBar){
			open = tic.getOpen();
			high = tic.getHigh();
			low = tic.getLow();
			close = tic.getClose();
			isNewBar=false;
		}
		if(tic.getHigh()>this.high){
			this.high=tic.getHigh();
		}
		if(tic.getLow()<this.low){
			this.low=tic.getLow();
		}
		this.close=tic.getClose();

		return null;
		
	}

}

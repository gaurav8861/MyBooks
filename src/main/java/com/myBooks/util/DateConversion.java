package com.myBooks.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConversion {
	
	public static String DateToString(Date date){
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return df2.format(date);
	}
	public static long DateDifferent(String currentDate,String date){
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		Date d1 = null;
		Date d2 = null;
		long diffTime = 0l;
		try {
			d1 = format.parse(currentDate);
			d2 = format.parse(date);

			//in milliseconds
			long diff = d1.getTime() - d2.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");
			if(diffDays == 0l){
				if(diffHours == 0l){
					if(diffMinutes<15l){
						diffTime = diffMinutes;
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diffTime;
		
	}

}

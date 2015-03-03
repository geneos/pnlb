package org.eevolution.tools;

import java.sql.Timestamp;
import java.util.*;

/**
* @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
* @version 1.0, October 14th 2005
*/
public class DateTimeUtil {

 	public static long getTimeDifference(Timestamp time1 , Timestamp time2) {

		GregorianCalendar gc1 = new GregorianCalendar();
		gc1.setTimeInMillis(time1.getTime());
		GregorianCalendar gc2 = new GregorianCalendar();
		gc2.setTimeInMillis(time2.getTime());

		long l1 = gc1.getTime().getTime();
                long l2 = gc2.getTime().getTime();

                return l2 - l1;
    }

 	
	public static Timestamp[] getDayBorders(Timestamp dateTime, Timestamp timeSlotStart, Timestamp timeSlotFinish) {
	
		return new Timestamp[] {
				
				getDayBorder(dateTime, timeSlotStart, false),
				getDayBorder(dateTime, timeSlotFinish, true),
		};
	}
	
	public static Timestamp getDayBorder(Timestamp dateTime, Timestamp timeSlot, boolean end) {
		
		 GregorianCalendar gc = new GregorianCalendar();
		 gc.setTimeInMillis(dateTime.getTime());
		 dateTime.setNanos(0);
		 
		 if(timeSlot != null) {
			 
			 timeSlot.setNanos(0);

			 GregorianCalendar gcTS = new GregorianCalendar();
			 gcTS.setTimeInMillis(timeSlot.getTime());
			 
			 gc.set(Calendar.HOUR_OF_DAY, gcTS.get(Calendar.HOUR_OF_DAY));
			 gc.set(Calendar.MINUTE, gcTS.get(Calendar.MINUTE));
			 gc.set(Calendar.SECOND, gcTS.get(Calendar.SECOND));
			 gc.set(Calendar.MILLISECOND, gcTS.get(Calendar.MILLISECOND));
		 } 
		 else if(end) {
			 
			 gc.set(Calendar.HOUR_OF_DAY, 23);
			 gc.set(Calendar.MINUTE, 59);
			 gc.set(Calendar.SECOND, 59);
			 gc.set(Calendar.MILLISECOND, 999);
		 }
		 else {
			 
			 gc.set(Calendar.MILLISECOND, 0);
	 		 gc.set(Calendar.SECOND, 0);
	 		 gc.set(Calendar.MINUTE, 0);
	 		 gc.set(Calendar.HOUR_OF_DAY, 0);
		 }
		 return new Timestamp(gc.getTimeInMillis());
	}
	
	public static Timestamp[] getDayBorders(Timestamp dateTime) {
	
		return getDayBorders(dateTime, null, null);
	}
	
	/*public static Timestamp incrementDay(Timestamp dateTime) {
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(dateTime.getTime());
		gc.add(Calendar.DAY_OF_MONTH, 1);
	
		return new Timestamp(gc.getTimeInMillis());
	}

	public static Timestamp decrementDay(Timestamp dateTime) {
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(dateTime.getTime());
		gc.add(Calendar.DAY_OF_MONTH, -1);
	
		return new Timestamp(gc.getTimeInMillis());
	}*/
	
	public static String parserFecha(Timestamp tFecha){

	 	String fecha = tFecha.toString().substring(0, 10);
		String ano = fecha.substring(0, fecha.indexOf('-'));
	 	String mes = fecha.substring(fecha.indexOf('-')+1, fecha.lastIndexOf('-'));
	 	String dia = fecha.substring(fecha.lastIndexOf('-')+1);     	
	 	
	 	return dia + "/" + mes + "/" + ano;
	}
}

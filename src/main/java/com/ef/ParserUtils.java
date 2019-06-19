package com.ef;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserUtils {

  private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  
  private ParserUtils() {
    throw new IllegalStateException("Utility class");
  }
  
  public static Date stringAsDate(String sDate) throws ParseException  {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);  
     try {
      return  formatter.parse(sDate);
     } catch (ParseException e) {
       throw new ParseException(ErrorMessage.DATE_FORMAT_ERROR.getMessage().concat(DATE_FORMAT), 
           ErrorMessage.DATE_FORMAT_ERROR.getErrorCode());
   }
  }
  
  public static Date stringAsDate(String sDate, String customFormat) throws ParseException  {
    SimpleDateFormat formatter = new SimpleDateFormat(customFormat);  
    try {
      return  formatter.parse(sDate);
    } catch (ParseException e) {
      throw new ParseException(ErrorMessage.DATE_FORMAT_ERROR.getMessage().concat(customFormat), 
          ErrorMessage.DATE_FORMAT_ERROR.getErrorCode());
    }
  }
  
  public static String dateToString(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT); 
    return formatter.format(date);
  }
  
  public static String dateToString(Date date, String customFormat) {
	    SimpleDateFormat formatter = new SimpleDateFormat(customFormat); 
	    return formatter.format(date);
	  }
  
}

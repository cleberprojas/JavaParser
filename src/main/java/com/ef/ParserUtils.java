package com.ef;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserUtils {

  private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  
  public static Date getStringAsDate(String sDate) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);  
     return  formatter.parse(sDate);
  }
  
  public static Date getStringAsDate(String sDate, String customFormat) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat(customFormat);  
     return  formatter.parse(sDate);
  }
  
}

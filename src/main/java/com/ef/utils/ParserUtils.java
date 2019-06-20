package com.ef.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

import com.ef.model.ApplicationArguments;

public class ParserUtils {

  final static Logger logger = Logger.getLogger(ParserUtils.class);
	  
  private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String PARAMNS_FILE_PATH = "--accesslog";
  public static final String PARAMNS_START_DATE = "--startDate";
  public static final String PARAMNS_DURATION = "--duration";
  public static final String PARAMNS_THRESHOLD = "--threshold";
  
  public static final String DURATION_VALUE_HOURLY = "hourly";
  public static final String DURATION_VALUE_DAILY = "daily";
  //public static final String[] PARAMNS_NAMES = {PARAMNS_FILE_PATH,PARAMNS_START_DATE,PARAMNS_DURATION,PARAMNS_THRESHOLD};
  
  private ParserUtils() {
    throw new IllegalStateException("Utility class");
  }
  
  public static Date stringAsDate(String sDate) throws ParseException  {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);  
     try {
      return  formatter.parse(sDate);
     }catch (ParseException e) {
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
  
  /**
   *  Parse String to Int
   * */
  public static int getValueAsInt(String value) {
    int intValue;
    try {
       intValue = Integer.parseInt(value);
    }catch (NumberFormatException e) {
      throw new IllegalArgumentException(ErrorMessage.INVALID_THRESHOLD_VALUE.getMessage());
    }
    return intValue;
  }
  
   /**
	 * Validate params informed as arguments to the application 
	 * if some is missing, then throw IllegalArgumentException
	 * */
	public static ApplicationArguments validateArgs(String[] args) {
		ApplicationArguments arguments = new ApplicationArguments();
		if(args.length < 3) { throw new IllegalArgumentException(ErrorMessage.PARAMNS_MISSING.getMessage());}
		for(int i = 0; i <= args.length-1 ;i++) {
			String[] values = args[i].split("\\=");
			if( values != null && values.length == 2 ) {
				switch (values[0]) {
				case ParserUtils.PARAMNS_FILE_PATH:
					arguments.setPathToFile(values[1]);
					break;
				case ParserUtils.PARAMNS_START_DATE:
					arguments.setStartDate(values[1]);
					break;
				case ParserUtils.PARAMNS_DURATION:
					arguments.setDuration(values[1]);
					break;
				case ParserUtils.PARAMNS_THRESHOLD:
					arguments.setThreshold( ParserUtils.getValueAsInt(values[1]));
					break;
				default:
					break;
				}
			} else {
				throw new IllegalArgumentException(ErrorMessage.values()[i].getMessage());
			}
		}
	 return arguments;
	}
  
}

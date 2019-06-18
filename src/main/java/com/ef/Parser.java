package com.ef;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.protobuf.Timestamp;

/**
 *
 */
public class Parser{

  private static String pathToFile; 
  private static String startDate; 
  private static String duration;  
  private static String threshold; 

  public static void main( String[] args ){
    long begin = System.currentTimeMillis();
    try {
      setParamns(args);
      LogFileReader logFileReader = new LogFileReader(pathToFile);
      Date start = ParserUtils.stringAsDate(startDate,"yyyy-MM-dd.HH:mm:ss");
      Calendar end = prepareDateParamns(duration, start);
      List<LogData> logs = logFileReader.toDataObject();
      
      saveLogs(logs);

      Map<String, Long> rst = logs.stream()
                              .filter(log->  isInDateRange(log, start, end) )
                              .collect(Collectors.groupingBy(LogData::getIpNumber,Collectors.counting()));
      rst.entrySet()
         .stream()
         .filter(entry-> entry.getValue() >= Integer.parseInt(threshold))
         .forEach(entry-> System.out.println(entry.getKey()));
    } catch (IOException | IllegalArgumentException | ParseException e ) {
      System.err.println(e.getMessage());
    } 
    
    long end = System.currentTimeMillis();
    System.out.println("TEMPO "+ (end - begin)/1000);
  }
  
  private static int saveLogs(List<LogData> logs) {
    LogDataRepository repo = new LogDataRepository();
    return repo.saveAll(logs);
  }

  private static void setParamns(String[] args) {
    validateArgs(args);
    pathToFile = args[0].split("\\=")[1] ; // --accesslog=/path/to/file 
    startDate = args[1].split("\\=")[1] ; // --startDate=2017-01-01.13:00:00
    duration = args[2].split("\\=")[1] ; // --duration=hourly 
    threshold = args[3].split("\\=")[1]; 
  }

  private static void validateArgs(String[] args) {
    if(args.length != 4) { throw new IllegalArgumentException(ErrorMessage.PARAMNS_MISSING.getMessage());}
    for(int i = 0; i <= args.length-1 ;i++) {
      String[] values = args[i].split("\\=");
      if( values != null && values.length < 2 ) {
        throw new IllegalArgumentException(ErrorMessage.values()[i].getMessage());
      } 
    }
  }

  private static Calendar prepareDateParamns(String duration, Date start) {
    Calendar end = Calendar.getInstance();
    end.setTime(start);
    if(duration.equalsIgnoreCase("hourly")) {
      int hour = end.get(Calendar.HOUR); 
      end.set(Calendar.HOUR, hour+1);
    }else {
      int day = end.get(Calendar.DAY_OF_MONTH); 
      end.set(Calendar.DAY_OF_MONTH, day+1);
    }
    return end;
  }

  private static boolean isInDateRange(LogData log, Date start, Calendar end) {
    return log.getLogDate().after(start) 
        && log.getLogDate().before(end.getTime());
  }

}

package com.ef;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class Parser{

  public static void main( String[] args ) throws ParseException{
    //TESTAR -> PARAMETROS EM BRANCO ou não informados
    String pathToFile = args[0].split("\\=")[1] ; // --accesslog=/path/to/file 
    String startDate = args[1].split("\\=")[1] ; // --startDate=2017-01-01.13:00:00
    String duration = args[2].split("\\=")[1] ; // --duration=hourly 
    String threshold = args[3].split("\\=")[1] ; // --threshold=100 

    LogFileReader logFileReader = new LogFileReader(pathToFile);
    try {
      Date start = ParserUtils.getStringAsDate(startDate,"yyyy-MM-dd.HH:mm:ss");
      Calendar end = prepareDateParamns(duration, start);
      List<LogData> list = logFileReader.toDataObject();
      Map<String, Long> rst = list.stream()
                              .filter(log->  isInDateRange(log, start, end) )
                              .collect(Collectors.groupingBy(LogData::getIpNumber,Collectors.counting()));
      rst.entrySet()
          .stream()
          .filter(entry-> entry.getValue() >= Integer.parseInt(threshold))
          .forEach(entry-> System.out.println(entry.getKey()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
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

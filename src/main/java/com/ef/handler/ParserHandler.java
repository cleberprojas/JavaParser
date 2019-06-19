package com.ef.handler;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.ef.dto.LogDataDTO;
import com.ef.model.LogData;
import com.ef.repository.LogDataRepository;
import com.ef.utils.ErrorMessage;
import com.ef.utils.LogFileReader;
import com.ef.utils.ParserUtils;

public class ParserHandler {

  private static String pathToFile; 
  private static String startDate; 
  private static String duration;  
  private static int threshold; 

  public void handleLogFile(String[] args) {
    List<LogData> logs = new ArrayList<>();
    try {
      if(validateArgs(args) == 0) {
        LogFileReader logFileReader = new LogFileReader(pathToFile);
        logs = logFileReader.toDataObject();
        saveLogs(logs);
      }
      Date start = ParserUtils.stringAsDate(startDate,"yyyy-MM-dd.HH:mm:ss");
      Calendar end = prepareDateParamns(duration, start);
      
      Optional<List<LogDataDTO>> listIp = findLogsByDateAndThreshold(start, end.getTime(), threshold);
      if(listIp.isPresent()) {
         listIp.get()
               .stream()
               .forEach(logIp->{
                 System.out.print("IP : ".concat(logIp.getIpNumber()));
                 System.out.println(" - Times Requested : "+logIp.getTimesRequested());
                 addIpToBlockedList(logIp.getIpNumber(), getBlockedMessage(logIp, start, end.getTime())); 
               });
      }else {
        System.out.println("No IP found with the informed paramns.");
      }
      
    } catch (IOException | IllegalArgumentException | ParseException e ) {
      System.err.println(e.getMessage());
    } 
  }

  private static String getBlockedMessage(LogDataDTO logIp, Date start, Date end) {
    StringBuilder sb = new StringBuilder(" IP Blocked due too many request.")
        .append(" Request count:")
        .append(logIp.getTimesRequested())
        .append( " Beginning at: " )
        .append( ParserUtils.dateToString(start))
        .append( " Ending at : ")
        .append(ParserUtils.dateToString(end));
    return sb.toString();
  }
  
  private static int addIpToBlockedList(String ipBlocked, String blockedMessage) {
    LogDataRepository repo = new LogDataRepository();
    return repo.saveBlockedIp(ipBlocked, blockedMessage);
  }

  private static int saveLogs(List<LogData> logs) {
    LogDataRepository repo = new LogDataRepository();
    return repo.saveAll(logs);
  }

  private static Optional<List<LogDataDTO>> findLogsByDateAndThreshold(Date start, Date end, int threshold  ) {
    LogDataRepository repo = new LogDataRepository();
    return repo.findLogsByDateAndThreshold(start, end, threshold);
  }
  
  private static int validateArgs(String[] args) {
    int route = 1;
    if(args.length < 3) { throw new IllegalArgumentException(ErrorMessage.PARAMNS_MISSING.getMessage());}
    for(int i = 0; i <= args.length-1 ;i++) {
      String[] values = args[i].split("\\=");
      if( values != null && values.length == 2 ) {
           switch (values[0]) {
            case ParserUtils.PARAMNS_FILE_PATH:
              pathToFile = values[1];
              route = 0;
              break;
            case ParserUtils.PARAMNS_START_DATE:
              startDate = values[1];
              break;
            case ParserUtils.PARAMNS_DURATION:
              duration = values[1];
              break;
            case ParserUtils.PARAMNS_THRESHOLD:
              threshold = getValueAsInt(values[1]);
              break;
            default:
              break;
          }
      } else {
        throw new IllegalArgumentException(ErrorMessage.values()[i].getMessage());
      }
    }
    return route;
  }

  private static int getValueAsInt(String value) {
    int intValue;
    try {
       intValue = Integer.parseInt(value);
    }catch (NumberFormatException e) {
      throw new IllegalArgumentException(ErrorMessage.INVALID_THRESHOLD_VALUE.getMessage());
    }
    return intValue;
  }
  
  private static Calendar prepareDateParamns(String duration, Date start) {
    Calendar end = Calendar.getInstance();
    end.setTime(start);
    if(duration.equalsIgnoreCase(ParserUtils.DURATION_VALUE_HOURLY)) {
      int hour = end.get(Calendar.HOUR); 
      end.set(Calendar.HOUR, hour+1);
    }else if(duration.equalsIgnoreCase(ParserUtils.DURATION_VALUE_DAILY)) {
      int day = end.get(Calendar.DAY_OF_MONTH); 
      end.set(Calendar.DAY_OF_MONTH, day+1);
    }else {
      throw new IllegalArgumentException(ErrorMessage.INVALID_DURATION_VALUE.getMessage());
    }
    return end;
  }
}

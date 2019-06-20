package com.ef.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ef.model.LogData;

public class LogFileReader {

  final static Logger logger = Logger.getLogger(LogFileReader.class);
  
  private String logFilePath;

  public LogFileReader(String logFilePath) {
    this.logFilePath = logFilePath;
  }

  public List<LogData> toDataObject() throws  IOException {
    List<LogData> lstData = new ArrayList<>(); 
    try( BufferedReader br = Files.newBufferedReader(Paths.get(logFilePath))){
      String line;
      int lineNum =1;
      while ((line = br.readLine()) != null) {
         String[] r = line.split("\\|");
         if(r.length==5) {
        	 lstData.add(new LogData(r));
         }else {
        	 logger.info("Malformed input at line "+lineNum+", missing info or '|' not present.");
         }
         lineNum++;
      }
    }catch (IOException e) {
      throw new IOException(ErrorMessage.INVALID_PATH_VALUE.getMessage() + e.getMessage());
    }
    return lstData;
  }
}

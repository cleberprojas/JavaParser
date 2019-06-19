package com.ef.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.ef.model.LogData;

public class LogFileReader {

  private String logFilePath;

  public LogFileReader(String logFilePath) {
    this.logFilePath = logFilePath;
  }

  public List<LogData> toDataObject() throws  IOException {
    List<LogData> lstData = new ArrayList<>(); 
    try( BufferedReader br = Files.newBufferedReader(Paths.get(logFilePath))){
      String line;
      while ((line = br.readLine()) != null) {
         String[] r = line.split("\\|");
         lstData.add(new LogData(r));
      }
    }catch (IOException e) {
      throw new IOException(ErrorMessage.INVALID_PATH_VALUE.getMessage() + e.getMessage());
    }
    return lstData;
  }
}

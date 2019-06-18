package com.ef;

import java.util.List;

public interface Repository {
  
  public int register(LogData log);
  public int saveAll(List<LogData> logs);
  public List<LogData> findAll();

}

package com.ef.repository;

import java.util.List;
import com.ef.model.LogData;

public interface Repository {
  
  public int register(LogData log);
  public int saveAll(List<LogData> logs);

}

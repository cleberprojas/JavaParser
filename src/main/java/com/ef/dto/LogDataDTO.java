package com.ef.dto;

public class LogDataDTO {

  private String ipNumber;
  private Long timesRequested;
  
  public LogDataDTO(String ipNumber, Long timesRequested) {
    super();
    this.ipNumber = ipNumber;
    this.timesRequested = timesRequested;
  }

  public String getIpNumber() {
    return ipNumber;
  }
  public void setIpNumber(String ipNumber) {
    this.ipNumber = ipNumber;
  }
  public Long getTimesRequested() {
    return timesRequested;
  }
  public void setTimesRequested(Long timesRequested) {
    this.timesRequested = timesRequested;
  }
  
  
}

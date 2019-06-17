package com.ef;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogData {
  
  private Date logDate;
  private String ipNumber;
  private String requestMethod;
  private String statusCode;
  private String sourceDescription;
  

  
  public LogData() {  }
  
  public LogData(Date logDate, String ipNumber, String requestMethod, String statusCode,
      String sourceDescription) {
    super();
    this.logDate = logDate;
    this.ipNumber = ipNumber;
    this.requestMethod = requestMethod;
    this.statusCode = statusCode;
    this.sourceDescription = sourceDescription;
  }

  public LogData(String[] arrayData) {
    super();
    setDate(arrayData[0]);
    this.ipNumber = arrayData[1];
    this.requestMethod = arrayData[2];
    this.statusCode = arrayData[3];
    this.sourceDescription = arrayData[4];
  }
  
   private void setDate(String sDate) {
     try {
       this.logDate = ParserUtils.getStringAsDate(sDate);
     } catch (ParseException e) {
       e.printStackTrace();
     }
   }
   
  public Date getLogDate() {
    return logDate;
  }
  public void setLogDate(Date logDate) {
    this.logDate = logDate;
  }
  public String getIpNumber() {
    return ipNumber;
  }
  public void setIpNumber(String ipNumber) {
    this.ipNumber = ipNumber;
  }
  public String getRequestMethod() {
    return requestMethod;
  }
  public void setRequestMethod(String requestMethod) {
    this.requestMethod = requestMethod;
  }
  public String getStatusCode() {
    return statusCode;
  }
  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }
  public String getSourceDescription() {
    return sourceDescription;
  }
  public void setSourceDescription(String sourceDescription) {
    this.sourceDescription = sourceDescription;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((ipNumber == null) ? 0 : ipNumber.hashCode());
    result = prime * result + ((logDate == null) ? 0 : logDate.hashCode());
    result = prime * result + ((requestMethod == null) ? 0 : requestMethod.hashCode());
    result = prime * result + ((sourceDescription == null) ? 0 : sourceDescription.hashCode());
    result = prime * result + ((statusCode == null) ? 0 : statusCode.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    LogData other = (LogData) obj;
    if (ipNumber == null) {
      if (other.ipNumber != null)
        return false;
    } else if (!ipNumber.equals(other.ipNumber))
      return false;
    if (logDate == null) {
      if (other.logDate != null)
        return false;
    } else if (!logDate.equals(other.logDate))
      return false;
    if (requestMethod == null) {
      if (other.requestMethod != null)
        return false;
    } else if (!requestMethod.equals(other.requestMethod))
      return false;
    if (sourceDescription == null) {
      if (other.sourceDescription != null)
        return false;
    } else if (!sourceDescription.equals(other.sourceDescription))
      return false;
    if (statusCode == null) {
      if (other.statusCode != null)
        return false;
    } else if (!statusCode.equals(other.statusCode))
      return false;
    return true;
  }  

}

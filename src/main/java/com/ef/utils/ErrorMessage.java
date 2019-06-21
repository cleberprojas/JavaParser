package com.ef.utils;

public enum ErrorMessage {
  
  PARAMNS_01_MISSING(1,"Paramns --accesslog should not be null or empty"),
  PARAMNS_02_MISSING(2,"Paramns --startDate= should not be null or empty"),
  PARAMNS_03_MISSING(3,"Paramns --duration= should not be null or empty"),
  PARAMNS_04_MISSING(4,"Paramns --threshold= should not be null or empty"),
  DATE_FORMAT_ERROR(5,"invalid Date Format at param --startDate, use as follows "),
  PARAMNS_MISSING(6,"Some paramns are missing, Please enter as follow  --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100 "),
  INVALID_DURATION_VALUE(7,"Paramns --duration= should  be 'hourly' or 'daily', please verify the value informed "),
  INVALID_THRESHOLD_VALUE(8,"Paramns --threshold= should  be Integer value, not Zero or Less, please verify the value informed "),
  INVALID_PATH_VALUE(9,"Log file informed in '--accesslog=' was not found, verify if the file exists or the path is correct. ");
  
  private final  int errorCode;
  private final String message;
  
  private ErrorMessage(int errorCode, String message) {
      this.errorCode = errorCode;
      this.message =  message;
  }
  
  public String getMessage() {
    return message;
  }

  public int getErrorCode() {
    return errorCode;
  }
  
}

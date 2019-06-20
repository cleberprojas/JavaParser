package com.ef.model;

public class ApplicationArguments {
	
	private String pathToFile; 
	private String startDate; 
	private String duration;  
	private int threshold;
	
	public String getPathToFile() {
		return pathToFile;
	}
	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public int getThreshold() {
		return threshold;
	}
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	
	
}

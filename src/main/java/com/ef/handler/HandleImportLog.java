package com.ef.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ef.model.ApplicationArguments;
import com.ef.model.LogData;
import com.ef.repository.LogDataRepository;
import com.ef.utils.LogFileReader;
import com.mysql.cj.util.StringUtils;

public class HandleImportLog implements Handler {
	
	final static Logger logger = Logger.getLogger(HandleRequestOccurrences.class);
	
	@Override
	public void handleLogFile(ApplicationArguments arguments) {
		List<LogData> logs = new ArrayList<>();
		try {
			if(!StringUtils.isNullOrEmpty(arguments.getPathToFile() )){
				LogFileReader logFileReader = new LogFileReader(arguments.getPathToFile());
				logs = logFileReader.toDataObject();
				saveLogs(logs);
			}
		 } catch (IOException e) {
			 logger.error(e.getMessage());
		 }
	}

	/**
	 * Save All logData from access log file
	 * */
	private static int saveLogs(List<LogData> logs) {
		LogDataRepository repo = new LogDataRepository();
		return repo.saveAll(logs);
	}

}
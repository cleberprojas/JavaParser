package com.ef;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.ef.handler.HandleImportLog;
import com.ef.handler.HandleRequestOccurrences;
import com.ef.handler.Handler;
import com.ef.model.ApplicationArguments;
import com.ef.utils.ParserUtils;

/**
	Parse access log application file to MySql database
	@param String[] as "accesslog" "startDate", "duration" and "threshold" 
	@return void
	@autor Cleber Rojas
	@throw
*/ 
public class Parser{

	final static Logger logger = Logger.getLogger(Parser.class);

	public static void main( String[] args ){
		BasicConfigurator.configure();
		try {
			ApplicationArguments arguments = ParserUtils.validateArgs(args);
			handleLogFile(arguments);
			handleOccurrences(arguments);
		}catch (Exception e){
			logger.error(e.getMessage());
		}

	}

	private static void handleLogFile(ApplicationArguments arguments){
		Handler parse =  new HandleImportLog();
		parse.handleLogFile(arguments);
	}

	private static void handleOccurrences(ApplicationArguments arguments){
		Handler parse = new HandleRequestOccurrences();
		parse.handleLogFile(arguments);
	}


}

package com.ef.handler;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;
import com.ef.dto.LogDataDTO;
import com.ef.model.ApplicationArguments;
import com.ef.model.LogData;
import com.ef.repository.LogDataRepository;
import com.ef.utils.ErrorMessage;
import com.ef.utils.ParserUtils;

public class HandleRequestOccurrences implements Handler{

	final static Logger logger = Logger.getLogger(HandleRequestOccurrences.class);

	/**
	 * Read from DB Ip's that made more than an certain numbers of Request in given time interval.
	 * */
	@Override
	public void handleLogFile(ApplicationArguments arguments) {
		try {
			Date start = ParserUtils.stringAsDate(arguments.getStartDate(),"yyyy-MM-dd.HH:mm:ss");
			Calendar end = prepareDateParamns(arguments.getDuration(), start);
			Optional<List<LogDataDTO>> listIp = findLogsByDateAndThreshold(start, end.getTime(), arguments.getThreshold());
			if(listIp.isPresent()) {
				listIp.get()
				.stream()
				.forEach(logIp->{
					System.out.print("IP : ".concat(logIp.getIpNumber()));
					System.out.println(" - Times Requested : "+logIp.getTimesRequested());
					addIpToBlockedList(logIp.getIpNumber(), getBlockedMessage(logIp, start, end.getTime())); 
				});
				
				/**
				 * (2) Write MySQL query to find requests made by a given IP.
				 * NOT SURE HOW TO PROCEED WITH THIS, SO I KEEP IT COMMENTED 
				 *   PLEASE UNCOMMENT IF YOU WAN TO RUN, I'LL SEND THI QUERY IN SQL FILE
				Optional<List<LogData>> requests = findRequestMadeByGivenIp("192.168.129.191");
				if(requests.isPresent()) {
					requests.get().stream().forEach(logIp-> System.out.println(logIp.getSourceDescription()));
				}else{
					logger.info("No Log Entries found with the informed paramns.");
				}*/
				
			}else {
				logger.info("No IP found with the informed paramns.");
			}

		} catch ( IllegalArgumentException | ParseException e ) {
			logger.error(e.getMessage());
		} 
	}

	/**
	 * Add time to a Date, an Hour or a Day
	 * */
	private static Calendar prepareDateParamns(String duration, Date start) {
		Calendar end = Calendar.getInstance();
		end.setTime(start);
		if(duration.equalsIgnoreCase(ParserUtils.DURATION_VALUE_HOURLY)) {
			int hour = end.get(Calendar.HOUR); 
			end.set(Calendar.HOUR, hour+1);
		}else if(duration.equalsIgnoreCase(ParserUtils.DURATION_VALUE_DAILY)) {
			int day = end.get(Calendar.DAY_OF_MONTH); 
			end.set(Calendar.DAY_OF_MONTH, day+1);
		}else {
			throw new IllegalArgumentException(ErrorMessage.INVALID_DURATION_VALUE.getMessage());
		}
		return end;
	}

	/**
	 * build comments about why an IP it`s being blocked
	 * */
	private static String getBlockedMessage(LogDataDTO logIp, Date start, Date end) {
		StringBuilder sb = new StringBuilder(" IP Blocked due too many request.")
				.append(" Request count:")
				.append(logIp.getTimesRequested())
				.append( " Beginning at: " )
				.append( ParserUtils.dateToString(start))
				.append( " Ending at : ")
				.append(ParserUtils.dateToString(end));
		return sb.toString();
	}

	/**
	 * Save in blocked table an IP that its caught doing too many requests to the server
	 * */
	private static int addIpToBlockedList(String ipBlocked, String blockedMessage) {
		LogDataRepository repo = new LogDataRepository();
		return repo.saveBlockedIp(ipBlocked, blockedMessage);
	}

	/**
	 * Find IP`s that area doing too many request to the server
	 * It`s loads from the MySql tables
	 * */
	private static Optional<List<LogDataDTO>> findLogsByDateAndThreshold(Date start, Date end, int threshold  ) {
		LogDataRepository repo = new LogDataRepository();
		return repo.findLogsByDateAndThreshold(start, end, threshold);
	}
	
	/**
	 * Find Requests made by a given IP
	 * 
	 * */
	private static Optional<List<LogData>> findRequestMadeByGivenIp(String ipNumber  ) {
		LogDataRepository repo = new LogDataRepository();
		return repo.findRequestByIp(ipNumber);
	}
}

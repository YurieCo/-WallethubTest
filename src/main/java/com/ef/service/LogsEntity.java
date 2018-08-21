package com.ef.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ef.dao.BatchSave;
import com.ef.dao.CustomerRepository;
import com.ef.entity.ApplicationRunnerBean;
import com.ef.pojo.Logs;

@Service
public class LogsEntity {
	private static final Logger logger = LoggerFactory.getLogger(LogsEntity.class);
	@Autowired
	private ApplicationRunnerBean cmdLineArgs;
	private List<Logs> savedEntities;
	@Autowired
	private BatchSave batchSave;
	private int batchSize = 500;
	@Autowired
	private CustomerRepository repo;

	private List<Logs> loadLogFile(String filename) {
		String cwd = new File("").getAbsolutePath();

		if (filename.isEmpty()) {
			filename = "access.log";
		}
		List<Logs> logs = null;
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			logs = new ArrayList<>();
			String line = br.readLine();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			while (line != null) {
				String[] data = line.split("\\|");

				Logs log = new Logs.LogsBuilder().setStartDate(LocalDateTime.parse(data[0], dtf)).setIpaddress(data[1])
						.setRequestType(data[2]).setStatusCode(Integer.parseInt(data[3])).setBrowser(data[4])
						.createItem();
				logger.debug("now Adding files " + log);
				logs.add(log);
				line = br.readLine();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("searching for " + filename + " inside " + cwd);
			return null;
		} catch (DateTimeParseException e) {
			logger.error("error when parsing " + e);
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return logs;
	}

	private Collection<Logs> bulkSave(List<Logs> samples) {
		int size = samples.size();
		savedEntities = new ArrayList<>(size);
		try {
			for (int i = 0; i < size; i += batchSize) {
				int toIndex = i + (((i + batchSize) < size) ? batchSize : size - i);
				savedEntities.addAll(batchSave.process(samples.subList(i, toIndex)));
			}
			logger.info("success inserted " + batchSize + " rows");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return savedEntities;
	}

	public void proccessArgs() {
		String filename = cmdLineArgs.getFilename();
		if (!filename.isEmpty()) {
			List<Logs> logsData = loadLogFile(filename);
			bulkSave(logsData);
		}

		LocalDateTime startDate = cmdLineArgs.getStartDate();
		String duration = cmdLineArgs.getDuration();
		String ipAddress = cmdLineArgs.getIpAddress();
		int threshold = cmdLineArgs.getThreshold();

		if (duration.equalsIgnoreCase("daily")) {
			duration = "day";
		} else {
			duration = "hour";
		}

		if (ipAddress == null) {
			extractRequestByIp(startDate, threshold, duration);
		}else {
			extractIpOver(startDate, duration, threshold, ipAddress);
		}

	}

	private void extractIpOver(LocalDateTime startDate, String duration, int threshold, String ipAddress) {
		logger.info("got request for ips which has overlimits ");
		String[] strings = repo.findrequestsByIp(ipAddress, startDate, threshold, duration);
		System.out.println("".join(" ", strings));
	}

	private void extractRequestByIp(LocalDateTime startDate, int threashold, String duraction) {
		List<String[]> queryResult = repo.findoverlimitsIP(startDate, threashold, duraction);
		for (String[] strings : queryResult) {
			System.out.println("".join(" ", strings));
		}
	}

}

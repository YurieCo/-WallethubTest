package com.ef.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunnerBean implements ApplicationRunner {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationRunnerBean.class);
	private LocalDateTime startDate;
	private String ipAddress;
	private String duration;
	private int threshold;
	private String filename;

	public String getFilename() {
		return filename;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public String getDuration() {
		return duration;
	}

	public int getThreshold() {
		return threshold;
	}

	@PostConstruct
	private void init() {
		threshold = -1;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		List<String> startDateArgs = args.getOptionValues("startDate");
		List<String> durationArgs = args.getOptionValues("duration");
		List<String> thresholdArgs = args.getOptionValues("threshold");
		List<String> fileArg = args.getOptionValues("file");
		List<String> ipAddressArgs = args.getOptionValues("ipAddress");

		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
			if (startDateArgs.size() > 0) {
				startDate = LocalDateTime.parse(startDateArgs.get(0), dtf);
			}

			if (durationArgs != null && durationArgs.size() > 0) {
				String temp = durationArgs.get(0);
				if (temp.equalsIgnoreCase("hourly") || temp.equalsIgnoreCase("daily")) {
					duration = temp;
				}
			}

			if (thresholdArgs != null && thresholdArgs.size() > 0) {
				threshold = Integer.parseInt(thresholdArgs.get(0));
			}
			if (fileArg != null && fileArg.size() > 0) {
				filename = fileArg.get(0);
			}

			if (ipAddressArgs != null && ipAddressArgs.size() > 0) {
				ipAddress = ipAddressArgs.get(0);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("wrong argument for thresholdArgs, it should be integer");
		} catch (DateTimeParseException e) {
			e.printStackTrace();
			System.out.println("wrong argument for startDate");
		}

		String strArgs = Arrays.stream(args.getSourceArgs()).collect(Collectors.joining("|"));
		logger.info("Application started with arguments:" + strArgs);
	}
}

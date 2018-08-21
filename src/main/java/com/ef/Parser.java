package com.ef;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ef.dao.CustomerRepository;
import com.ef.service.LogsEntity;

@SpringBootApplication(scanBasePackages = { "com.ef" })
public class Parser {

	static ConfigurableApplicationContext context;
	private static final Logger logger = LoggerFactory.getLogger(LogsEntity.class);
	
	
	public static void main(String[] args) {
		context = SpringApplication.run(Parser.class, args);
		LogsEntity logs = context.getBean(LogsEntity.class);
		logs.proccessArgs(); 	 

	}

}

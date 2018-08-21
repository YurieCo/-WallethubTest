package com.ef.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="Logs")
public class Logs {

	public Long getId() {
		return id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.sss")
	@Column(nullable = false)
	private LocalDateTime date;

	@Column(nullable = false)
	private String ipaddress;

	private String requestType;

	@Column(columnDefinition = "smallint", nullable = false)
	private int statusCode;

	private String browser;

	public Logs(LocalDateTime startDate, String ipaddress, String requestType, int statusCode, String browser) {
		this.date = startDate;
		this.ipaddress = ipaddress;
		this.requestType = requestType;
		this.statusCode = statusCode;
		this.browser = browser;
	}

	@Override
	public String toString() {
		return "Logs [date=" + date + ", ipaddress=" + ipaddress + ", requestType=" + requestType + ", statusCode="
				+ statusCode + ", browser=" + browser + "]";
	}

	public static class LogsBuilder {
		private LocalDateTime nesteddate;
		private String nestedipaddress;
		private String nestedrequestType;
		private int nestedstatusCode;
		private String nestedbrowser;

		public LogsBuilder setStartDate(LocalDateTime startDate) {
			this.nesteddate = startDate;
			return this;
		}

		public LogsBuilder setIpaddress(String ipaddress) {
			this.nestedipaddress = ipaddress;
			return this;
		}

		public LogsBuilder setRequestType(String requestType) {
			this.nestedrequestType = requestType;
			return this;
		}

		public LogsBuilder setStatusCode(int statusCode) {
			this.nestedstatusCode = statusCode;
			return this;
		}

		public LogsBuilder setBrowser(String browser) {
			this.nestedbrowser = browser;
			return this;
		}

		public Logs createItem() {
			return new Logs(nesteddate, nestedipaddress, nestedrequestType, nestedstatusCode, nestedbrowser);
		}
	}

}
package com.ef.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ef.pojo.Logs;

public interface CustomerRepository extends CrudRepository<Logs, Long> {

	@Query(value = "select ipaddress, count(*) as nr from Logs where date between :startDate and :startDate + interval 1 :duraction and ipaddress = :ip having nr > :threshold;", nativeQuery = true)
	String[] findrequestsByIp(String ip, LocalDateTime startDate, int threshold, String duraction);

	@Query(value = "select ipaddress, count(*) as nr from Logs where date between :startDate and :startDate + interval 1 :duraction having nr > :threshold;", nativeQuery = true)
	List<String[]> findoverlimitsIP(LocalDateTime startDate, int threshold, String duraction);

}
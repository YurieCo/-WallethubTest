package com.ef.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ef.pojo.Logs;

@Transactional
@Component
public class BatchSave {
	@PersistenceContext
	private EntityManager em;

	@Transactional
	public List<Logs> process(List<Logs> batch) {

		int size = batch.size();
		List<Logs> savedEntities = new ArrayList<>(size);

		for (Logs t : batch) {
			Logs result;
			if (t.getId() == null) {
				em.persist(t);
				result = t;
			} else {
				result = em.merge(t);
			}
			savedEntities.add(result);
		}
		em.flush();
		em.clear();
		return savedEntities;
	}

}

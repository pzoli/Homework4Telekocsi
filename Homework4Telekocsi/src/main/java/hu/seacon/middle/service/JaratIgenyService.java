package hu.seacon.middle.service;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.primefaces.model.SortOrder;

import hu.exprog.beecomposit.back.model.Addresses;
import hu.exprog.beecomposit.back.model.Company;
import hu.exprog.honeyweb.middle.services.BasicService;
import hu.seacon.back.model.IgenyStatusz;
import hu.seacon.back.model.JaratIdo;
import hu.seacon.back.model.JaratIgeny;
import hu.seacon.back.model.JaratIrany;

@Named
@Stateless
public class JaratIgenyService extends BasicService<JaratIgeny> implements
		Serializable {

	private static final long serialVersionUID = -9139027636913174016L;

	@PersistenceContext(unitName="config")
	private EntityManager em;
	
	@Inject
	private Logger log;

	public JaratIgenyService() {

	}

	@PostConstruct
	public void init() {
	}

	@Override
	protected Query buildCountQuery(Map<String, Object> filters) {
		String result = buildQueryString(filters);
		result = "select count(*) " + result;
		Query q = em.createQuery(result);
		return q;
	}

	@Override
	protected Query buildQuery(String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		String result = buildQueryString(filters);
		Query q = em.createQuery(result);
		return q;
	}

	@Override
	protected String buildQueryString(Map<String, Object> filters) {
		StringBuffer queryBuff = new StringBuffer();
		queryBuff.append("from "+getDomainClass().getSimpleName());
		filters.keySet().stream().forEach(key -> {
			Object o = filters.get(key);
			Map<String, String> desc = getDescriptor(key);
			queryBuff.append(" ");
		});
		return queryBuff.toString();
	}

	private Map<String, String> getDescriptor(String key) {
		return null;
	}

	@Override
	protected void setQueryParams(Query q, Map<String, Object> filters) {
		filters.entrySet().stream().forEach((Entry<String, Object>e) -> {
			q.setParameter(e.getKey(), e.getValue());	
		});
	}

	@Override
	public EntityManager getEm() {
		return em;
	}

	@Override
	public JaratIgeny find(JaratIgeny item) {
		return em.find(JaratIgeny.class, item.getId());
	}

	@Override
	public Logger getLogger() {
		return log;
	}

	@Override
	public Class<JaratIgeny> getDomainClass() {
		return JaratIgeny.class;
	}

}

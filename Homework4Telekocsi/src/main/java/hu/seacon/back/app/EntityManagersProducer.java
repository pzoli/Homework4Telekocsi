package hu.seacon.back.app;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class EntityManagersProducer {

	@Produces
	@PersistenceContext(unitName = "config")
	private EntityManager emConfig;

}

package hu.seacon.back.application;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class StartupService {

	@PostConstruct
	private void init() {
	}

}

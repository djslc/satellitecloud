package uk.me.g4dpz.gae.satellitecloud.server.implementation;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.datanucleus.jpa.EntityManagerFactoryImpl;

public class CoreManager {
	
	private CoreManager() {
		
	}
	
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("transactions-optional");

	public static final EntityManagerFactory getEmf() {
		return emf;
	}

}

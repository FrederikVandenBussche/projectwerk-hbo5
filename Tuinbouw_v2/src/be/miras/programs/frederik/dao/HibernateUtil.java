package be.miras.programs.frederik.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * @author Frederik Vanden Bussche
 * 
 * * Utility class om een Hibernate session aan te maken.
 *
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory;
	
	private static final Logger LOGGER = Logger.getLogger(SessionFactory.class);
	private final String TAG = "HibernateUtil : ";

	/*
	 * Een static methode zonder naam heet een 'Static Initialization Block'
	 * Deze block wordt gerund als de class geload wordt. Dit kan gebruikt
	 * worden om static member variabelen te initialiseren.
	 */
	static {
		try {
			// 1. hibernate configureren
			Configuration configuration = new Configuration();
			configuration.configure();
			// 2. een sessionfactory creêren
			ServiceRegistry sr = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
					.buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(sr);
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			LOGGER.error("Throwable: " + TAG , ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * 
	 * @return hibernate Session
	 */
	public static Session openSession() {
		return sessionFactory.openSession();
	}
	
}

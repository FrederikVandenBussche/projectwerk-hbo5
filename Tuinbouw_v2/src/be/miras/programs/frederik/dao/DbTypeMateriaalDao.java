package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbPersoon;
import be.miras.programs.frederik.dbo.DbTypeMateriaal;


/**
 * @author Frederik  Vanden Bussche
 * 
 * Dao voor het databankobject DbTypeMateriaal
 *
 */
public class DbTypeMateriaalDao implements ICRUD {
	private static final Logger LOGGER = Logger.getLogger(DbTypeMateriaalDao.class);
	
	@Override
	public int voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		int id = Integer.MIN_VALUE;
		Transaction transaction = null;
		try{
			DbTypeMateriaal typeMateriaal = (DbTypeMateriaal)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(typeMateriaal);
			id = typeMateriaal.getId();
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}	
		return id;
	}

	@Override
	public Object lees(int id) {
		DbTypeMateriaal typeMateriaal = new DbTypeMateriaal();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbTypeMateriaal where id = :id";
		List<DbTypeMateriaal> lijst = new ArrayList<DbTypeMateriaal>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", id);
			lijst = q.list();
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			typeMateriaal = lijst.get(0);
		}
		
		return typeMateriaal;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbTypeMateriaal> lijst = new ArrayList<DbTypeMateriaal>();
		String query = "FROM DbTypeMateriaal"; // SELECT * FROM DbTypeMateriaal
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			lijst = q.list();
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}
		
		List<Object> objectLijst = new ArrayList<Object>(lijst);
		return objectLijst;
	}

	@Override
	public void wijzig(Object o) {

		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			DbTypeMateriaal typeMateriaal = (DbTypeMateriaal)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(typeMateriaal);
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}
	}

	@Override
	public void verwijder(int id) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbTypeMateriaal where id = :id";
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", id);
			q.executeUpdate();
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}
	}

	/**
	 * @param naamType String
	 * @return int
	 * 
	 * return id van een DbTypeMateriaal met een bepaalde naam
	 */
	public int lees(String naamType) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT id FROM DbTypeMateriaal where naam = :naam";
		List<Integer> lijst = new ArrayList<Integer>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("naam", naamType);
			lijst = q.list();
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			id = lijst.get(0);
		}
		
		return id;
	}

	
}

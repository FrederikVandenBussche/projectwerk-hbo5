package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbStatus;

/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbStatus
 *
 */
public class DbStatusDao implements ICRUD {
	private static final Logger LOGGER = Logger.getLogger(DbStatusDao.class);
	private final String TAG = "DbStatusDao: ";
	
	@Override
	public int voegToe(Object o) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try{
			DbStatus status = (DbStatus)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(status);
			id = status.getId();
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: " + TAG + "voegToe() ", e);
		} finally {
			session.close();
		}	
		return id;
	}

	@Override
	public Object lees(int id) {
		DbStatus status = new DbStatus();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbStatus where id = :id";
		List<DbStatus> lijst = new ArrayList<DbStatus>();
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
			LOGGER.error("Exception: " + TAG + "lees(id)" + id + "", e);
		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			status = lijst.get(0);
		}
		
		return status;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbStatus> lijst = new ArrayList<DbStatus>();
		String query = "FROM DbStatus"; 
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
			LOGGER.error("Exception: " + TAG  + " leesAlle() ", e);
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
			DbStatus status = (DbStatus)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(status);
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: " + TAG + "wijzig(o)" , e);
		} finally {
			session.close();
		}
	}

	@Override
	public void verwijder(int id) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		LOGGER.error("Exception: " + TAG + "verwijder(id)" + id + " ", e);
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
	 * @param naam String
	 * @return int
	 * 
	 * return id van DbStatus met een bepaalde naam
	 */
	public int lees(String naam) {
		DbStatus status = new DbStatus();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbStatus where naam = :naam";
		List<DbStatus> lijst = new ArrayList<DbStatus>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("naam", naam);
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
			LOGGER.error("Exception: " + TAG + "lees(naam) " + naam + " ", e);
		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			status = lijst.get(0);
		}
		int id = status.getId();
		
		return id;
	}

	
}

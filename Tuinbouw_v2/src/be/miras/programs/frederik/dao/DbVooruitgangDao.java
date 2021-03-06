package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbVooruitgang;

/**
*@author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbVooruitgang
 *
 */
public class DbVooruitgangDao implements ICRUD {
	
	private static final Logger LOGGER = Logger.getLogger(DbVooruitgangDao.class);
	private static String TAG = "DbVooruitgangDao: ";
	
	public DbVooruitgangDao(){
	}
	
	@Override
	public int voegToe(Object o) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		
		try {
			DbVooruitgang vooruitgang = (DbVooruitgang) o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.save(vooruitgang);
			id = vooruitgang.getId();
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
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
		DbVooruitgang vooruitgang = new DbVooruitgang();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbVooruitgang where id = :id";
		List<DbVooruitgang> lijst = new ArrayList<DbVooruitgang>();
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
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
			vooruitgang = lijst.get(0);
		}

		return vooruitgang;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbVooruitgang> lijst = new ArrayList<DbVooruitgang>();
		String query = "FROM DbVooruitgang"; // SELECT * FROM werknemer
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			session.beginTransaction();
			transaction = session.getTransaction();
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
		DbVooruitgang vooruitgang = (DbVooruitgang) o;
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			session.saveOrUpdate(vooruitgang);
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: " + TAG + "wijzig(o), id = " 
			+ vooruitgang.getId() + ", statusId = " + vooruitgang.getStatusId() + " " , e);
		} finally {
			session.close();
		}
	}

	@Override
	public void verwijder(int id) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbVooruitgang where id = :id";
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
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
			LOGGER.error("Exception: " + TAG + "verwijder(id)" + id + " ", e);
		} finally {
			session.close();
		}
	}

	/**
	 * @param vooruitgangId int
	 * @return int
	 * 
	 * return select statusId from vooruitgang where id = geparamteriseerde vooruitgangId
	 */
	public int leesStatusId(int id) {
		int statusId = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT statusId FROM DbVooruitgang where id = :id";
		List<Integer> lijst = new ArrayList<Integer>();
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
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
			statusId = lijst.get(0);
		}

		return statusId;
	}

	
}

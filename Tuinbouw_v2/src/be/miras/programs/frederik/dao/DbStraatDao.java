package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbStraat;

/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbStraat
 *
 */
public class DbStraatDao implements ICRUD {
	
	private static final Logger LOGGER = Logger.getLogger(DbStraatDao.class);
	private final String TAG = "DbStraatDao: ";
	
	
	public DbStraatDao(){
	}
	
	@Override
	public int voegToe(Object o) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		
		try{
			DbStraat straat = (DbStraat)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.save(straat);
			id = straat.getId();
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
		DbStraat straat = new DbStraat();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbStraat where id = :id";
		List<DbStraat> lijst = new ArrayList<DbStraat>();
		
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
			straat = lijst.get(0);
		}
		
		return straat;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbStraat> lijst = new ArrayList<DbStraat>();
		String query = "FROM DbStraat";
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
		
		try {
			DbStraat straat = (DbStraat)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.saveOrUpdate(straat);
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
		String query = "DELETE FROM DbStraat where id = :id";
		
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
	 * @param straat String
	 * @return int
	 * 
	 * return id van DbStraat met een bepaalde naam
	 */
	public int geefIdVan(String straat) {
		int id = Integer.MIN_VALUE;
		List<DbStraat> lijst = new ArrayList<DbStraat>();
		String query = "FROM DbStraat where naam = :naam"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("naam", straat);
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
			LOGGER.error("Exception: " + TAG + "geefIdVan(straat) " + straat + " ", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()){
			DbStraat dbStraat = lijst.get(0);
			id = dbStraat.getId();
		} else {
			// deze straatnaam staat nog niet in de databank
		}
		
		return id;
	}

	
}

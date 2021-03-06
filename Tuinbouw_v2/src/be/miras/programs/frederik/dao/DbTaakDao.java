package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbTaak;

/**
 * @author Frederik  Vanden Bussche
 * 
 * Dao voor het databankobject DbTaak
 *
 */
public class DbTaakDao implements ICRUD {
	
	private static final Logger LOGGER = Logger.getLogger(DbTaakDao.class);
	private final String TAG = "DbTaakDao: ";
	
	
	public DbTaakDao(){
	}
	
	@Override
	public int voegToe(Object o) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		
		try{
			DbTaak taak = (DbTaak)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.save(taak);
			id = taak.getId();
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
		DbTaak taak = new DbTaak();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbTaak where id = :id";
		List<DbTaak> lijst = new ArrayList<DbTaak>();
		
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
			taak = lijst.get(0);
		}
		
		return taak;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbTaak> lijst = new ArrayList<DbTaak>();
		String query = "FROM DbTaak order by naam"; 
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
			DbTaak taak = (DbTaak)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.saveOrUpdate(taak);
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
		String query = "DELETE FROM DbTaak where id = :id";
		
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
	 * @param naam String
	 * @param visible int
	 * @return int
	 * 
	 * return de id van de DbTaak met een bepaalde naam en visible
	 */
	public int geefIdVan(String naam, int visible){
		int id = Integer.MIN_VALUE;
		List<DbTaak> lijst = new ArrayList<DbTaak>();
		String query = "FROM DbTaak WHERE naam = :naam";
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
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
			LOGGER.error("Exception: " + TAG + "geefIdVan(naam, visible " + naam + " " + visible + " ", e);
		} finally {
			session.close();
		}
		
		if (lijst.size() > 0){
			// er is een taak gevonden met dezelfde naam
			Iterator<DbTaak> it = lijst.iterator();
			while(it.hasNext()){
				DbTaak dbTaak = it.next();
				id = dbTaak.getId();
			}
		}
		
		return id;
	}

	/**
	 * @param taakId int
	 * @return String
	 * 
	 * return de naam van een taak met bepaalde id
	 */
	public String selectNaam(int taakId) {
		String naam = null;
		List<String> lijst = new ArrayList<String>();
		String query = "SELECT naam FROM DbTaak where id = :id"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", taakId);
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
			LOGGER.error("Exception: " + TAG + "selectNaam(taakId) " + taakId  + " ", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()){
			naam = lijst.get(0);
		}
		
		return naam;
	}


}

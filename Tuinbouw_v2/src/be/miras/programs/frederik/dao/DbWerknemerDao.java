package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbWerknemer;


/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbWerknemer
 *
 */
public class DbWerknemerDao implements ICRUD {
	
	private static final Logger LOGGER = Logger.getLogger(DbWerknemerDao.class);
	private final String TAG = "DbWerknemerDao";
	
	
	public DbWerknemerDao(){
	}

	@Override
	public int voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		int id = Integer.MIN_VALUE;
		Transaction transaction = null;
		
		try{
			DbWerknemer werknemer = (DbWerknemer)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.save(werknemer);
			id = werknemer.getId();
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: "+ TAG + "voegtoe() ", e);
		} finally {
			session.close();
		}	
		
		return id;
	}

	@Override
	public Object lees(int id) {
		DbWerknemer werknemer = new DbWerknemer();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbWerknemer where id = :id";
		List<DbWerknemer> lijst = new ArrayList<DbWerknemer>();
		
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
			LOGGER.error("Exception: " + TAG + "lees(id)" + id + " ", e);

		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()) {
			werknemer = lijst.get(0);
		}
		
		return werknemer;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbWerknemer> lijst = new ArrayList<DbWerknemer>();
		String query = "FROM DbWerknemer"; 
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
			LOGGER.error("Exception: " + TAG + "leesAlle() ", e);
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
			DbWerknemer werknemer = (DbWerknemer)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.saveOrUpdate(werknemer);
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: " + TAG + "wijzig() ", e);
		} finally {
			session.close();
		}
	}

	@Override
	public void verwijder(int id) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbWerknemer where id = :id";
		
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
	 * @param persoonId int
	 * 
	 * verwijder uit DbWerknemer met een bepaalde persoonId
	 */
	public void verwijderWaarPersoonId(int persoonId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbWerknemer where persoonId = :persoonId";
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("persoonId", persoonId);
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
			LOGGER.error("Exception: " + TAG + "verwijderWaarPersoonId(persoonId)" + persoonId + " ", e);
		} finally {
			session.close();
		}
	}

	/**
	 * @param werknemerId int
	 * @return int
	 * 
	 * return de persoonId aan de hand van een werknemerId
	 */
	public int returnPersoonId(int werknemerId) {
		int persoonId = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT persoonId FROM DbWerknemer where id = :id";
		List<Integer> lijst = new ArrayList<Integer>();
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", werknemerId);
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
			LOGGER.error("Exception: " + TAG + "returnPersoonId(werknemerId)" + werknemerId + " ", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()) {
			persoonId = lijst.get(0);
		}
		
		return persoonId;
	}

	/**
	 * @param werknemerId int
	 * @return int
	 * 
	 * return de werknemerId aan de hand van een persoonId
	 */
	public int returnWerknemerId(int persoonId) {
		int werknemerId = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT id FROM DbWerknemer where persoonId = :persoonId";
		List<Integer> lijst = new ArrayList<Integer>();
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("persoonId", persoonId);
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
			LOGGER.error("Exception: " + TAG + "returnWerknemerId(persoonId)" + persoonId + " ", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()) {
			werknemerId = lijst.get(0);
		}
		
		return werknemerId;
	}

	/**
	 * @param id int
	 * @return DbWerknemer
	 * 
	 * return de DbWerknemer met geparameteriseerde persoonId
	 */
	public DbWerknemer leesWaarPersoonId(int id) {
		DbWerknemer werknemer = new DbWerknemer();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbWerknemer where persoonId = :id";
		List<DbWerknemer> lijst = new ArrayList<DbWerknemer>();
		
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
			LOGGER.error("Exception: " + TAG + "leesWaarPersoonId(persoonId)" + id + " ", e);

		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()) {
			werknemer = lijst.get(0);
		}
		
		return werknemer;
	}


}

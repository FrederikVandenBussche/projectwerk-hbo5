package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbPersoonAdres;


/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbPersoonAdres
 *
 */
public class DbPersoonAdresDao implements ICRUD {
	private static final Logger LOGGER = Logger.getLogger(DbPersoonAdresDao.class);
	private final String TAG = "DbPersoonAdresDao: ";
	
	@Override
	public int voegToe(Object o) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try{
			DbPersoonAdres persoonAdres = (DbPersoonAdres)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(persoonAdres);
			id = persoonAdres.getId();
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
	public Object lees(int adresId) {
		DbPersoonAdres persoonAdres = new DbPersoonAdres();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbPersoonAdres where adresId = :adresId";
		List<DbPersoonAdres> lijst = new ArrayList<DbPersoonAdres>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("adresId", adresId);
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
			persoonAdres = lijst.get(0);
		}
		
		return persoonAdres;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbPersoonAdres> lijst = new ArrayList<DbPersoonAdres>();
		String query = "FROM DbPersoonAdres"; 
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
			DbPersoonAdres persoonAdres = (DbPersoonAdres)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(persoonAdres);
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
	public void verwijder(int persoonId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbPersoonAdres where persoonId = :persoonId";
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
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
			LOGGER.error("Exception: " + TAG + "verwijder(id)" + id + " ", e);
		} finally {
			session.close();
		}
	}
	
	/**
	 * @param id int
	 * @return List<Integer>
	 * 
	 * return een lijst van adresId die bij een bepaalde persoonId horen
	 */
	public List<Integer> leesLijst(int id) {
		List<Integer> lijst = new ArrayList<Integer>();
		String query = "SELECT adresId FROM DbPersoonAdres where persoonId = :id"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

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
			LOGGER.error("Exception: " + TAG + "leesLijst(id) " + id + " ", e);
		} finally {
				
			session.close();
		}
		
		return lijst;
	}
	
	/**
	 * @param persoonId int
	 * @param adresId int
	 * 
	 * verwijder DbPersoonAdres met een bepaalde persoonId EN adresId
	 */
	public void verwijder(int persoonId, int adresId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbPersoonAdres where persoonId = :persoonId and adresId = :adresId";
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("persoonId", persoonId);
			q.setParameter("adresId", adresId);
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
			LOGGER.error("Exception: " + TAG + "verwijder(persoonId, adresId) " + persoonId + adresId  + " ", e);
		} finally {
			session.close();
		}
	}
	

}

package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbMateriaal;

/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbMateriaal
 *
 */
public class DbMateriaalDao implements ICRUD {
	
	private static final Logger LOGGER = Logger.getLogger(DbMateriaalDao.class);
	private final String TAG = "DbMateriaalDao: ";
	
	
	public DbMateriaalDao(){
	}
	
	@Override
	public int voegToe(Object o) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		
		try{
			DbMateriaal materiaal = (DbMateriaal)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.save(materiaal);
			id = materiaal.getId();
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
		DbMateriaal materiaal = new DbMateriaal();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbMateriaal where id = :id";
		List<DbMateriaal> lijst = new ArrayList<DbMateriaal>();
		
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
			materiaal = lijst.get(0);
		}
		
		return materiaal;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbMateriaal> lijst = new ArrayList<DbMateriaal>();
		String query = "FROM DbMateriaal order by naam"; 
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
			DbMateriaal materiaal = (DbMateriaal)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.saveOrUpdate(materiaal);
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
		String query = "DELETE FROM DbMateriaal where id = :id";
		
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
	 * @param id int
	 * @return int
	 * 
	 * return tyeMateriaalId van DbMateriaal met bepaalde id
	 */
	public int geefTypeMateriaalId(int id) {
		int typeMateriaalId = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT typeMateriaalId FROM DbMateriaal where id = :id";
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
			LOGGER.error("Exception: " + TAG + "geefTypeMateriaalId(id)" + id + " ", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()) {
			typeMateriaalId = lijst.get(0);
		}
		
		return typeMateriaalId;
	}

	/**
	 * @param dbMateriaal DbMateriaal
	 * @return int
	 * 
	 * return de id van dit DbMateriaal
	 * Als deze record niet bestaat, return Integer.min_value
	 */
	public int haalId(DbMateriaal dbMateriaal) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT id FROM DbMateriaal where naam = :naam "
							+ "AND typeMateriaalId = :typeMateriaalId "
							+ "AND eenheid = :eenheid";
		List<Integer> lijst = new ArrayList<Integer>();
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("naam", dbMateriaal.getNaam());
			q.setParameter("typeMateriaalId", dbMateriaal.getTypeMateriaalId());
			q.setParameter("eenheid", dbMateriaal.getEenheid());
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
			LOGGER.error("Exception: " + TAG + "haalId(dbMateriaal)", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()) {
			id = lijst.get(0);
		}

		return id;
	}

	public List<DbMateriaal> leesWaarTypeMateriaalId(int typeMateriaalId) {
		List<DbMateriaal> lijst = new ArrayList<DbMateriaal>();
		String query = "FROM DbMateriaal WHERE typeMateriaalId = :typeMateriaalId"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("typeMateriaalId", typeMateriaalId);
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
			LOGGER.error("Exception: " + TAG  + " leesWaarTypeMateriaalId(typeMateriaalId) " + 
					" " + typeMateriaalId + " ", e);
		} finally {
			session.close();
		}
		
		return lijst;
	}

	/**
	 * @param typeMateriaalId int
	 * 
	 * verwijder alle materialen met de geparameteriseerde typeMateriaalId
	 */
	public void verwijderWaarTypeMateriaalId(int typeMateriaalId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbMateriaal where typeMateriaalId = :typeMateriaalId";
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("typeMateriaalId", typeMateriaalId);
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
			LOGGER.error("Exception: " + TAG + "verwijderWaarTypeMateriaalId(typeMateriaalId)" + typeMateriaalId + " ", e);
		} finally {
			session.close();
		}
	}

	
}

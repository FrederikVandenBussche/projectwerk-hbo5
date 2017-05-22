package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbOpdrachtMateriaal;

/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbOpdrachtMateriaal
 *
 */
public class DbOpdrachtMateriaalDao implements ICRUD {
	private static final Logger LOGGER = Logger.getLogger(DbOpdrachtMateriaalDao.class);
	private final String TAG = "DbOpdrachtMateriaalDao: ";
	
	@Override
	public int voegToe(Object o) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try{
			DbOpdrachtMateriaal opdrachtMateriaal = (DbOpdrachtMateriaal)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.save(opdrachtMateriaal);
			id = opdrachtMateriaal.getId();
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
		DbOpdrachtMateriaal opdrachtMateriaal = new DbOpdrachtMateriaal();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbOpdrachtMateriaal where id = :id";
		List<DbOpdrachtMateriaal> lijst = new ArrayList<DbOpdrachtMateriaal>();
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
			opdrachtMateriaal = lijst.get(0);
		}
		
		return opdrachtMateriaal;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbOpdrachtMateriaal> lijst = new ArrayList<DbOpdrachtMateriaal>();
		String query = "FROM DbOpdrachtMateriaal"; 
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
			DbOpdrachtMateriaal opdrachtMateriaal = (DbOpdrachtMateriaal)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.saveOrUpdate(opdrachtMateriaal);
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
		String query = "DELETE FROM DbOpdrachtMateriaal where id = :id";
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
	 * @param opdrachtId int
	 * @return List<DbOpdrachtMateriaal>
	 * 
	 * haal een lijst met DbOpdrachtMateriaal op met een bepaalde opdrachtId
	 */
	public List<DbOpdrachtMateriaal> leesWaarOpdrachtId(int opdrachtId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbOpdrachtMateriaal where opdrachtId = :opdrachtId";
		List<DbOpdrachtMateriaal> lijst = new ArrayList<DbOpdrachtMateriaal>();
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("opdrachtId", opdrachtId);
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
			LOGGER.error("Exception: " + TAG + "leesWaarOpdrachtId(opdrachtId) " + opdrachtId + " ", e);
		} finally {
			session.close();
		}
		
		return lijst;
	}

	/**
	 * @param opdrachtId int
	 * 
	 * verwijder alle DbOpdrachtMateriaal met een bepaalde opdrachtId
	 */
	public void verwijderWaarOpdrachtId(int opdrachtId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbOpdrachtMateriaal where opdrachtId = :opdrachtId";
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("opdrachtId", opdrachtId);
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
			LOGGER.error("Exception: " + TAG + "verwijderWaarOpdrachtId(opdrachtId) " + opdrachtId + " ", e);
		} finally {
			session.close();
		}
	}

	/**
	 * @param materiaalId int
	 * @return boolean
	 * 
	 * return true indien deze materiaalId voorkomt in de tabel.
	 */
	public boolean isMateriaalKomtVoor(int materiaalId) {
		boolean isKomtvoor = false;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT COUNT(id) FROM DbOpdrachtMateriaal where materiaalId = :materiaalId";
		List<Long> lijst = new ArrayList<Long>();
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("materiaalId", materiaalId);
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
			LOGGER.error("Exception: " + TAG + "isMateriaalKomtVoor(int materiaalId)" + materiaalId + "", e);
		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			Long aantal = lijst.get(0);
			if (aantal > 0){
				isKomtvoor = true;
			}
		}
		
		return isKomtvoor;
	}

		/**
	 * @param opdrachtId int
	 * @param materiaalId int
	 * 
	 * verwijder alle DbOpdrachtMateriaal met een bepaalde opdrachtId EN materiaalId
	 */
	public void verwijderWaarOpdrachtIdEnMateriaalId(int opdrachtId, int materiaalId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbOpdrachtMateriaal "
						+ "where opdrachtId = :opdrachtId "
						+ "AND materiaalId = :materiaalId";
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("opdrachtId", opdrachtId);
			q.setParameter("materiaalId", materiaalId);
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
			LOGGER.error("Exception: " + TAG + "verwijderWaarOpdrachtIdEnMateriaalId(opdrachtId, materiaalId) "
			+ opdrachtId + " " + materiaalId + " ", e);
		} finally {
			session.close();
		}
	}
	
}

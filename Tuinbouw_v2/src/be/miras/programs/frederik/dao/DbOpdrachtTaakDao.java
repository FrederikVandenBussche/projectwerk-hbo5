package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbOpdrachtTaak;

/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbOpdrachtTaak
 *
 */
public class DbOpdrachtTaakDao implements ICRUD {
	private static final Logger LOGGER = Logger.getLogger(DbOpdrachtTaakDao.class);
	private final String TAG = "DbOpdrachtTaakDao: ";
	
	@Override
	public int voegToe(Object o) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try{
			DbOpdrachtTaak opdrachtTaak = (DbOpdrachtTaak)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.save(opdrachtTaak);
			id = opdrachtTaak.getId();
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
		DbOpdrachtTaak opdrachtTaak = new DbOpdrachtTaak();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbOpdrachtTaak where id = :id";
		List<DbOpdrachtTaak> lijst = new ArrayList<DbOpdrachtTaak>();
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
			opdrachtTaak = lijst.get(0);
		}
		
		return opdrachtTaak;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbOpdrachtTaak> lijst = new ArrayList<DbOpdrachtTaak>();
		String query = "FROM DbOpdrachtTaak"; 
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
			DbOpdrachtTaak opdrachtTaak = (DbOpdrachtTaak)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.saveOrUpdate(opdrachtTaak);
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
		String query = "DELETE FROM DbOpdrachtTaak where id = :id";
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
	 * @return List<DbOpdrachtTaak>
	 * 
	 * return een List<DbOpdrachtTaak> met een bepaalde opdrachtId
	 */
	public List<DbOpdrachtTaak> leesLijst(int opdrachtId) {
		List<DbOpdrachtTaak> lijst = new ArrayList<DbOpdrachtTaak>();
		String query = "FROM DbOpdrachtTaak where opdrachtId = :opdrachtId"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

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
			LOGGER.error("Exception: " + TAG + "leesLijst(opdrachtId) " + opdrachtId + " ", e);
		} finally {
			session.close();
		}
		
		return lijst;
	}

	/**
	 * @param opdrachtId int
	 * @param taakId int
	 * @param opmerking String
	 * 
	 * wijzig opmerking van de DbOpdrachtTaak met een bepaalde opdrachtId EN taakId
	 */
	public void wijzigOpmerking(int opdrachtId, int taakId,  String opmerking) {
		Session session = HibernateUtil.openSession();
		String query = "UPDATE DbOpdrachtTaak SET opmerking = :opmerking "
				+ "WHERE opdrachtId = :opdrachtId"
				+ " AND taakId = :taakId";
		Transaction transaction = null;
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("opmerking", opmerking);
			q.setParameter("opdrachtId", opdrachtId);
			q.setParameter("taakId", taakId);
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
			LOGGER.error("Exception: " + TAG + "wijzigOpmerking(opdrachtId, taakId, opmerking " + opdrachtId + " " + taakId + " " + opmerking + " ", e);
		} finally {
			session.close();
		}
		
	}

	/**
	 * @param taakId int
	 * @return Long
	 * 
	 * return het aantal DbOpdrachtTaak met bepaalde taakId
	 */
	public Long hoeveelMetTaakId(int  taakId) {
		Long aantal = Long.MIN_VALUE;
		
		String query = "SELECT COUNT(*) FROM DbOpdrachtTaak "
				+ "where taakId = :taakId";
		
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		List<Long> results = null;
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("taakId", taakId);
			results = q.list();
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: " + TAG + "hoeveelMetTaakId " + taakId + " ", e);
		} finally {
			session.close();
		}
		
		if (!results.isEmpty()){
			aantal = results.get(0);
		}
		
		return aantal;
	}

	/**
	 * @param opdrachtId int
	 * 
	 * verwijder alle DbOpdrachtTaak met een bepaalde opdrachtId
	 */
	public void verwijderWaarOpdrachtId(int opdrachtId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbOpdrachtTaak where opdrachtId = :opdrachtId";
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
	 * @param opdrachtId int
	 * @return List<Integer>
	 * 
	 * return lijst met vooruitgangId's met een bepaalde opdrachtId 
	 */
	public List<Integer> leesVooruitgangIds(int opdrachtId) {
		List<Integer> lijst = new ArrayList<Integer>();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT vooruitgangId FROM DbOpdrachtTaak WHERE opdrachtId = :opdrachtId";
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
			LOGGER.error("Exception: " + TAG + "leesVooruitgangIds(opdrachtId) " + opdrachtId + " ", e);
		} finally {
			session.close();
		}
		return lijst;
	}

	/**
	 * @param taakId int
	 * @return DbOpdrachtTaak
	 * 
	 * return DbOpdrachtTaak met een bepaalde taakId
	 */
	public DbOpdrachtTaak leesWaarTaakId(int taakId) {
		DbOpdrachtTaak opdrachtTaak = new DbOpdrachtTaak();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbOpdrachtTaak where taakId = :taakId";
		List<DbOpdrachtTaak> lijst = new ArrayList<DbOpdrachtTaak>();
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("taakId", taakId);
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
			LOGGER.error("Exception: " + TAG + "leesWaarTaakId(taakId) " + taakId + " ", e);
		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			opdrachtTaak = lijst.get(0);
		}
		
		return opdrachtTaak;
	}

	
}

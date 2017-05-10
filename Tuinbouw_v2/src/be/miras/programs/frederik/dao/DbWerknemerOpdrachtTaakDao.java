package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbWerknemerOpdrachtTaak;


/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbWerknemerOpdrachtTaak
 *
 */
public class DbWerknemerOpdrachtTaakDao implements ICRUD {
	private static final Logger LOGGER = Logger.getLogger(DbWerknemerOpdrachtTaakDao.class);
	private final String TAG = "DbWerknemerOpdrachtTaakDao: ";

	@Override
	public int voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		int id = Integer.MIN_VALUE;
		Transaction transaction = null;
		try{
			DbWerknemerOpdrachtTaak dwot = (DbWerknemerOpdrachtTaak)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(dwot);
			id = dwot.getId();
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: " + TAG + "voegtoe() ", e);
		} finally {
			session.close();
		}	
		return id;
	}

	@Override
	public Object lees(int id) {
		DbWerknemerOpdrachtTaak dwot = new DbWerknemerOpdrachtTaak();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbWerknemerOpdrachtTaak where id = :id";
		List<DbWerknemerOpdrachtTaak> lijst = new ArrayList<DbWerknemerOpdrachtTaak>();
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
			LOGGER.error("Exception: " + TAG + "lees() ", e);
		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			dwot = lijst.get(0);
		}
		
		return dwot;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbWerknemerOpdrachtTaak> lijst = new ArrayList<DbWerknemerOpdrachtTaak>();
		String query = "FROM DbWerknemerOpdrachtTaak"; 
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
			DbWerknemerOpdrachtTaak dwot = (DbWerknemerOpdrachtTaak)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(dwot);
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
		String query = "DELETE FROM DbWerknemerOpdrachtTaak where id = :id";
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
			LOGGER.error("Exception: " + TAG + "verwijder(id) " + id + " ", e);
		} finally {
			session.close();
		}
	}

	/**
	 * @param taakId int
	 * @return Lijst<DbWerknemerOpdrachtTaak>
	 * 
	 * returnt waar opdrachtTaakTaakId
	 */
	public List<DbWerknemerOpdrachtTaak> leesWaarTaakId(int taakId) {
		List<DbWerknemerOpdrachtTaak> lijst = new ArrayList<DbWerknemerOpdrachtTaak>();
		String query = "FROM DbWerknemerOpdrachtTaak where opdrachtTaakTaakId = :taakId"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.getTransaction();
			session.beginTransaction();
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
			LOGGER.error("Exception: " + TAG + "leesWaarTaakId(taakId)" + taakId + " " , e);
		} finally {
			session.close();
		}
		
		return lijst;
	}

	/**
	 * @param opdrachtId int
	 * 
	 * verwijder DbWerknemerOpdrachtTaak met een bepaalde opdrachtId
	 */
	public void verwijderWaarOpdrachtId(int opdrachtId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbWerknemerOpdrachtTaak where opdrachtTaakOpdrachtId = :opdrachtId";
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
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
	 * @param taakId int
	 * 
	 * verwijder DbWerknemerOpdrachtTaak met een bepaalde taakId
	 */
	public void verwijderWaarTaakId(int taakId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbWerknemerOpdrachtTaak where opdrachtTaakTaakId = :taakId";
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
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
			LOGGER.error("Exception: " + TAG + "verwijderWaarTaakId(taakId) " + taakId + " ", e);
		} finally {
			session.close();
		}
		
	}

	/**
	 * @param werknemerId int
	 * @return List<Object>
	 * 
	 * haal OpdrachtId, TaakId en Beginuur met een bepaalde werknemerId uit databank
	 */
	public List<Object> leesOpdrachtIdTaakIdBeginuur(int werknemerId) {
		List<Object> lijst = new ArrayList<Object>();
		String query = "SELECT id, opdrachtTaakOpdrachtId, opdrachtTaakTaakId, beginuur "
				+ "FROM DbWerknemerOpdrachtTaak where werknemerId = :werknemerId"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("werknemerId", werknemerId);
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
			LOGGER.error("Exception: " + TAG + " leesOpdrachtIdTaakIdBeginuur(werknemerId)" + werknemerId + " ", e);
		} finally {
			session.close();
		}
		
		return lijst;
	}


}

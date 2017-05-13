package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbWerknemerOpdrachtTaak;
import be.miras.programs.frederik.util.Datum;


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
			session.beginTransaction();
			transaction = session.getTransaction();
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
			DbWerknemerOpdrachtTaak dwot = (DbWerknemerOpdrachtTaak)o;
			session.beginTransaction();
			transaction = session.getTransaction();
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
	 * @param taakId int
	 * 
	 * verwijder DbWerknemerOpdrachtTaak met een bepaalde taakId
	 */
	public void verwijderWaarTaakId(int taakId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbWerknemerOpdrachtTaak where opdrachtTaakTaakId = :taakId";
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
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
			session.beginTransaction();
			transaction = session.getTransaction();
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

	/**
	 * @param dag Date
	 * @return List<DbWerknemerOpdrachtTaak>
	 * 
	 * return een lijst met DbWerknemerOpdrachtTaak met dezelfde beginuur-datum als 
	 * de geparameteriseerde datum.
	 */
	public List<DbWerknemerOpdrachtTaak> lees(Date datum) {
		 int dag = datum.getDate();
		 int maand = datum.getMonth() + 1;
		 int jaar = datum.getYear() + 1900;
		 	
		 List<DbWerknemerOpdrachtTaak> lijst = new ArrayList<DbWerknemerOpdrachtTaak>();
		 String query = "FROM DbWerknemerOpdrachtTaak "
		 		+ "WHERE YEAR(beginuur) = :jaar "
		 		+ "AND MONTH(beginuur) = :maand "
		 		+ "AND DAY(beginuur) = :dag"; 
		
		 Session session = HibernateUtil.openSession();
		 Transaction transaction = null;
		 
		 try {
			 session.beginTransaction();
				transaction = session.getTransaction();
			 Query q = session.createQuery(query);
			 q.setParameter("jaar", jaar);
			 q.setParameter("maand", maand);
			 q.setParameter("dag", dag);
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
			 LOGGER.error("Exception: " + TAG + "lees(datum)" + datum.toString() + " " , e);
		} finally {
			session.close();
		}
		
		return lijst;
	}

	/**
	 * @param werknemerId int
	 * @return boolean
	 * 
	 * return true indien deze werknemerId voorkomt in de tabel.
	 */
	public boolean isWerknemerKomtVoor(int werknemerId) {
		boolean isKomtvoor = false;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT COUNT(id) FROM DbWerknemerOpdrachtTaak where werknemerId = :werknemerId";
		List<Long> lijst = new ArrayList<Long>();
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
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
			LOGGER.error("Exception: " + TAG + "isWerknemerKomtVoor(int werknemerId)" + werknemerId + " ", e);
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


}

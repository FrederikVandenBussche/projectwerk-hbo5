package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbWerkgever;


/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbWerkgever
 *
 */
public class DbWerkgeverDao implements ICRUD {
	private static final Logger LOGGER = Logger.getLogger(DbWerkgeverDao.class);
	private final String TAG = "DbWerkgeverDao: ";
	
	@Override
	public int voegToe(Object o) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			DbWerkgever werkgever = (DbWerkgever) o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(werkgever);
			id = werkgever.getId();
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: " + TAG + "voegToe(o) ", e);
		} finally {
			session.close();
		}
		return id;
	}

	@Override
	public Object lees(int id) {
		DbWerkgever werkgever = new DbWerkgever();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbWerkgever where id = :id";
		List<DbWerkgever> lijst = new ArrayList<DbWerkgever>();
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
			LOGGER.error("Exception: " + TAG + "lees(id) " + id + " ", e);

		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			werkgever = lijst.get(0);
		}

		return werkgever;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbWerkgever> lijst = new ArrayList<DbWerkgever>();
		String query = "FROM DbWerkgever"; 
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
			LOGGER.error("Exception: "+ TAG + "leesAlle() " , e);
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
			DbWerkgever werkgever = (DbWerkgever) o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(werkgever);
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: " + TAG + "wijzig(o) ", e);
		} finally {
			session.close();
		}
	}

	@Override
	public void verwijder(int id) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbWerkgever where id = :id";
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
	 * @param persoonId int
	 * @return DbWerkgever
	 * 
	 * return DbWerkgever met een bepaalde persoonId
	 */
	public DbWerkgever geefId(int persoonId) {
		DbWerkgever werkgever = new DbWerkgever();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbWerkgever where persoonId = :persoonId";
		List<DbWerkgever> lijst = new ArrayList<DbWerkgever>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
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
			LOGGER.error("Exception: " + TAG + "geefId(persoonId) " + persoonId + " ", e);

		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			werkgever = lijst.get(0);
		}
		
		return werkgever;
	}
	
	
}

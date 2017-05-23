package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbOpdracht;

/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbOpdracht
 *
 */
public class DbOpdrachtDao implements ICRUD {
	
	private static final Logger LOGGER = Logger.getLogger(DbOpdrachtDao.class);
	private final String TAG = "DbOpdrachtDao: ";
	
	
	public DbOpdrachtDao(){
	}
	
	@Override
	public int voegToe(Object o) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		
		try{
			DbOpdracht opdracht = (DbOpdracht)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.save(opdracht);
			id = opdracht.getId();
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
		DbOpdracht opdracht = new DbOpdracht();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbOpdracht where id = :id";
		List<DbOpdracht> lijst = new ArrayList<DbOpdracht>();
		
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
			opdracht = lijst.get(0);
		}
		
		return opdracht;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbOpdracht> lijst = new ArrayList<DbOpdracht>();
		String query = "FROM DbOpdracht order by startdatum desc"; 
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
			DbOpdracht opdracht = (DbOpdracht)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.saveOrUpdate(opdracht);
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
		String query = "DELETE FROM DbOpdracht where id = :id";
		
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
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}
	}

	/**
	 * @param id int
	 * @return String[]
	 * 
	 * return de klantId en de naam van DbOpdracht met geparameteriseerde id
	 * String[0] : klantId
	 * String[1] : opdrachtNaam
	 */
	public String[] selectKlantIdEnNaam(int id) {
		String[] returnData = new String[2];
		List<Object[]> lijst = new ArrayList<Object[]>();
		String query = "SELECT klantId, naam FROM DbOpdracht WHERE id = :id"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

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
			LOGGER.error("Exception: " + TAG + "verwijder(id)" + id + " ", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()){
			Object[]  o = lijst.get(0);
			returnData[0] = String.valueOf((int) o[0]);
			returnData[1] = (String) o[1];			
		}
		
		return returnData;
	}

	/**
	 * @param klantId int
	 * @return Lijst<DbOpdracht>
	 * 
	 * return een lijst met DbOpdrachten met een bepaalde klantId
	 */
	public List<DbOpdracht> leesWaarKlantId(int klantId) {
		List<DbOpdracht> lijst = new ArrayList<DbOpdracht>();
		String query = "FROM DbOpdracht WHERE klantId = :klantId order by startdatum desc"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("klantId", klantId);
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
			LOGGER.error("Exception: " + TAG + "leesWaarKlantId(klantId) " + klantId + " ", e);
		} finally {
			session.close();
		}

		return lijst;
	}

	/**
	 * @param id int
	 * @return int
	 * 
	 * return klantAdresId van den DbOpdracht met geparameteriseerde id
	 */
	public int geefKlantAdresId(int id) {
		int klantAdresId = Integer.MIN_VALUE;
		List<Integer> lijst = new ArrayList<Integer>();
		String query = "SELECT klantAdresId FROM DbOpdracht where id = :id"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

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
			LOGGER.error("Exception: " + TAG + "geefKlantAdresId(id) " + id + " ", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()){
			klantAdresId = lijst.get(0);
		}
		
		return klantAdresId;
	}

	/**
	 * @param id int
	 * @return int
	 * 
	 * return klantId van den DbOpdracht met geparameteriseerde id
	 */
	public int geefKlantId(int id) {
		int klantId = Integer.MIN_VALUE;
		List<Integer> lijst = new ArrayList<Integer>();
		String query = "SELECT klantId FROM DbOpdracht where id = :id"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

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
			LOGGER.error("Exception: " + TAG + "geefAdresId(id) " + id + " ", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()){
			klantId = lijst.get(0);
		}
		
		return klantId;
	}
	
	/**
	 * @param adresId int
	 * @return boolean
	 * return true indien dit klantAdresId voorkomt in de tabel.
	 */
	public boolean isKlantAdresKomtVoor(int klantAdresId) {
		boolean isKomtvoor = false;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT COUNT(id) FROM DbOpdracht where klantAdresId = :klantAdresId";
		List<Long> lijst = new ArrayList<Long>();
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("klantAdresId", klantAdresId);
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
			LOGGER.error("Exception: " + TAG + "isKlantAdresKomtVoor(int klantAdresId)" + klantAdresId + "", e);
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

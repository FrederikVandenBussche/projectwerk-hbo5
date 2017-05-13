package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbKlantAdres;

/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbKlantAdres
 *
 */
public class DbKlantAdresDao implements ICRUD {
	private static final Logger LOGGER = Logger.getLogger(DbKlantAdresDao.class);
	private final String TAG = "DbKlantAdresDao: ";
	
	@Override
	public int voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		int id = Integer.MIN_VALUE;
		Transaction transaction = null;
		try{
			DbKlantAdres klantAdres = (DbKlantAdres)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.save(klantAdres);
			id = klantAdres.getId();
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
	public Object lees(int klantId) {
		DbKlantAdres klantAdres = new DbKlantAdres();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbKlantAdres where klantId = :id";
		List<DbKlantAdres> lijst = new ArrayList<DbKlantAdres>();
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", klantId);
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
			LOGGER.error("Exception: " + TAG + "lees(klantId)" + klantId + "", e);
		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			klantAdres = lijst.get(0);
		}
		
		return klantAdres;
	}

	@Override
	public List<Object> leesAlle() {

		return null;
	}

	@Override
	public void wijzig(Object o) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			DbKlantAdres klantAdres = (DbKlantAdres)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.saveOrUpdate(klantAdres);
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
	public void verwijder(int klantId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbKlantAdres where klantId = :id";
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", klantId);
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
			LOGGER.error("Exception: " + TAG + "verwijder(klantId)" + klantId + " ", e);
		} finally {
			session.close();
		}
	}
	
	/**
	 * @param klantId int
	 * @return List<Integer>
	 * 
	 * retun een lijst met adresId die bij een klantId horen
	 */
	public List<Integer> leesLijst(int klantId) {
		List<Integer> lijst = new ArrayList<Integer>();
		String query = "SELECT adresId FROM DbKlantAdres where klantId = :id"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", klantId);
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
			LOGGER.error("Exception: " + TAG + "leesLijst(klantId) " + klantId + " ", e);
		} finally {
			session.close();
		}
		
		return lijst;
	}

	/**
	 * @param klantId int
	 * @param adresId int
	 * 
	 * verwijder DbKlatnAdres met klantId EN adresId
	 */
	public void verwijder(int klantId, int adresId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbKlantAdres where klantId = :id and adresId = :adresId";
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", klantId);
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
			LOGGER.error("Exception: " + TAG + "verwijder(klantId, adresId) " + klantId + " " + adresId + " ", e);
		} finally {
			session.close();
		}
		
	}

	/**
	 * @param id int
	 * @return int
	 * 
	 * return het eerste adresId met geparameteriseerde id
	 */
	public int geefEersteAdresId(int id) {
		int eersteAdresId = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT adresId FROM DbKlantAdres where id = :id";
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
			LOGGER.error("Exception: " + TAG + "geefEersteAdresId(id) " + id + " ", e);
		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			eersteAdresId = lijst.get(0);
		}
		
		return eersteAdresId;
	}

	/**
	 * @param klantId int
	 * @param adresId int
	 * @return int
	 * 
	 * return id van DbKlantAdres met een bepaald klantId EN adresId
	 */
	public int geefId(int klantId, int adresId){
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT id FROM DbKlantAdres where klantId = :klantId AND adresId = :adresId";
		List<Integer> lijst = new ArrayList<Integer>();
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("klantId", klantId);
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
			LOGGER.error("Exception: " + TAG + "geefId(klantId, adresId) " + klantId + " " + adresId  + " ", e);
		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			id = lijst.get(0);
		}
		return id;
	}
	
	/**
	 * @param id int
	 * @return DbKlantAdres
	 * 
	 * select * from dbKlantAdres where id = id
	 */
	public DbKlantAdres select(int id){
		DbKlantAdres klantAdres = new DbKlantAdres();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbKlantAdres where id = :id";
		List<DbKlantAdres> lijst = new ArrayList<DbKlantAdres>();
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
			LOGGER.error("Exception: " + TAG + "select(id)" + id + "", e);
		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			klantAdres = lijst.get(0);
		}
		
		return klantAdres;
	};

	
}

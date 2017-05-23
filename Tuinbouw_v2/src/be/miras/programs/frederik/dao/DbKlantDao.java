package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbBedrijf;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbParticulier;

/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbKlant
 *
 */
public class DbKlantDao implements ICRUD {
	
	private static final Logger LOGGER = Logger.getLogger(DbKlantDao.class);
	private final String TAG = "DbKlantDao: ";
	
	
	public DbKlantDao(){
	}
	
	@Override
	public int voegToe(Object o) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		
		try{
			DbKlant klant = (DbKlant)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.save(klant);
			id = klant.getId();
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
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbKlant where id = :id";
		List<DbKlant> lijst = new ArrayList<DbKlant>();
		DbKlant dbKlant = null;
		
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
			dbKlant = lijst.get(0);
		}
		
		return dbKlant;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbKlant> lijst = new ArrayList<DbKlant>();
		String query = "FROM DbKlant"; 
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
			DbKlant klant = (DbKlant)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.saveOrUpdate(klant);
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
		String query = "DELETE FROM DbKlant where id = :id";
		
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
	 * @return Object
	 * 
	 * return select * from DbParticulier
	 */
	public Object leesAlleParticulier() {
		List<DbParticulier> lijst = new ArrayList<DbParticulier>();
		String query = "FROM DbParticulier order by naam";
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
			LOGGER.error("Exception: " + TAG + "leesAllePartiuculier() ", e);
		} finally {
			session.close();
		}
		List<Object> objectLijst = new ArrayList<Object>(lijst);
		
		return objectLijst;
	}

	/**
	 * @return Object
	 * 
	 * return select * from BdBedrijf
	 */
	public Object leesAlleBedrijf() {
		List<DbBedrijf> lijst = new ArrayList<DbBedrijf>();
		String query = "FROM DbBedrijf order by bedrijfnaam"; 
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
			LOGGER.error("Exception: " + TAG + "leesAlleBedrijf() ", e);
		} finally {
			session.close();
		}
		List<Object> objectLijst = new ArrayList<Object>(lijst);
		
		return objectLijst;
	}

	/**
	 * @param id int
	 * @return DbKlant
	 * 
	 * return de DbKlant met geparameteriseerde id
	 */
	public DbKlant leesParticulier(int id) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbParticulier where id = :id";
		List<DbKlant> lijst = new ArrayList<DbKlant>();
		DbKlant dbKlant = new DbParticulier();
		
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
			LOGGER.error("Exception: " + TAG + "leesParticulier(id)" + id + "", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()) {
			dbKlant = lijst.get(0);
		}
		
		return dbKlant;
	}
	
	/**
	 * @param id int
	 * @return DbKlant
	 * 
	 * return de DbKlant met geparameteriseerde id
	 */
	public DbKlant leesBedrijf(int id) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbBedrijf where id = :id";
		List<DbKlant> lijst = new ArrayList<DbKlant>();
		DbKlant dbKlant = new DbBedrijf();
		
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
			LOGGER.error("Exception: " + TAG + "leesBedrijf(id)" + id + "", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()) {
			dbKlant = lijst.get(0);
		}
		
		return dbKlant;
	}

	
}

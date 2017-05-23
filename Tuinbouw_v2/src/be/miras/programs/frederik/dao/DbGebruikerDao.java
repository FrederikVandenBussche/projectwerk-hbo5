package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbGebruiker;

/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbGebruiker
 *
 */
/**
 * @author Frederik
 *
 */
public class DbGebruikerDao implements ICRUD {
	
	private static final Logger LOGGER = Logger.getLogger(DbGebruikerDao.class);
	private final String TAG = "DbGebruikerDao: ";
	
	
	public DbGebruikerDao(){
	}
	
	@Override
	public int voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		int id = Integer.MIN_VALUE;
		Transaction transaction = null;
		
		try{
			DbGebruiker gebruiker = (DbGebruiker)o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.save(gebruiker);
			id = gebruiker.getId();
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
		DbGebruiker gebruiker = new DbGebruiker();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbGebruiker where id = :id";
		List<DbGebruiker> lijst = new ArrayList<DbGebruiker>();
		
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
			gebruiker = lijst.get(0);
		}
		
		return gebruiker;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbGebruiker> lijst = new ArrayList<DbGebruiker>();
		String query = "FROM DbGebruiker"; 
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
			DbGebruiker gebruiker = (DbGebruiker) o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.saveOrUpdate(gebruiker);
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
		String query = "DELETE FROM DbGebruiker where id = :id";
		
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
	 * @param idLijst int[]
	 * @return Lijst<DbGebruiker>
	 * 
	 * return een lijst met DbGebruiker aan de hand van een bepaalde IdLijst
	 */
	public List<DbGebruiker> lees(int[] idLijst){
		List<DbGebruiker> gebruikerLijst = new ArrayList<DbGebruiker>();
		String query = "FROM DbGebruiker where persoonId = :persoonId";
		
		for (int i = 0; i < idLijst.length; i++){
			Session session = HibernateUtil.openSession();
			Transaction transaction = null;
			transaction = session.getTransaction();
			List<DbGebruiker> lijst = new ArrayList<DbGebruiker>();
			
			try{
				session.beginTransaction();
				transaction = session.getTransaction();
				Query q = session.createQuery(query);
				q.setParameter("persoonId", idLijst[i]);
				lijst = q.list();
				session.flush();
				if(!transaction.wasCommitted()){
					transaction.commit();
				}
			} catch (Exception e){
				if (transaction != null){
					transaction.rollback();
				}
				e.printStackTrace();
				LOGGER.error("Exception: " + TAG + "lees(idLijst) ", e);
			} finally {
				session.close();
			}
			
			if(!lijst.isEmpty()){
				DbGebruiker gebruiker = lijst.get(0);
				gebruikerLijst.add(gebruiker);
			}
		}
		
		return gebruikerLijst;
	}

	/**
	 * @param id int
	 * @return String
	 * 
	 * return wachtwoord van DbGebruiker met bepaald id;
	 */
	public String leesWachtwoord(int id) {
		String wachtwoord = null;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT wachtwoord FROM DbGebruiker where id = :id";
		List<String> lijst = new ArrayList<String>();
		
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
			LOGGER.error("Exception: " + TAG + "leesWachtwoord(id) " + id + " ", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()) {
			wachtwoord = lijst.get(0);
		}
		
		return wachtwoord;
	}

	/**
	 * @param id int
	 * @param wachtwoord String
	 * 
	 * wijzig het wachtwoord van DbGebruiker met bepaald id en het geparameteriseerde wachtwoord
	 */
	public void wijzigWachtwoord(int id, String wachtwoord) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "UPDATE DbGebruiker SET wachtwoord = :wachtwoord where id = :id";
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("wachtwoord", wachtwoord);
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
			LOGGER.error("Exception: " + TAG + "wijzigWachtwoord(id, wachtwoord) " + id + " " + wachtwoord + " ", e);
		} finally {
			session.close();
		}		
	}

	/**
	 * @param gebruikersnaam String
	 * @return DbGebruiker
	 * 
	 * return DbGebruiker met een bepaalde gebruikersnaam
	 */
	public DbGebruiker getGebruiker(String gebruikersnaam) {
		DbGebruiker gebruiker = new DbGebruiker();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbGebruiker where gebruikersnaam = :gebruikersnaam";
		List<DbGebruiker> lijst = new ArrayList<DbGebruiker>();
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("gebruikersnaam", gebruikersnaam);
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
			LOGGER.error("Exception: " + TAG + "getGebruiker(gebruikersnaam) " + gebruikersnaam + " ", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()) {
			gebruiker = lijst.get(0);
		}
		
		return gebruiker;
	}

	
	/**
	 * @param gebruikersnaam String
	 * @return int
	 * 
	 * return het aantal DbGebruikers met bepaalde gebruikersnaam
	 */
	public int aantalMetGebruikersnaam(String gebruikersnaam) {
		int  aantal = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT COUNT(*) FROM DbGebruiker where gebruikersnaam = :gebruikersnaam";
		List<DbGebruiker> lijst = new ArrayList<DbGebruiker>();
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("gebruikersnaam", gebruikersnaam);
			long result = (long) q.uniqueResult();
			aantal = (int) result;
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: " + TAG + "aantalMetGebruikersnaam(gebruikersnaam) " + gebruikersnaam + " ", e);
		} finally {
			session.close();
		}
		
		return aantal;
	}

	/**
	 * @param persoonId id
	 * 
	 * verwijder DbGebruiker met bepaalde persoonId
	 */
	public void verwijderWaarPersoonId(int persoonId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbGebruiker where persoonId = :persoonId";
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
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
			LOGGER.error("Exception: " + TAG + "verwijderWaarPersoonId(persoonId) " + persoonId + " ", e);
		} finally {
			session.close();
		}
	}

	/**
	 * @param persoonId int
	 * @return DbGebruiker
	 * 
	 * return de DbGebruiker met geparameteriseerde persoonId
	 */
	public DbGebruiker leesWaarPersoonId(int persoonId) {
		DbGebruiker gebruiker = new DbGebruiker();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbGebruiker where persoonId = :persoonId";
		List<DbGebruiker> lijst = new ArrayList<DbGebruiker>();
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
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
			LOGGER.error("Exception: " + TAG + "leesWaarPersoonId(persoonId)" + persoonId + "", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()) {
			gebruiker = lijst.get(0);
		}
		
		return gebruiker;
	}
	
	
}

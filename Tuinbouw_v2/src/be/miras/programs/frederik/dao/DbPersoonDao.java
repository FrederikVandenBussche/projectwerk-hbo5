package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbPersoon;

/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject DbPersoon
 *
 */
public class DbPersoonDao implements ICRUD {
	
	private static final Logger LOGGER = Logger.getLogger(DbPersoonDao.class);
	private final String TAG = "DbPersoonDao: ";
	
	
	public DbPersoonDao(){
	}
	
	@Override
	public int voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		int id = Integer.MIN_VALUE;
		Transaction transaction = null;
		
		try {
			DbPersoon persoon = (DbPersoon) o;
			persoon = this.tel30JaarBijGeboortedatum(persoon);
			session.beginTransaction();
			transaction = session.getTransaction();
			session.save(persoon);
			id = persoon.getId();
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
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
		DbPersoon persoon = new DbPersoon();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbPersoon where id = :id";
		List<DbPersoon> lijst = new ArrayList<DbPersoon>();
		
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
			persoon = lijst.get(0);
		}
		persoon = this.trek30AfBijGeboortedatum(persoon);
		return persoon;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbPersoon> lijst = new ArrayList<DbPersoon>();
		String query = "FROM DbPersoon order by naam";
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
		
		Iterator<DbPersoon> it = lijst.iterator();
		while(it.hasNext()){
			DbPersoon p = it.next();
			p = this.trek30AfBijGeboortedatum(p);
		}
		
		List<Object> objectLijst = new ArrayList<Object>(lijst);
		
		return objectLijst;
	}

	@Override
	public void wijzig(Object o) {
		DbPersoon persoon = (DbPersoon) o;
		persoon = this.tel30JaarBijGeboortedatum(persoon);
		
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			session.saveOrUpdate(persoon);
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
		persoon = this.trek30AfBijGeboortedatum(persoon);
	}

	@Override
	public void verwijder(int id) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbPersoon where id = :id";
		
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
	 * @return List<DbPersoon>
	 * 
	 * return een List<DbPersoon> met enkel DbPersonen met een id die
	 * voorkomt in de idLijst 
	 */
	public List<DbPersoon> lees(int[] idLijst) {
		List<DbPersoon> persoonLijst = new ArrayList<DbPersoon>();
		String query = "FROM DbPersoon where id = :id";
		
		for (int i = 0; i < idLijst.length; i++) {
			Session session = HibernateUtil.openSession();
			Transaction transaction = null;
			transaction = session.getTransaction();
			List<DbPersoon> lijst = new ArrayList<DbPersoon>();
			
			try {
				session.beginTransaction();
				transaction = session.getTransaction();
				Query q = session.createQuery(query);
				q.setParameter("id", idLijst[i]);
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
				LOGGER.error("Exception: " + TAG + "lees(idLijst) ", e);
			} finally {
				session.close();
			}
			
			if (!lijst.isEmpty()) {
				DbPersoon persoon = lijst.get(0);
				persoon = this.trek30AfBijGeboortedatum(persoon);
				persoonLijst.add(persoon);
			}
		}
		
		return persoonLijst;
	}

	/**
	 * @param dbpersoon DbPersoon
	 * @return int
	 * 
	 * return de id van dit DbPersoon
	 * Als deze record niet bestaat, return Integer.min_value
	 */
	public int haalId(DbPersoon dbPersoon) {
		dbPersoon = this.tel30JaarBijGeboortedatum(dbPersoon);
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT id FROM DbPersoon where naam = :naam "
				+ "AND voornaam = :voornaam "
				+ "AND geboortedatum = :geboortedatum";
		List<Integer> lijst = new ArrayList<Integer>();
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("naam", dbPersoon.getNaam());
			q.setParameter("voornaam", dbPersoon.getVoornaam());
			q.setParameter("geboortedatum", dbPersoon.getGeboortedatum());
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
			id = lijst.get(0);
		}

		return id;
	}

	/**
	 * @param Persoon DbPersoon
	 * @return DbPersoon
	 * 
	 * De databank kan niet overweg met een timestamp vroeger dan 1970.
	 * Met deze reden wordt er 30jaar bijgetelt aan de geboortedatum
	 */
	private DbPersoon tel30JaarBijGeboortedatum(DbPersoon persoon){
		Date geboorteDatum = persoon.getGeboortedatum();
		geboorteDatum.setYear(geboorteDatum.getYear()+30);
		persoon.setGeboortedatum(geboorteDatum);
		return persoon;
	}
	
	/**
	 * @param Persoon DbPersoon
	 * @return DbPersoon
	 * 
	 * De databank kan niet overweg met een timestamp vroeger dan 1970.
	 * Met deze reden wordt de geboortedatum opgeslaan met 30jaar erbij getelt.
	 * In deze metode wordt er terug 30 afgetrokken zodat de originele 
	 * geboortedatum terug verschijnt.
	 */
	private DbPersoon trek30AfBijGeboortedatum(DbPersoon persoon){
		Date geboorteDatum = persoon.getGeboortedatum();
		geboorteDatum.setYear(geboorteDatum.getYear()-30);
		persoon.setGeboortedatum(geboorteDatum);
		return persoon;
	}
}

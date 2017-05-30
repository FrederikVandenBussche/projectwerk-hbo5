package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbAdres;

/**
 * @author Frederik Vanden Bussche
 * 
 * Dao voor het databankobject Dbadres
 *
 */
public class DbAdresDao implements ICRUD {
	
	private static final Logger LOGGER = Logger.getLogger(DbAdresDao.class);
	private final String TAG = "DbAdresDao: ";
	
	
	public DbAdresDao(){
	}

	@Override
	public int voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		int id = Integer.MIN_VALUE;
		Transaction transaction = null;
		
		try {
			DbAdres adres = (DbAdres) o;
			session.beginTransaction();
			transaction = session.getTransaction();
			session.save(adres);
			id = adres.getId();
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

		DbAdres adres = new DbAdres();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbAdres where id = :id";
		List<DbAdres> lijst = new ArrayList<DbAdres>();
		
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
			adres = lijst.get(0);
		}

		return adres;
	}

	@Override
	public List<Object> leesAlle() {
		return null;
	}

	@Override
	public void wijzig(Object o) {
	}

	@Override
	public void verwijder(int id) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbAdres where id = :id";
		
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
	 * @param straatId int
	 * @return boolean
	 * 
	 * return true indien de straatId voorkomt in DbAdres-tabel
	 */
	public boolean isStraatInGebruik(int straatId) {
		boolean isInGebruik = true;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbAdres where straatId = :straatId";
		List<DbAdres> lijst = new ArrayList<DbAdres>();
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("straatId", straatId);
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
			LOGGER.error("Exception: " + TAG + "isStraatInGebruik(straatId) " + straatId + " ", e);
		} finally {
			session.close();
		}
		
		if (lijst.isEmpty()) {
			isInGebruik = false;
		}

		return isInGebruik;
	}

	/**
	 * @param gemeenteId int
	 * @return boolean
	 * 
	 * return true indien de gemeenteId voorkomt in DbAdres-tabel
	 */
	public boolean isGemeenteInGebruik(int gemeenteId) {
		boolean isInGebruik = true;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbAdres where gemeenteId = :gemeenteId";
		List<DbAdres> lijst = new ArrayList<DbAdres>();
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("gemeenteId", gemeenteId);
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
			LOGGER.error("Exception: " + TAG + "isGemeenteInGebruik(gemeenteId) " + gemeenteId + " ", e);
		} finally {
			session.close();
		}
		
		if (lijst.isEmpty()) {
			isInGebruik = false;
		}

		return isInGebruik;
	}

	/**
	 * @param adres DbAdres
	 * @return int
	 * 
	 * indien dit adres in de databank aanwezig is:
	 * return de id van dit adres
	 * 
	 * indien niet: return Integer.min_value
	 */
	public int geefIdVan(DbAdres adres) {
		
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT id FROM DbAdres where straatId = :straatId "
										+ "AND gemeenteId = :gemeenteId "
										+ "AND huisnummer = :huisnummer "
										+ "AND bus = :bus";
		List<Integer> lijst = new ArrayList<Integer>();
		
		try {
			session.beginTransaction();
			transaction = session.getTransaction();
			Query q = session.createQuery(query);
			q.setParameter("straatId", adres.getStraatId());
			q.setParameter("gemeenteId", adres.getGemeenteId());
			q.setParameter("huisnummer", adres.getHuisnummer());
			q.setParameter("bus", adres.getBus());
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
			LOGGER.error("Exception: " + TAG + 
					"geefIdVan(adres)", e);
		} finally {
			session.close();
		}
		
		if (!lijst.isEmpty()) {
			id = lijst.get(0);
		}

		return id;
	}
	
	
}

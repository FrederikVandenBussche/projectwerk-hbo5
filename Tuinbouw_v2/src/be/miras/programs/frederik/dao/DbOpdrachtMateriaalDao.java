package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbOpdrachtMateriaal;

public class DbOpdrachtMateriaalDao implements ICRUD {
	private static final Logger LOGGER = Logger.getLogger(DbOpdrachtMateriaalDao.class);

	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try{
			DbOpdrachtMateriaal opdrachtMateriaal = (DbOpdrachtMateriaal)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(opdrachtMateriaal);
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			isGelukt = false;
			e.printStackTrace();
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}	
		return isGelukt;
	}

	@Override
	public Object lees(int id) {
		DbOpdrachtMateriaal opdrachtMateriaal = new DbOpdrachtMateriaal();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbOpdrachtMateriaal where id = :id";
		List<DbOpdrachtMateriaal> lijst = new ArrayList<DbOpdrachtMateriaal>();
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
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			opdrachtMateriaal = lijst.get(0);
		}
		
		return opdrachtMateriaal;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbOpdrachtMateriaal> lijst = new ArrayList<DbOpdrachtMateriaal>();
		String query = "FROM DbOpdrachtMateriaal"; 
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
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}
		
		List<Object> objectLijst = new ArrayList<Object>(lijst);
		return objectLijst;
	}

	@Override
	public boolean wijzig(Object o) {

		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try {
			DbOpdrachtMateriaal opdrachtMateriaal = (DbOpdrachtMateriaal)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(opdrachtMateriaal);
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			isGelukt = false;
			e.printStackTrace();
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}
		return isGelukt;
	}

	@Override
	public boolean verwijder(int id) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		String query = "DELETE FROM DbOpdrachtMateriaal where id = :id";
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
			isGelukt = false;
			e.printStackTrace();
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}
		return isGelukt;
	}

	public List<DbOpdrachtMateriaal> leesWaarOpdrachtId(int opdrachtId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbOpdrachtMateriaal where opdrachtId = :opdrachtId";
		List<DbOpdrachtMateriaal> lijst = new ArrayList<DbOpdrachtMateriaal>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
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
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}
		
		return lijst;
	}

	public void verwijderWaarOpdrachtId(int opdrachtId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbOpdrachtMateriaal where opdrachtId = :opdrachtId";
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
			LOGGER.error("Exception: ", e);
		} finally {
			session.close();
		}
		
	}

}

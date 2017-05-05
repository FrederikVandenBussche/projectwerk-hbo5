package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbBevoegdheid;

public class DbBevoegdheidDao implements ICRUD {
	private static final Logger LOGGER = Logger.getLogger(DbBevoegdheidDao.class);

	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try{
			DbBevoegdheid bevoegdheid = (DbBevoegdheid)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(bevoegdheid);
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
		DbBevoegdheid bevoegdheid = new DbBevoegdheid();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbBevoegdheid where id = :id";
		List<DbBevoegdheid> lijst = new ArrayList<DbBevoegdheid>();
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
			bevoegdheid = lijst.get(0);
		}
		
		return bevoegdheid;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbBevoegdheid> lijst = new ArrayList<DbBevoegdheid>();
		String query = "FROM DbBevoegdheid"; 
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
			DbBevoegdheid bevoegdheid  = (DbBevoegdheid)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(bevoegdheid);
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
		String query = "DELETE FROM DbBevoegdheid where id = :id";
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

}

package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbVooruitgang;

public class DbVooruitgangDao implements ICRUD {
	private static final Logger LOGGER = Logger.getLogger(DbVooruitgangDao.class);
	
	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try {
			DbVooruitgang vooruitgang = (DbVooruitgang) o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(vooruitgang);
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
	public Object lees(int id) {
		DbVooruitgang vooruitgang = new DbVooruitgang();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbVooruitgang where id = :id";
		List<DbVooruitgang> lijst = new ArrayList<DbVooruitgang>();
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
			vooruitgang = lijst.get(0);
		}

		return vooruitgang;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbVooruitgang> lijst = new ArrayList<DbVooruitgang>();
		String query = "FROM DbVooruitgang"; // SELECT * FROM werknemer
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
			DbVooruitgang vooruitgang = (DbVooruitgang) o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(vooruitgang);
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
		String query = "DELETE FROM DbVooruitgang where id = :id";
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

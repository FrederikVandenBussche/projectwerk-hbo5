package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbPersoon;
import be.miras.programs.frederik.dbo.DbTypeMateriaal;


public class DbTypeMateriaalDao implements ICRUD {
	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try{
			DbTypeMateriaal typeMateriaal = (DbTypeMateriaal)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(typeMateriaal);
			session.getTransaction().commit();
		} catch (Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			isGelukt = false;
			e.printStackTrace();
		} finally {
			session.close();
		}	
		return isGelukt;
	}

	@Override
	public Object lees(int id) {
		DbTypeMateriaal typeMateriaal = new DbTypeMateriaal();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbTypeMateriaal where id = :id";
		List<DbTypeMateriaal> lijst = new ArrayList<DbTypeMateriaal>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", id);
			lijst = q.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();

		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			typeMateriaal = lijst.get(0);
		}
		
		return typeMateriaal;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbTypeMateriaal> lijst = new ArrayList<DbTypeMateriaal>();
		String query = "FROM DbTypeMateriaal"; // SELECT * FROM DbTypeMateriaal
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			lijst = q.list();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
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
			DbTypeMateriaal typeMateriaal = (DbTypeMateriaal)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(typeMateriaal);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			isGelukt = false;
			e.printStackTrace();
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
		String query = "DELETE FROM DbTypeMateriaal where id = :id";
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", id);
			q.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			isGelukt = false;
			e.printStackTrace();
		} finally {
			session.close();
		}
		return isGelukt;
	}

	public int lees(String naamType) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT id FROM DbTypeMateriaal where naam = :naam";
		List<Integer> lijst = new ArrayList<Integer>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("naam", naamType);
			lijst = q.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();

		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			id = lijst.get(0);
		}
		
		return id;
	}
	
	public int zoekMaxId() {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT MAX(id) FROM DbTypeMateriaal";
		List<Integer> lijst = new ArrayList<Integer>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			lijst = q.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();

		} finally {
			session.close();
		}
		int id = 0;
		if (!lijst.isEmpty()) {
			id = lijst.get(0);
		}
		return id;
	}
}

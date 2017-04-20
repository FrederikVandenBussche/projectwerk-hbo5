package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbPersoon;

public class DbPersoonDao implements ICRUD {

	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try {
			DbPersoon persoon = (DbPersoon) o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(persoon);
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
	public Object lees(int id) {
		DbPersoon persoon = new DbPersoon();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbPersoon where id = :id";
		List<DbPersoon> lijst = new ArrayList<DbPersoon>();
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
			persoon = lijst.get(0);
		}

		return persoon;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbPersoon> lijst = new ArrayList<DbPersoon>();
		String query = "FROM DbPersoon";
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
			DbPersoon persoon = (DbPersoon) o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(persoon);
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
		String query = "DELETE FROM DbPersoon where id = :id";
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
				Query q = null;

				q = session.createQuery(query);
				q.setParameter("id", idLijst[i]);
				lijst = q.list();
				if (!lijst.isEmpty()) {
					DbPersoon persoon = lijst.get(0);
					persoonLijst.add(persoon);
				}

			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
			} finally {
				session.close();
			}
		}
		
		return persoonLijst;
	}

	public int zoekMaxId() {
		DbPersoon persoon = new DbPersoon();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT MAX(id) FROM DbPersoon";
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

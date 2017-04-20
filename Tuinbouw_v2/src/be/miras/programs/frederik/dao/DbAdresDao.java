package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbAdres;

public class DbAdresDao implements ICRUD {

	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try {
			DbAdres adres = (DbAdres) o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(adres);
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

		DbAdres adres = new DbAdres();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbAdres where id = :id";
		List<DbAdres> lijst = new ArrayList<DbAdres>();
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
			adres = lijst.get(0);
		}

		return adres;
	}

	@Override
	public List<Object> leesAlle() {
		return null;
	}

	@Override
	public boolean wijzig(Object o) {
		return false;
	}

	@Override
	public boolean verwijder(int id) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		String query = "DELETE FROM DbAdres where id = :id";
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

	public int zoekMaxId() {
		DbAdres dbAdres = new DbAdres();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT MAX(id) FROM DbAdres";
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

	public boolean isStraatInGebruik(int straatId) {
		boolean isInGebruik = true;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbAdres where straatId = :straatId";
		List<DbAdres> lijst = new ArrayList<DbAdres>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("straatId", straatId);
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
		if (lijst.isEmpty()) {
			isInGebruik = false;
		}

		return isInGebruik;
	}

	public boolean isGemeenteInGebruik(int gemeenteId) {
		boolean isInGebruik = true;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbAdres where gemeenteId = :gemeenteId";
		List<DbAdres> lijst = new ArrayList<DbAdres>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("gemeenteId", gemeenteId);
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
		if (lijst.isEmpty()) {
			isInGebruik = false;
		}

		return isInGebruik;
	}

}

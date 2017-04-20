package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbAdres;
import be.miras.programs.frederik.dbo.DbBedrijf;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbParticulier;

public class DbKlantDao implements ICRUD {
	
	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try{
			DbKlant klant = (DbKlant)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(klant);
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
		//DbKlant klant = new DbKlant();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbKlant where id = :id";
		List<DbKlant> lijst = new ArrayList<DbKlant>();
		DbKlant dbKlant = null;
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
			DbKlant klant = (DbKlant)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(klant);
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
		String query = "DELETE FROM DbKlant where id = :id";
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

	public Object leesAlleParticulier() {
		List<DbParticulier> lijst = new ArrayList<DbParticulier>();
		String query = "FROM DbParticulier";
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

	public Object leesAlleBedrijf() {
		List<DbBedrijf> lijst = new ArrayList<DbBedrijf>();
		String query = "FROM DbBedrijf"; 
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

	public void voegToePartiulier(DbParticulier klant) {
		voegToe(klant);
		
	}

	public void voegToeBedrijf(DbBedrijf klant) {
		// TODO Auto-generated method stub
		
	}

	public void wijzigParticulier(DbParticulier klant) {
		// TODO Auto-generated method stub
		
	}

	public void wijzigBedrijf(DbBedrijf klant) {
		// TODO Auto-generated method stub
		
	}

	public int zoekMakId() {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT MAX(id) FROM DbKlant";
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

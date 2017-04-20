package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbGemeente;

public class DbGemeenteDao implements ICRUD {
	private String TAG = "DbGemeenteDao :";

	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try{
			DbGemeente gemeente = (DbGemeente)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(gemeente);
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
		DbGemeente gemeente = new DbGemeente();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbGemeente where id = :id";
		List<DbGemeente> lijst = new ArrayList<DbGemeente>();
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
			gemeente = lijst.get(0);
		}
		
		return gemeente;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbGemeente> lijst = new ArrayList<DbGemeente>();
		String query = "FROM DbGemeente"; 
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
			DbGemeente gemeente = (DbGemeente)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(gemeente);
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
		String query = "DELETE FROM DbGemeente where id = :id";
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

	public int geefIdVan(int postcode, String plaats) {
		System.out.println(TAG + postcode + " " + plaats);
		int id = Integer.MIN_VALUE;
		List<DbGemeente> lijst = new ArrayList<DbGemeente>();
		String query = "FROM DbGemeente where postcode = :postcode";
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("postcode", postcode);
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
		
		if (lijst.size() > 0){
			//postcode is gevonden
			plaats = plaats.toLowerCase();
			Iterator<DbGemeente> it = lijst.iterator();
			
			while(it.hasNext()){
				DbGemeente gemeente = it.next();
				String gemeenteNaam = gemeente.getNaam().toLowerCase();
				if (gemeenteNaam.equals(plaats)){
					// gemeente gevonden
					id = gemeente.getId();
				} // else {gemeente is niet gevonden}
				// id = Integer.min_value
			}
			
		} // else {postcode is niet gevonden}
		/*
		 * indien de postcode niet gevonden werd, is
		 * de id : Integer.min_value
		 */
		
		return id;
	}

}

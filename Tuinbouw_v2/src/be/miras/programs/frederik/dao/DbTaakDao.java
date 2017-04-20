package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbTaak;


public class DbTaakDao implements ICRUD {

	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try{
			DbTaak taak = (DbTaak)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(taak);
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
		DbTaak taak = new DbTaak();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbTaak where id = :id";
		List<DbTaak> lijst = new ArrayList<DbTaak>();
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
			taak = lijst.get(0);
		}
		
		return taak;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbTaak> lijst = new ArrayList<DbTaak>();
		String query = "FROM DbTaak"; 
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
			DbTaak taak = (DbTaak)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(taak);
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
		String query = "DELETE FROM DbTaak where id = :id";
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

	public int geefIdVan(String naam, int visible){
		int id = Integer.MIN_VALUE;
		
		List<DbTaak> lijst = new ArrayList<DbTaak>();
		String query = "FROM DbTaak WHERE naam = :naam";
		
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("naam", naam);
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
			// er is een taak gevonden met dezelfde naam
			Iterator<DbTaak> it = lijst.iterator();
			while(it.hasNext()){
				DbTaak dbTaak = it.next();
				id = dbTaak.getId();
			}
		}
		return id;
	}

	public String selectNaam(int taakId) {

		String naam = null;
		List<String> lijst = new ArrayList<String>();
		String query = "SELECT naam FROM DbTaak where id = :id"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", taakId);
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
		if (!lijst.isEmpty()){
			naam = lijst.get(0);
		}
		
		return naam;
	}

}

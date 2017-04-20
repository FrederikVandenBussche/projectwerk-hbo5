package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbStraat;


public class DbStraatDao implements ICRUD {
	
	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try{
			DbStraat straat = (DbStraat)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(straat);
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
		DbStraat straat = new DbStraat();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbStraat where id = :id";
		List<DbStraat> lijst = new ArrayList<DbStraat>();
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
			straat = lijst.get(0);
		}
		
		return straat;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbStraat> lijst = new ArrayList<DbStraat>();
		String query = "FROM DbStraat";
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
			DbStraat straat = (DbStraat)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(straat);
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
		String query = "DELETE FROM DbStraat where id = :id";
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

	public int geefIdVan(String straat) {
		
		int id = Integer.MIN_VALUE;
		List<DbStraat> lijst = new ArrayList<DbStraat>();
		
		String query = "FROM DbStraat where naam = :naam"; 
		
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("naam", straat);
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
			DbStraat dbStraat = lijst.get(0);
			id = dbStraat.getId();
		} else {
			// deze straatnaam staat nog niet in de databank
		}
		
		
		
		return id;
	}
}

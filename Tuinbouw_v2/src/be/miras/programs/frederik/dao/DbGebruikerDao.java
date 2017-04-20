package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbGebruiker;

public class DbGebruikerDao implements ICRUD {

	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try{
			DbGebruiker gebruiker = (DbGebruiker)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(gebruiker);
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
		DbGebruiker gebruiker = new DbGebruiker();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbGebruiker where id = :id";
		List<DbGebruiker> lijst = new ArrayList<DbGebruiker>();
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
			gebruiker = lijst.get(0);
		}
		
		return gebruiker;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbGebruiker> lijst = new ArrayList<DbGebruiker>();
		String query = "FROM DbGebruiker"; 
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
			DbGebruiker gebruiker = (DbGebruiker) o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(gebruiker);
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
		String query = "DELETE FROM DbGebruiker where id = :id";
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

	public List<DbGebruiker> lees(int[] idLijst){
		List<DbGebruiker> gebruikerLijst = new ArrayList<DbGebruiker>();
		
		String query = "FROM DbGebruiker where id = :id";
		
		for (int i = 0; i < idLijst.length; i++){
			Session session = HibernateUtil.openSession();
			Transaction transaction = null;
			transaction = session.getTransaction();
			List<DbGebruiker> lijst = new ArrayList<DbGebruiker>();
			try{
				session.beginTransaction();
				Query q = null;
				
				q = session.createQuery(query);
				q.setParameter("id", idLijst[i]);
				lijst = q.list();
				if(!lijst.isEmpty()){
					DbGebruiker gebruiker = lijst.get(0);
					gebruikerLijst.add(gebruiker);
				}
			} catch (Exception e){
				if (transaction != null){
					transaction.rollback();
				}
				e.printStackTrace();
			} finally {
				session.close();
			}
		}
		
		return gebruikerLijst;
	}

	public String leesWachtwoord(int id) {
		String wachtwoord = null;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT wachtwoord FROM DbGebruiker where id = :id";
		List<String> lijst = new ArrayList<String>();
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
			wachtwoord = lijst.get(0);
		}
		return wachtwoord;
	}

	public void wijzigWachtwoord(int id, String wachtwoord) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "UPDATE DbGebruiker SET wachtwoord = :wachtwoord where id = :id";
		List<DbGebruiker> lijst = new ArrayList<DbGebruiker>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("wachtwoord", wachtwoord);
			q.setParameter("id", id);
			q.executeUpdate();
			session.getTransaction().commit();;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();

		} finally {
			session.close();
		}		
	}

	public DbGebruiker getGebruiker(String gebruikersnaam) {
		DbGebruiker gebruiker = new DbGebruiker();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbGebruiker where gebruikersnaam = :gebruikersnaam";
		List<DbGebruiker> lijst = new ArrayList<DbGebruiker>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("gebruikersnaam", gebruikersnaam);
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
			gebruiker = lijst.get(0);
		}
		
		return gebruiker;
	}
}

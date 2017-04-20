package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbOpdracht;

public class DbOpdrachtDao implements ICRUD {

	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try{
			DbOpdracht opdracht = (DbOpdracht)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(opdracht);
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
		DbOpdracht opdracht = new DbOpdracht();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbOpdracht where id = :id";
		List<DbOpdracht> lijst = new ArrayList<DbOpdracht>();
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
			opdracht = lijst.get(0);
		}
		
		return opdracht;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbOpdracht> lijst = new ArrayList<DbOpdracht>();
		String query = "FROM DbOpdracht"; 
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
			DbOpdracht opdracht = (DbOpdracht)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(opdracht);
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
		String query = "DELETE FROM DbOpdracht where id = :id";
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

	public int geefMaxId() {
		int maxId = Integer.MIN_VALUE;
		List<Integer> lijst = new ArrayList<Integer>();
		String query = "SELECT MAX(id) FROM DbOpdracht"; 
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
		if (!lijst.isEmpty()){
			maxId = lijst.get(0);
		}
		return maxId;
	}

	public String[] selectKlantIdEnNaam(int id) {
		
		String[] returnData = new String[2];
		List<Object[]> lijst = new ArrayList<Object[]>();
		String query = "SELECT klantId, naam FROM DbOpdracht WHERE id = :id"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", id);
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
			Object[]  o = lijst.get(0);
			returnData[0] = String.valueOf((int) o[0]);
			returnData[1] = (String) o[1];			
		}
		
		return returnData;
	}

	public List<DbOpdracht> leesWaarKlantId(int klantId) {
		List<DbOpdracht> lijst = new ArrayList<DbOpdracht>();
		String query = "FROM DbOpdracht WHERE klantId = :klantId"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("klantId", klantId);
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
		

		return lijst;
		
	}

}

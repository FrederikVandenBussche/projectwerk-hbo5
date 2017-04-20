package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dao.HibernateUtil;

public class WachtwoordDaoAdapter {
	
	
	public Object lees(){
		String wachtwoord = null;
		
		Session session = HibernateUtil.openSession();
		Transaction transaction =null;
		String query="SELECT paswoord FROM Inloggegevens where id = 1";
		List<String> lijst = new ArrayList<String>();
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
		if (!lijst.isEmpty()) {
			wachtwoord = lijst.get(0);
		}
		
		return wachtwoord;
	}
	
	public boolean wijzig(String paswoord){
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		String query="UPDATE Inloggegevens "
				+ "SET paswoord = :paswoord "
				+ "WHERE id = 1";
		try {
			
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("paswoord", paswoord);
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

}

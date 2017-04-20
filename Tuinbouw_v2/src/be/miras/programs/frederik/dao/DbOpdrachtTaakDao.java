package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbOpdrachtTaak;

public class DbOpdrachtTaakDao implements ICRUD {
	
	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try{
			DbOpdrachtTaak opdrachtTaak = (DbOpdrachtTaak)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(opdrachtTaak);
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
		DbOpdrachtTaak opdrachtTaak = new DbOpdrachtTaak();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbOpdrachtTaak where id = :id";
		List<DbOpdrachtTaak> lijst = new ArrayList<DbOpdrachtTaak>();
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
			opdrachtTaak = lijst.get(0);
		}
		
		return opdrachtTaak;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbOpdrachtTaak> lijst = new ArrayList<DbOpdrachtTaak>();
		String query = "FROM DbOpdrachtTaak"; 
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
			DbOpdrachtTaak opdrachtTaak = (DbOpdrachtTaak)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(opdrachtTaak);
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
		String query = "DELETE FROM DbOpdrachtTaak where id = :id";
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

	public List<DbOpdrachtTaak> leesLijst(int opdrachtId) {
		List<DbOpdrachtTaak> lijst = new ArrayList<DbOpdrachtTaak>();
		String query = "FROM DbOpdrachtTaak where opdrachtId = :opdrachtId"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("opdrachtId", opdrachtId);
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

	public int leesId(int opdrachtId, int taakId) {
		int id = Integer.MIN_VALUE;
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT id FROM DbOpdrachtTaak WHERE opdrachtId = :opdrachtId"
				+ " AND taakId = :taakId";
		List<Integer> lijst = new ArrayList<Integer>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("opdrachtId", opdrachtId);
			q.setParameter("taakId", taakId);
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

	public void wijzigOpmerking(int opdrachtId, int taakId,  String opmerking) {
		Session session = HibernateUtil.openSession();
		String query = "UPDATE DbOpdrachtTaak SET opmerking = :opmerking "
				+ "WHERE opdrachtId = :opdrachtId"
				+ " AND taakId = :taakId";
		Transaction transaction = null;
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("opmerking", opmerking);
			q.setParameter("opdrachtId", opdrachtId);
			q.setParameter("taakId", taakId);
			q.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		
	}

	public Long hoeveelMetTaakId(int  taakId) {
		Long aantal = Long.MIN_VALUE;
		
		String query = "SELECT COUNT(*) FROM DbOpdrachtTaak "
				+ "where taakId = :taakId";
		
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		List<Long> results = null;
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("taakId", taakId);
			results = q.list();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		if (!results.isEmpty()){
			aantal = results.get(0);
		}
		
		return aantal;
	}

	public void verwijderWaarOpdrachtId(int opdrachtId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbOpdrachtTaak where opdrachtId = :opdrachtId";
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("opdrachtId", opdrachtId);
			q.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public List<Integer> leesVooruitgangIds(int opdrachtId) {
		List<Integer> lijst = new ArrayList<Integer>();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "SELECT vooruitgangId FROM DbOpdrachtTaak WHERE opdrachtId = :opdrachtId";
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("opdrachtId", opdrachtId);
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
		return lijst;
	}

}

package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dao.DbGebruikerDao;
import be.miras.programs.frederik.dao.DbPersoonDao;
import be.miras.programs.frederik.dao.DbWerknemerDao;
import be.miras.programs.frederik.dao.HibernateUtil;
import be.miras.programs.frederik.dao.ICRUD;
import be.miras.programs.frederik.dbo.DbGebruiker;
import be.miras.programs.frederik.dbo.DbPersoon;
import be.miras.programs.frederik.dbo.DbWerknemer;
import be.miras.programs.frederik.model.Personeel;

public class PersoneelDaoAdapter implements ICRUD{
	private String TAG = "PersoneelDaoAdapter: ";
	
	@Override
	public boolean voegToe(Object o) {
		Personeel personeel = (Personeel)o;
		
		DbWerknemerDao dbWerknemerDao = new DbWerknemerDao();
		DbPersoonDao dbPersoonDao = new DbPersoonDao();
		DbGebruikerDao dbGebruikerDao = new DbGebruikerDao();
		DbPersoon dbpersoon = new DbPersoon();
		DbWerknemer dbwerknemer = new DbWerknemer();
		DbGebruiker dbgebruiker = new DbGebruiker();
		
		System.out.println(TAG + "ik zal een persoon toevoegen");
		
		dbpersoon.setId(personeel.getPersoonId());
		dbpersoon.setNaam(personeel.getNaam());
		dbpersoon.setVoornaam(personeel.getVoornaam());
		dbpersoon.setGeboortedatum(personeel.getGeboortedatum());
		dbPersoonDao.voegToe(dbpersoon);

		System.out.println(TAG + "Persoon is toegevoegd" );
		
		
		int nieuweId = dbPersoonDao.zoekMaxId();

		System.out.println(TAG + "De nieuwe id = " + nieuweId);
		personeel.setPersoonId(nieuweId);
		
		
		
		
		dbgebruiker.setId(nieuweId);
		dbgebruiker.setEmail(personeel.getEmail());
		dbgebruiker.setBevoegdheidId(2);
		dbGebruikerDao.voegToe(dbgebruiker);

		System.out.println(TAG + "gebruiker is toegevoegd" );
		
		dbwerknemer.setId(nieuweId);
		dbwerknemer.setLoon(personeel.getLoon());
		dbwerknemer.setAanwervingsdatum(personeel.getAanwervingsdatum());
		dbwerknemer.setPersoonId(nieuweId);
		dbWerknemerDao.voegToe(dbwerknemer);

		System.out.println(TAG + "werknemer is toegevoegd");
		
		
		return true;
	}

	@Override
	public Object lees(int id) {
		Personeel personeel = new Personeel();
		
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM Personeel where id = :id";
		List<Personeel> lijst = new ArrayList<Personeel>();
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
			personeel = lijst.get(0);
		}
		
		return personeel;
	}

	@Override
	public List<Object> leesAlle() {
		
		List<Personeel> personeelLijst = new ArrayList<Personeel>();
		List<DbPersoon> dbPersoonLijst = new ArrayList<DbPersoon>();
		List<DbWerknemer> dbWerknemerLijst = new ArrayList<DbWerknemer>();	
		List<DbGebruiker> dbGebruikerLijst = new ArrayList<DbGebruiker>();
		
		DbPersoonDao dbPersoonDao = new DbPersoonDao();
		DbWerknemerDao dbWerknemerDao = new DbWerknemerDao();
		DbGebruikerDao dbGebruikerDao = new DbGebruikerDao();
		
		dbWerknemerLijst = (List<DbWerknemer>) (Object) dbWerknemerDao.leesAlle();
		
		//dbPersoonDao.lees(int[] idLijst) vereist een int[] als parameter
		int[] idLijst = new int[dbWerknemerLijst.size()];
		int i = 0;
		
		// DbWerknemers toevoegen aan de lijst met personeelsleden.
		Iterator<DbWerknemer> it =  dbWerknemerLijst.iterator();
		while(it.hasNext()){
			DbWerknemer dbwerknemer = it.next();
			
			Personeel personeel = new Personeel();
			personeel.setWerknemerId(dbwerknemer.getId());
			System.out.println(TAG + "(iterator), werknemersId = " + dbwerknemer.getId());
			personeel.setPersoonId(dbwerknemer.getPersoonId());
			personeel.setLoon(dbwerknemer.getLoon());
			personeel.setAanwervingsdatum(dbwerknemer.getAanwervingsdatum());
			personeel.setWerknemerId(dbwerknemer.getId());
			
			personeelLijst.add(personeel);
			
			idLijst[i] = dbwerknemer.getPersoonId();
			i++;
		}
		
		//dbPersoon lijst ophalen met de id's van alle personeelsleden
		dbPersoonLijst = (ArrayList<DbPersoon>) dbPersoonDao.lees(idLijst);
		dbGebruikerLijst = (ArrayList<DbGebruiker>) dbGebruikerDao.lees(idLijst);
		
		// DbPersoon toevoegen aan de lijst met personeelsleden
		// DbGebruiker toevoegen aan de lijst met personeelsleden
		ListIterator<Personeel> personeelIt = personeelLijst.listIterator();
		while(personeelIt.hasNext()){
			Personeel personeel = personeelIt.next();
			
			Iterator<DbPersoon> dbPersoonIt = dbPersoonLijst.iterator();
			while(dbPersoonIt.hasNext()){
				DbPersoon dbpersoon = dbPersoonIt.next();
		
				if(personeel.getPersoonId() == dbpersoon.getId()){
					personeel.setVoornaam(dbpersoon.getVoornaam());
					personeel.setNaam(dbpersoon.getNaam());
					personeel.setGeboortedatum(dbpersoon.getGeboortedatum());
				}
			}
			
			Iterator<DbGebruiker> dbGebruikerIt = dbGebruikerLijst.iterator();
			while(dbGebruikerIt.hasNext()){
				DbGebruiker dbgebruiker = dbGebruikerIt.next();
				
				if(personeel.getPersoonId() == dbgebruiker.getId()){
					
					personeel.setEmail(dbgebruiker.getEmail());
					
				}
				
			}
		}
		

		
		
	
		List<Object> objectLijst = new ArrayList<Object>(personeelLijst);
		return objectLijst;
	}

	@Override
	public boolean wijzig(Object o) {

		Personeel personeel = (Personeel)o;
		
		DbWerknemerDao dbWerknemerDao = new DbWerknemerDao();
		DbPersoonDao dbPersoonDao = new DbPersoonDao();
		DbGebruikerDao dbGebruikerDao = new DbGebruikerDao();
		
		DbWerknemer dbwerknemer = new DbWerknemer();
		DbPersoon dbpersoon = new DbPersoon();
		DbGebruiker dbgebruiker = new DbGebruiker();
		
		System.out.println(TAG + "de personeelsId = " + personeel.getPersoonId());
		System.out.println(TAG + "de werknemersId = " + personeel.getWerknemerId());
		
		dbpersoon.setId(personeel.getPersoonId());
		dbpersoon.setNaam(personeel.getNaam());
		dbpersoon.setVoornaam(personeel.getVoornaam());
		dbpersoon.setGeboortedatum(personeel.getGeboortedatum());
		dbPersoonDao.wijzig(dbpersoon);
		
		System.out.println(TAG + "de persoon is gewijzigd" + dbpersoon.getId());
		
		dbwerknemer.setId(personeel.getWerknemerId()); 
		dbwerknemer.setLoon(personeel.getLoon());
		dbwerknemer.setAanwervingsdatum(personeel.getAanwervingsdatum());
		dbwerknemer.setPersoonId(personeel.getPersoonId());
		dbWerknemerDao.wijzig(dbwerknemer);
		
		System.out.println(TAG + "de werknemer is gewijzigd" + dbwerknemer.getId());
		
		
		
		dbgebruiker.setId(personeel.getPersoonId());
		dbgebruiker.setEmail(personeel.getEmail());
		dbgebruiker.setBevoegdheidId(2);
		dbGebruikerDao.wijzig(dbgebruiker);
		
		System.out.println(TAG + "de gebruiker is gewijzigd" + dbgebruiker.getId());

		return true;
	}

	@Override
	public boolean verwijder(int id) {
		
		// vooraleer het personeelslid te verwijderen eerst alle adressen verwijderen.
		PersoonAdresDaoAdapter adresDaoAdapter = new PersoonAdresDaoAdapter();
		adresDaoAdapter.verwijderVanPersoon(id);
		
		DbWerknemerDao dbWerknemerDao = new DbWerknemerDao();
		DbPersoonDao dbPersoonDao = new DbPersoonDao();
		DbGebruikerDao dbGebruikerDao = new DbGebruikerDao();
			
		dbWerknemerDao.verwijderWaarPersoonId(id);
		
		dbPersoonDao.verwijder(id);
				
		dbGebruikerDao.verwijder(id);
		
		return true;
	}

}

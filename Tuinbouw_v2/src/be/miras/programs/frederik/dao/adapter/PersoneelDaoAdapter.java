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
	//private String TAG = "PersoneelDaoAdapter: ";
	
	@Override
	public boolean voegToe(Object o) {
		Personeel personeel = (Personeel)o;
		
		DbWerknemerDao dbWerknemerDao = new DbWerknemerDao();
		DbPersoonDao dbPersoonDao = new DbPersoonDao();
		DbGebruikerDao dbGebruikerDao = new DbGebruikerDao();
		DbPersoon dbpersoon = new DbPersoon();
		DbWerknemer dbwerknemer = new DbWerknemer();
		DbGebruiker dbgebruiker = new DbGebruiker();
		
		dbpersoon.setId(personeel.getPersoonId());
		dbpersoon.setNaam(personeel.getNaam());
		dbpersoon.setVoornaam(personeel.getVoornaam());
		dbpersoon.setGeboortedatum(personeel.getGeboortedatum());
		dbPersoonDao.voegToe(dbpersoon);

		int persoonId = dbPersoonDao.zoekMaxId();

		personeel.setPersoonId(persoonId);
		
		String gebruikersnaam = creeerGebruikersnaam(personeel, dbGebruikerDao);
		
		dbgebruiker.setPersoonId(persoonId);
		dbgebruiker.setEmail(personeel.getEmail());
		dbgebruiker.setBevoegdheidId(2);
		dbgebruiker.setGebruikersnaam(gebruikersnaam);
		
		dbGebruikerDao.voegToe(dbgebruiker);

		dbwerknemer.setLoon(personeel.getLoon());
		dbwerknemer.setAanwervingsdatum(personeel.getAanwervingsdatum());
		dbwerknemer.setPersoonId(persoonId);
		dbWerknemerDao.voegToe(dbwerknemer);

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
			personeel.setPersoonId(dbwerknemer.getPersoonId());
			personeel.setLoon(dbwerknemer.getLoon());
			personeel.setAanwervingsdatum(dbwerknemer.getAanwervingsdatum());
			
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
				
				if(personeel.getPersoonId() == dbgebruiker.getPersoonId()){
					
					personeel.setEmail(dbgebruiker.getEmail());
					personeel.setGebruikerId(dbgebruiker.getId());	
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
		
		DbPersoon oudeDbPersoon = (DbPersoon) dbPersoonDao.lees(personeel.getPersoonId());
		
		dbpersoon.setId(personeel.getPersoonId());
		dbpersoon.setNaam(personeel.getNaam());
		dbpersoon.setVoornaam(personeel.getVoornaam());
		dbpersoon.setGeboortedatum(personeel.getGeboortedatum());
		dbPersoonDao.wijzig(dbpersoon);
			
		dbwerknemer.setId(personeel.getWerknemerId()); 
		dbwerknemer.setLoon(personeel.getLoon());
		dbwerknemer.setAanwervingsdatum(personeel.getAanwervingsdatum());
		dbwerknemer.setPersoonId(personeel.getPersoonId());
		dbWerknemerDao.wijzig(dbwerknemer);
				
		if (!oudeDbPersoon.getVoornaam().equals(personeel.getVoornaam()) ||
				!oudeDbPersoon.getNaam().equals(personeel.getNaam())){
			String gebruikersnaam = creeerGebruikersnaam(personeel, dbGebruikerDao);
			dbgebruiker.setGebruikersnaam(gebruikersnaam);
		}
		
		dbgebruiker.setId(personeel.getGebruikerId());
		dbgebruiker.setEmail(personeel.getEmail());
		dbgebruiker.setBevoegdheidId(2);
		dbgebruiker.setPersoonId(personeel.getPersoonId());
		
		dbGebruikerDao.wijzig(dbgebruiker);
		
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
				
		dbGebruikerDao.verwijderWaarPersoonId(id);
		
		return true;
	}

	private String creeerGebruikersnaam(Personeel personeel, DbGebruikerDao dbGebruikerDao) {
		String gebruikersnaam = personeel.getVoornaam().concat(".").concat(personeel.getNaam());
		int aantalKeer = dbGebruikerDao.aantalMetGebruikersnaam(gebruikersnaam);

		int nr = 1;
		while (aantalKeer > 0){
			gebruikersnaam = gebruikersnaam.concat(String.valueOf(nr));
			nr++;
			aantalKeer = dbGebruikerDao.aantalMetGebruikersnaam(gebruikersnaam);
		}
		return gebruikersnaam;
	}
	
	
}

package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import be.miras.programs.frederik.dao.DbGebruikerDao;
import be.miras.programs.frederik.dao.DbPersoonDao;
import be.miras.programs.frederik.dao.DbWerknemerDao;
import be.miras.programs.frederik.dao.ICRUD;
import be.miras.programs.frederik.dbo.DbGebruiker;
import be.miras.programs.frederik.dbo.DbPersoon;
import be.miras.programs.frederik.dbo.DbWerknemer;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Personeel;
import be.miras.programs.frederik.util.GoogleApis;

/**
  * @author Frederik Vanden Bussche
 * 
 * Adapter die het model Personeel
 * koppelt aan de databankobjecten : DbWerknemer, DbPersoon, DbGebruiker
 *
 */
public class PersoneelDaoAdapter implements ICRUD{
	
	
	public PersoneelDaoAdapter(){
	}
	
	@Override
	public int voegToe(Object o) {
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
		int persoonId = dbPersoonDao.voegToe(dbpersoon);

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

		return persoonId;
	}

	@Override
	public Object lees(int id) {
		
		Personeel personeel = new Personeel();
		DbPersoon dbPersoon = new DbPersoon();
		DbWerknemer dbWerknemer = new DbWerknemer();	
		DbGebruiker dbGebruiker = new DbGebruiker();
		DbPersoonDao dbPersoonDao = new DbPersoonDao();
		DbWerknemerDao dbWerknemerDao = new DbWerknemerDao();
		DbGebruikerDao dbGebruikerDao = new DbGebruikerDao();
		
		dbWerknemer = dbWerknemerDao.leesWaarPersoonId(id);
		dbPersoon = (DbPersoon) dbPersoonDao.lees(id);
		dbGebruiker = (DbGebruiker) dbGebruikerDao.leesWaarPersoonId(id);
		
		AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();
		List<Adres> adresLijst = adresDaoAdapter.leesWaarPersoonId(id);
		
		// de googleApis van dit personeelslid ophalen
		ListIterator<Adres> adresLijstIt = adresLijst.listIterator();
		while (adresLijstIt.hasNext()) {
			Adres adres = adresLijstIt.next();
			String staticmap = GoogleApis.urlBuilderStaticMap(adres);
			adres.setStaticmap(staticmap);

			String googlemap = GoogleApis.urlBuilderGoogleMaps(adres);
			adres.setGooglemap(googlemap);
		}
		
		personeel.setPersoonId(dbPersoon.getId());
		personeel.setWerknemerId(dbWerknemer.getId());
		personeel.setGebruikerId(dbGebruiker.getId());
		personeel.setVoornaam(dbPersoon.getVoornaam());
		personeel.setNaam(dbPersoon.getNaam());
		personeel.setGeboortedatum(dbPersoon.getGeboortedatum());
		personeel.setAanwervingsdatum(dbWerknemer.getAanwervingsdatum());
		personeel.setLoon(dbWerknemer.getLoon());
		personeel.setEmail(dbGebruiker.getEmail());
		personeel.setAdreslijst((ArrayList<Adres>) adresLijst);
		
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
	public void wijzig(Object o) {

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
	}

	@Override
	public void verwijder(int id) {
		
		// vooraleer het personeelslid te verwijderen eerst alle adressen verwijderen.
		PersoonAdresDaoAdapter adresDaoAdapter = new PersoonAdresDaoAdapter();
		adresDaoAdapter.verwijderVanPersoon(id);
		DbWerknemerDao dbWerknemerDao = new DbWerknemerDao();
		DbPersoonDao dbPersoonDao = new DbPersoonDao();
		DbGebruikerDao dbGebruikerDao = new DbGebruikerDao();
			
		dbWerknemerDao.verwijderWaarPersoonId(id);
		dbGebruikerDao.verwijderWaarPersoonId(id);
		dbPersoonDao.verwijder(id);
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

	/**
	 * @param personeel Personeel
	 * @return int
	 * 
	 * Controle of de persoon met zelfde naam, voornaam en geboortedatum
	 * in de databank zit.
	 * return de id van dit DbPersoon 
	 * Als deze record niet bestaat: return Integer.min_value
	 */
	public int haalId(Personeel personeel) {
		DbPersoonDao dbPersoonDao = new DbPersoonDao();
		DbPersoon dbPersoon = new DbPersoon();
		
		dbPersoon.setNaam(personeel.getNaam());
		dbPersoon.setVoornaam(personeel.getVoornaam());
		dbPersoon.setGeboortedatum(personeel.getGeboortedatum());
		
		int persoonId = dbPersoonDao.haalId(dbPersoon);
		return persoonId;
	}
	
	
}

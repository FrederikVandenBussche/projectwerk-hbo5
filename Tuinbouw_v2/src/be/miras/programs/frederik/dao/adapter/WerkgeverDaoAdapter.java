package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import be.miras.programs.frederik.dao.DbGebruikerDao;
import be.miras.programs.frederik.dao.DbPersoonDao;
import be.miras.programs.frederik.dao.DbWerkgeverDao;
import be.miras.programs.frederik.dao.ICRUD;
import be.miras.programs.frederik.dbo.DbGebruiker;
import be.miras.programs.frederik.dbo.DbPersoon;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Werkgever;
import be.miras.programs.frederik.util.GoogleApis;

public class WerkgeverDaoAdapter implements ICRUD {

	@Override
	public boolean voegToe(Object o) {
		return false;
	}

	@Override
	public Object lees(int id) {
		Werkgever werkgever = new Werkgever();
		DbWerkgeverDao dbWerkgeverDao = new DbWerkgeverDao();
		DbPersoonDao dbPersoonDao = new DbPersoonDao();
		DbGebruikerDao dbGebruikerDao = new DbGebruikerDao();
		List<Adres> adresLijst = new ArrayList<Adres>();
		PersoonAdresDaoAdapter adresDaoAdapter = new PersoonAdresDaoAdapter();
		
		//de id van de werkgever
		int werkgeverId = dbWerkgeverDao.geefId();
		
		DbPersoon dbPersoon = (DbPersoon) dbPersoonDao.lees(werkgeverId);
		DbGebruiker dbGebruiker = (DbGebruiker) dbGebruikerDao.lees(werkgeverId);
		
		
		adresLijst = adresDaoAdapter.leesSelectief("persoon", werkgeverId);
		
		werkgever.setId(werkgeverId);
		werkgever.setNaam(dbPersoon.getNaam());
		werkgever.setVoornaam(dbPersoon.getVoornaam());
		werkgever.setGeboortedatum(dbPersoon.getGeboortedatum());
		
		werkgever.setEmail(dbGebruiker.getEmail());
		werkgever.setWachtwoord(dbGebruiker.getWachtwoord());
		werkgever.setGebruikersnaam(dbGebruiker.getGebruikersnaam());
		werkgever.setBevoegdheidID(dbGebruiker.getBevoegdheidId());
		
		ListIterator<Adres> adresLijstIt = adresLijst.listIterator();
		while (adresLijstIt.hasNext()){
			Adres adres = adresLijstIt.next();
			
			String staticmap = GoogleApis.urlBuilderStaticMap(adres);
			adres.setStaticmap(staticmap);
			
			String googlemap = GoogleApis.urlBuilderGoogleMaps(adres);
			adres.setGooglemap(googlemap);
		}
		
		werkgever.setAdreslijst((ArrayList<Adres>) adresLijst);
		
		
		
		//maak een werkgever aan en return de werkgever
		return werkgever;
	}

	@Override
	public List<Object> leesAlle() {
		return null;
	}

	@Override
	public boolean wijzig(Object o) {

		return true;
	}

	@Override
	public boolean verwijder(int id) {
		return false;
	}

}

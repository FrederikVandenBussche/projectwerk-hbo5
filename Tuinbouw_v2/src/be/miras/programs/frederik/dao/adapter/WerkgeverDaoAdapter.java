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
import be.miras.programs.frederik.dbo.DbWerkgever;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Werkgever;
import be.miras.programs.frederik.util.GoogleApis;

/**
 * @author Frederik Vanden Bussche
 * 
 * Adapter die het model Werkgever
 * koppelt aan de databankobjecten : DbWerkgever, DbPersoon, DbGebruiker,
 * en het model PersoonAdres
 *
 */
public class WerkgeverDaoAdapter implements ICRUD {

	@Override
	public int voegToe(Object o) {
		return Integer.MIN_VALUE;
	}

	@Override
	public Object lees(int gebruikerId) {
		
		Werkgever werkgever = new Werkgever();
		
		DbWerkgeverDao dbWerkgeverDao = new DbWerkgeverDao();
		DbPersoonDao dbPersoonDao = new DbPersoonDao();
		DbGebruikerDao dbGebruikerDao = new DbGebruikerDao();

		List<Adres> adresLijst = new ArrayList<Adres>();
		
		PersoonAdresDaoAdapter adresDaoAdapter = new PersoonAdresDaoAdapter();
		
		
		DbGebruiker dbGebruiker = (DbGebruiker) dbGebruikerDao.lees(gebruikerId);
		DbPersoon dbPersoon = (DbPersoon) dbPersoonDao.lees(dbGebruiker.getPersoonId());
		DbWerkgever dbWerkgever = dbWerkgeverDao.geefId(dbPersoon.getId());
		
		adresLijst = adresDaoAdapter.leesSelectief("persoon", dbPersoon.getId());
		
		werkgever.setId(dbWerkgever.getId());
		
		werkgever.setPersoonId(dbPersoon.getId());
		werkgever.setNaam(dbPersoon.getNaam());
		werkgever.setVoornaam(dbPersoon.getVoornaam());
		werkgever.setGeboortedatum(dbPersoon.getGeboortedatum());
		
		werkgever.setGebruikerId(dbGebruiker.getId());
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
		
		return werkgever;
	}

	@Override
	public List<Object> leesAlle() {
		return null;
	}

	@Override
	public void wijzig(Object o) {
	}

	@Override
	public void verwijder(int id) {
	}

}

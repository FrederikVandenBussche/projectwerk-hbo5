package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.List;

import be.miras.programs.frederik.dao.DbAdresDao;
import be.miras.programs.frederik.dao.DbGemeenteDao;
import be.miras.programs.frederik.dao.DbKlantAdresDao;
import be.miras.programs.frederik.dao.DbStraatDao;
import be.miras.programs.frederik.dao.ICRUD;
import be.miras.programs.frederik.dbo.DbAdres;
import be.miras.programs.frederik.dbo.DbGemeente;
import be.miras.programs.frederik.dbo.DbStraat;
import be.miras.programs.frederik.model.Adres;

public class AdresAdapter implements ICRUD {

	@Override
	public boolean voegToe(Object o) {
		Adres adres = (Adres) o;

		DbAdresDao dbAdresDao = new DbAdresDao();
		DbGemeenteDao dbGemeenteDao = new DbGemeenteDao();
		DbStraatDao dbStraatDao = new DbStraatDao();
		DbAdres dbAdres = new DbAdres();
		
		// zoekt ID van postcode + gemeente
		// indien de opgegeven postcode + gemeente nog niet in de databank zit
		// voeg toe
		int gemeenteId = dbGemeenteDao.geefIdVan(adres.getPostcode(), adres.getPlaats());
		if (gemeenteId < 0) {
			// gemeente nog niet in databank
			DbGemeente dbGemeente = new DbGemeente();
			dbGemeente.setNaam(adres.getPlaats());
			dbGemeente.setPostcode(adres.getPostcode());
			dbGemeenteDao.voegToe(dbGemeente);

			gemeenteId = dbGemeenteDao.geefIdVan(adres.getPostcode(), adres.getPlaats());
		}

		// zoek ID van straat
		// indien de opgegeven straatnaam nog niet in de databank zit
		// voeg toe
		int straatId = dbStraatDao.geefIdVan(adres.getStraat());
		if (straatId < 0) {
			// straatnaam staat nog niet in databank
			DbStraat dbStraat = new DbStraat();
			dbStraat.setNaam(adres.getStraat());
			dbStraatDao.voegToe(dbStraat);

			straatId = dbStraatDao.geefIdVan(adres.getStraat());
		}

		dbAdres.setStraatId(straatId);
		dbAdres.setGemeenteId(gemeenteId);
		dbAdres.setHuisnummer(adres.getNummer());
		dbAdres.setBus(adres.getBus());
		dbAdresDao.voegToe(dbAdres);

		return true;
	}

	@Override
	public Object lees(int id) {
		Adres adres = new Adres();
		
		DbAdresDao dbAdresDao = new DbAdresDao();
		DbGemeenteDao dbGemeenteDao = new DbGemeenteDao();
		DbStraatDao dbStraatDao = new DbStraatDao();
				
		DbAdres dbAdres = (DbAdres) dbAdresDao.lees(id);
		int dbStraatId = dbAdres.getStraatId();
		DbStraat dbStraat = (DbStraat) dbStraatDao.lees(dbStraatId);
		int dbGemeenteId = dbAdres.getGemeenteId();
		DbGemeente dbGemeente = (DbGemeente) dbGemeenteDao.lees(dbGemeenteId);
		
		adres.setStraat(dbStraat.getNaam());
		adres.setNummer(dbAdres.getHuisnummer());
		adres.setBus(dbAdres.getBus());
		adres.setPostcode(dbGemeente.getPostcode());
		adres.setPlaats(dbGemeente.getNaam());
		
		return adres;
	}

	@Override
	public List<Object> leesAlle() {

		return null;
	}

	@Override
	public boolean wijzig(Object o) {

		return false;
	}

	@Override
	public boolean verwijder(int id) {

		// straatId en gemeenteId ophalen
		DbAdresDao dbAdresdao = new DbAdresDao();
		DbAdres dbadres = (DbAdres) dbAdresdao.lees(id);
		int straatId = dbadres.getStraatId();
		int gemeenteId = dbadres.getGemeenteId();

		// delete dbAdres
		dbAdresdao.verwijder(id);
		// indien de straat nergens anders gebruikt wordt.
		// deze uit de db verwijderen
		boolean straatInGebruik = dbAdresdao.isStraatInGebruik(straatId);

		if (!straatInGebruik) {
			DbStraatDao dbStraatDao = new DbStraatDao();
			dbStraatDao.verwijder(straatId);
		}
		// indien de gemeente nergens anders gebruikt wordt
		// deze uit de db verwijderen
		boolean gemeenteInGebruik = dbAdresdao.isGemeenteInGebruik(gemeenteId);

		if (!gemeenteInGebruik) {
			DbGemeenteDao dbGemeenteDao = new DbGemeenteDao();
			dbGemeenteDao.verwijder(gemeenteId);
		}
		return true;
	}

	public int geefMaxId() {
		DbAdresDao dbAdresDao = new DbAdresDao();
		int maxId = dbAdresDao.zoekMaxId();
		return maxId;
	}
	
	public Object leesWaarAdresId(int klantAdresId) {
		Adres adres = new Adres();
		
		DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
		DbAdresDao dbAdresDao = new DbAdresDao();
		DbGemeenteDao dbGemeenteDao = new DbGemeenteDao();
		DbStraatDao dbStraatDao = new DbStraatDao();
		
		int id = dbKlantAdresDao.geefEersteAdresId(klantAdresId);
		
		DbAdres dbAdres = (DbAdres) dbAdresDao.lees(id);
		int dbStraatId = dbAdres.getStraatId();
		DbStraat dbStraat = (DbStraat) dbStraatDao.lees(dbStraatId);
		int dbGemeenteId = dbAdres.getGemeenteId();
		DbGemeente dbGemeente = (DbGemeente) dbGemeenteDao.lees(dbGemeenteId);
		
		adres.setStraat(dbStraat.getNaam());
		adres.setNummer(dbAdres.getHuisnummer());
		adres.setBus(dbAdres.getBus());
		adres.setPostcode(dbGemeente.getPostcode());
		adres.setPlaats(dbGemeente.getNaam());
		
		return adres;
	}
	
	public List<Adres> leesWaarKlantId(int klantId){
		List<Adres> adresLijst = new ArrayList<Adres>();
		
		DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
		DbAdresDao dbAdresDao = new DbAdresDao();
		DbGemeenteDao dbGemeenteDao = new DbGemeenteDao();
		DbStraatDao dbStraatDao = new DbStraatDao();
		
		List<Integer> adresIds = dbKlantAdresDao.leesLijst(klantId);
		for(int id : adresIds){
			Adres a = new Adres();
			DbAdres dbAdres = (DbAdres) dbAdresDao.lees(id);
			DbStraat dbStraat = (DbStraat) dbStraatDao.lees(dbAdres.getStraatId());
			DbGemeente dbGemeente = (DbGemeente) dbGemeenteDao.lees(dbAdres.getGemeenteId());
			
			a.setId(id);
			a.setStraat(dbStraat.getNaam());
			a.setNummer(dbAdres.getHuisnummer());
			a.setBus(dbAdres.getBus());
			a.setPostcode(dbGemeente.getPostcode());
			a.setPlaats(dbGemeente.getNaam());
			
			adresLijst.add(a);
		}
		
		return adresLijst;
	}
	
}

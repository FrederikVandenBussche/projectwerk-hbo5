package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.miras.programs.frederik.dao.DbAdresDao;
import be.miras.programs.frederik.dao.DbGemeenteDao;
import be.miras.programs.frederik.dao.DbKlantAdresDao;
import be.miras.programs.frederik.dao.DbPersoonAdresDao;
import be.miras.programs.frederik.dao.DbStraatDao;
import be.miras.programs.frederik.dao.ICRUD;
import be.miras.programs.frederik.dbo.DbAdres;
import be.miras.programs.frederik.dbo.DbGemeente;
import be.miras.programs.frederik.dbo.DbStraat;
import be.miras.programs.frederik.model.Adres;

/**
 * @author Frederik Vanden Bussche
 * 
 * Adapter die het model Adres
 * koppelt aan de databankobjecten : DbAdres, DbStraat, DBGemeente
 * 
 *
 */
public class AdresDaoAdapter implements ICRUD {
	private DbAdresDao dbAdresDao;
	private DbGemeenteDao dbGemeenteDao;
	private DbStraatDao dbStraatDao;
	


	/**
	 * 
	 */
	public AdresDaoAdapter() {
		super();
		this.dbAdresDao = new DbAdresDao();
		this.dbGemeenteDao = new DbGemeenteDao();
		this.dbStraatDao = new DbStraatDao();
	}

	@Override
	public int voegToe(Object o) {
		Adres adres = (Adres) o;

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

		return Integer.MIN_VALUE;
	}

	/**
	 * 	@param id de id
	 *  @return het adres met de geparameteriseerde adres_id
	 */
	@Override
	public Object lees(int id) {
		Adres adres = new Adres();
				
		DbAdres dbAdres = (DbAdres) dbAdresDao.lees(id);
		int dbStraatId = dbAdres.getStraatId();
		DbStraat dbStraat = (DbStraat) dbStraatDao.lees(dbStraatId);
		int dbGemeenteId = dbAdres.getGemeenteId();
		DbGemeente dbGemeente = (DbGemeente) dbGemeenteDao.lees(dbGemeenteId);
		
		adres.setId(id);
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
	public void wijzig(Object o) {
	}

	@Override
	public void verwijder(int id) {

		// straatId en gemeenteId ophalen
		DbAdres dbadres = (DbAdres) dbAdresDao.lees(id);
		int straatId = dbadres.getStraatId();
		int gemeenteId = dbadres.getGemeenteId();

		// delete dbAdres
		dbAdresDao.verwijder(id);
		// indien de straat nergens anders gebruikt wordt.
		// deze uit de db verwijderen
		boolean straatInGebruik = dbAdresDao.isStraatInGebruik(straatId);

		if (!straatInGebruik) {
			dbStraatDao.verwijder(straatId);
		}
		// indien de gemeente nergens anders gebruikt wordt
		// deze uit de db verwijderen
		boolean gemeenteInGebruik = dbAdresDao.isGemeenteInGebruik(gemeenteId);

		if (!gemeenteInGebruik) {
			dbGemeenteDao.verwijder(gemeenteId);
		}
	}

	/**
	 * @param klantAdresId int
	 * @return Adres
	 * 
	 * returnt het adres met een bepaalde klantAdresId
	 */
	public Adres leesWaarKlantAdresId(int klantAdresId) {
		Adres adres = new Adres();
		
		DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
		
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
	
	/**
	 * @param klantId int
	 * @return List<Adres>
	 * 
	 * returnt List<Adres> van Adres met een bepaalde klantId
	 */
	public List<Adres> leesWaarKlantId(int klantId){
		List<Adres> adresLijst = new ArrayList<Adres>();
		
		DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
		
		List<Integer> adresIds = dbKlantAdresDao.leesLijst(klantId);
		
		Iterator<Integer> it = adresIds.iterator();
		while(it.hasNext()){
			int id = it.next();
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
	
	/**
	 * @param persoonId int
	 * @return List<Adres>
	 * 
	 * returnt List<Adres> van Adres met een bepaalde persoonId
	 */
	public List<Adres> leesWaarPersoonId(int persoonId){
		List<Adres> adresLijst = new ArrayList<Adres>();
		
		DbPersoonAdresDao dbPersoonAdresDao = new DbPersoonAdresDao();
		
		List<Integer> adresIds = dbPersoonAdresDao.leesLijst(persoonId);
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

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
import be.miras.programs.frederik.dbo.DbPersoonAdres;
import be.miras.programs.frederik.model.Adres;

/**
 * @author Frederik Vanden Bussche
 * 
 * Adapter die het model PersoonAdres
 * koppelt aan de databankobjecten : DbPersoonAdres, DbAdres, DbGemeente,
 * DbStraat
 *
 */
public class PersoonAdresDaoAdapter implements ICRUD {
	

	public PersoonAdresDaoAdapter(){
	}
	
	@Override
	public int voegToe(Object o) {
		Adres adres = (Adres) o;
		
		DbPersoonAdresDao dbPersoonAdresDao = new DbPersoonAdresDao();
		DbPersoonAdres dbPersoonAdres = new DbPersoonAdres();
		AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();
		
		int dbAdresId = adresDaoAdapter.voegToe(adres);
		int dbPersoonId = adres.getPersoonId();
		
		dbPersoonAdres.setAdresId(dbAdresId);
		dbPersoonAdres.setPersoonId(dbPersoonId);
		
		// zoek id van dbPersoonAdres
		// indien niet in databank: voeg toe
		int dbPersoonAdresId = dbPersoonAdresDao.geefIdVan(dbPersoonAdres);
		if (dbPersoonAdresId < 0){
			dbPersoonAdresDao.voegToe(dbPersoonAdres);
		}
		
		return dbAdresId;
	}

	@Override
	public Object lees(int id) {
		return null;
	}

	@Override
	public List<Object> leesAlle() {
		return null;
	}

	@Override
	public void wijzig(Object o) {
	}

	@Override
	public void verwijder(int adresId) {
		DbPersoonAdresDao dbPersoonAdresDao = new DbPersoonAdresDao();
		DbPersoonAdres dbPersoonAdres = null;
		
		// persoonId ophalen
		dbPersoonAdres =  (DbPersoonAdres) dbPersoonAdresDao.lees(adresId);
		int persoonId = dbPersoonAdres.getPersoonId();
		
		//persoonAdres verwijderen
		dbPersoonAdresDao.verwijder(persoonId, adresId);
		List<Integer> persoonAdresIdLijst = dbPersoonAdresDao.leespersoonIdLijst(adresId);
		
		// indien er geen personen meer aan dit adres gekoppeld zijn
		if (persoonAdresIdLijst.size() == 0){

			AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();
			adresDaoAdapter.verwijder(adresId);
		}
	}

	/**
	 * @param persoonOfKlant String
	 * @param id int
	 * @return List<Adres>
	 * 
	 * returnt een adreslijst van een bepaalde persoon of klant
	 * String persoonOfKlant definieert of het om een Persoon of een Klant gaat.
	 * Int id definieert welke id deze Persoon of Klant heeft.
	 */
	public List<Adres> leesSelectief(String persoonOfKlant, int id){
		
		List<Adres> adreslijst = new ArrayList<Adres>();
		List<Integer> dbadresIdLijst = null;
		DbPersoonAdresDao dbpersoonadresdao = null;
		DbKlantAdresDao dbklantadresdao = null;
		DbAdresDao dbadresdao = new DbAdresDao();
		DbGemeenteDao dbgemeentedao = new DbGemeenteDao();
		DbStraatDao dbstraatdao = new DbStraatDao();
		AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();
			
		if (persoonOfKlant.equals("persoon")){
			
			dbpersoonadresdao = new DbPersoonAdresDao();
			dbadresIdLijst = dbpersoonadresdao.leesLijst(id);

		} else if (persoonOfKlant.equals("klant")){		

			dbklantadresdao = new DbKlantAdresDao();
			dbadresIdLijst = dbklantadresdao.leesLijst(id);
		}else {
			// In deze app wordt enkel gezocht naar adressen van 'persoon' of van 'klant'
		}
	
		Iterator<Integer> adresIdIterator = dbadresIdLijst.iterator();
		while (adresIdIterator.hasNext()){
			int adresId = adresIdIterator.next();
			
			Adres adres = (Adres) adresDaoAdapter.lees(adresId);
			
			adreslijst.add(adres);
		}
		
		return adreslijst;
	}

	/**
	 * @param persoonId int
	 * 
	 * verwijdert adres met geparameteriseerd persoonId.
	 * Indien straat of gemeente nergens meer worden gebruikt wordt ook dat verwijderd
	 */
	public void verwijderVanPersoon(int persoonId) {
		
		DbPersoonAdresDao dbPersoonAdresDao = new DbPersoonAdresDao();
		DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
		AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();
		
		//adressenlijst ophalen
		ArrayList<Integer> adressenlijst = null;
		adressenlijst = (ArrayList<Integer>) dbPersoonAdresDao.leesLijst(persoonId);
		
		//persoonAdressen verwijderen
		dbPersoonAdresDao.verwijder(persoonId);

		//voor elk adres
		Iterator<Integer> it = adressenlijst.iterator();
		while(it.hasNext()){
			int adresId = it.next();
			
			//straatId en gemeenteId ophalen
			// indien dit adres nergens anders gebruikt word
			// delete dbAdres 
			boolean adresPersoonInGebruik = dbPersoonAdresDao.isInGebruik(adresId);
			boolean adresKlantInGebruik = dbKlantAdresDao.isInGebruik(adresId);
		
			if (!adresPersoonInGebruik && !adresKlantInGebruik){
				adresDaoAdapter.verwijder(adresId);
			}
		}
	}

	
}

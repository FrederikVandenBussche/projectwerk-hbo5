package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dao.DbAdresDao;
import be.miras.programs.frederik.dao.DbGemeenteDao;
import be.miras.programs.frederik.dao.DbKlantAdresDao;
import be.miras.programs.frederik.dao.DbPersoonAdresDao;
import be.miras.programs.frederik.dao.DbStraatDao;
import be.miras.programs.frederik.dao.HibernateUtil;
import be.miras.programs.frederik.dao.ICRUD;
import be.miras.programs.frederik.dbo.DbAdres;
import be.miras.programs.frederik.dbo.DbGemeente;
import be.miras.programs.frederik.dbo.DbPersoonAdres;
import be.miras.programs.frederik.dbo.DbStraat;
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
	private static final Logger LOGGER = Logger.getLogger(PersoonAdresDaoAdapter.class);
	private final String TAG = "PersoonAdresDaoAdapter: ";
	
	@Override
	public int voegToe(Object o) {
		Adres adres = (Adres) o;
		
		DbPersoonAdresDao dbPersoonAdresDao = new DbPersoonAdresDao();
		DbAdresDao dbAdresDao = new DbAdresDao();
		DbGemeenteDao dbGemeenteDao = new DbGemeenteDao();
		DbStraatDao dbStraatDao = new DbStraatDao();
		DbAdres dbAdres = new DbAdres();
		DbPersoonAdres dbPersoonAdres = new DbPersoonAdres();
		
		//zoekt ID van postcode + gemeente
		// indien de opgegeven postcode + gemeente nog niet in de databank zit
		// voeg toe
		int gemeenteId = dbGemeenteDao.geefIdVan(adres.getPostcode(), adres.getPlaats());

		if (gemeenteId < 0){
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
		if (straatId < 0){
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
		int dbAdresId = dbAdresDao.voegToe(dbAdres);
		
		int dbPersoonId = adres.getPersoonId();
		
		dbPersoonAdres.setAdresId(dbAdresId);
		dbPersoonAdres.setPersoonId(dbPersoonId);
		dbPersoonAdresDao.voegToe(dbPersoonAdres);
		
		
		return dbAdresId;
	}

	@Override
	public Object lees(int id) {
		
		Adres adres = new Adres();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM Adres where id = :id";
		
		List<Adres> lijst = new ArrayList<Adres>();
		try{
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id",  id);
			lijst = q.list();
			session.flush();
			if(!transaction.wasCommitted()){
				transaction.commit();
			}
		} catch (Exception e){
			if (transaction != null){
				transaction.rollback();
			}
			e.printStackTrace();
			LOGGER.error("Exception: " + TAG + "lees(id)" + id + "", e);
		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			adres = lijst.get(0);
		}
		
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

			//straatId en gemeenteId ophalen
			DbAdresDao dbAdresdao = new DbAdresDao();
			DbAdres dbadres = (DbAdres) dbAdresdao.lees(adresId);
			int straatId = dbadres.getStraatId();
			int gemeenteId = dbadres.getGemeenteId();
			
			// delete dbAdres
			dbAdresdao.verwijder(adresId);
			// indien de straat nergens anders gebruikt wordt.
			// deze uit de db verwijderen
			boolean straatInGebruik = dbAdresdao.isStraatInGebruik(straatId);
			
			if(!straatInGebruik){
				DbStraatDao dbStraatDao = new DbStraatDao();
				dbStraatDao.verwijder(straatId);
			}
			// indien de gemeente nergens anders gebruikt wordt
			// deze uit de db verwijderen
			boolean gemeenteInGebruik = dbAdresdao.isGemeenteInGebruik(gemeenteId);
			
			if(!gemeenteInGebruik){
				DbGemeenteDao dbGemeenteDao = new DbGemeenteDao();
				dbGemeenteDao.verwijder(gemeenteId);
			}
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
			
			DbAdres dbadres = (DbAdres) dbadresdao.lees(adresId);
			
			DbStraat dbstraat = (DbStraat) dbstraatdao.lees(dbadres.getStraatId());
			DbGemeente dbgemeente = (DbGemeente) dbgemeentedao.lees(dbadres.getGemeenteId());
			
			Adres adres = new Adres();
			adres.setId(adresId);
			adres.setStraat(dbstraat.getNaam());
			adres.setNummer(dbadres.getHuisnummer());
			adres.setBus(dbadres.getBus());
			adres.setPostcode(dbgemeente.getPostcode());
			adres.setPlaats(dbgemeente.getNaam());
			
			adreslijst.add(adres);
		}
		
		return adreslijst;
	}

	/**
	 * @param persoonId int
	 * 
	 * verwijdert adresmet geparameteriseerd persoonId.
	 * Indien straat of gemeente nergens meer worden gebruikt wordt ook dat verwijderd
	 */
	public void verwijderVanPersoon(int persoonId) {
		
		DbPersoonAdresDao dbPersoonAdresDao = new DbPersoonAdresDao();
		
		//adressenlijst ophalen
		ArrayList<Integer> adressenlijst = null;
		adressenlijst = (ArrayList<Integer>) dbPersoonAdresDao.leesLijst(persoonId);
		
		//persoonAdressen verwijderen
		dbPersoonAdresDao.verwijder(persoonId);
		
		DbAdresDao dbAdresdao = new DbAdresDao();
		DbStraatDao dbStraatDao = new DbStraatDao();
		DbGemeenteDao dbGemeenteDao = new DbGemeenteDao();
		
		//voor elk adres
		Iterator<Integer> it = adressenlijst.iterator();
		while(it.hasNext()){
			int adresId = it.next();
			
			//straatId en gemeenteId ophalen
			
			DbAdres dbadres = (DbAdres) dbAdresdao.lees(adresId);
			int straatId = dbadres.getStraatId();
			int gemeenteId = dbadres.getGemeenteId();
			// delete dbAdres
			dbAdresdao.verwijder(adresId);
			// indien de straat nergens anders gebruikt wordt.
			// deze uit de db verwijderen
			boolean straatInGebruik = dbAdresdao.isStraatInGebruik(straatId);
			if(!straatInGebruik){
				dbStraatDao.verwijder(straatId);
			}
			// indien de gemeente nergens anders gebruikt wordt
			// deze uit de db verwijderen
			boolean gemeenteInGebruik = dbAdresdao.isGemeenteInGebruik(gemeenteId);
			if(!gemeenteInGebruik){
				
				dbGemeenteDao.verwijder(gemeenteId);
			}

		}
	}

	
}

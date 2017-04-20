package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

public class PersoonAdresDaoAdapter implements ICRUD {
	String TAG = "AdresDaoAdapter ";
	
	@Override
	public boolean voegToe(Object o) {
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
		System.out.println(TAG + "de gemeenteId = " + gemeenteId);
		if (gemeenteId < 0){
			// gemeente nog niet in databank
			DbGemeente dbGemeente = new DbGemeente();
			dbGemeente.setNaam(adres.getPlaats());
			dbGemeente.setPostcode(adres.getPostcode());
			dbGemeenteDao.voegToe(dbGemeente);
			
			gemeenteId = dbGemeenteDao.geefIdVan(adres.getPostcode(), adres.getPlaats());
		}
		System.out.println(TAG + "de gemeenteId = " + gemeenteId);
		
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
		System.out.print(TAG + "de straatnaamId =  " + straatId);
		
		
		dbAdres.setStraatId(straatId);
		dbAdres.setGemeenteId(gemeenteId);
		dbAdres.setHuisnummer(adres.getNummer());
		dbAdres.setBus(adres.getBus());
		dbAdresDao.voegToe(dbAdres);
		
		int dbAdresId = dbAdresDao.zoekMaxId();
		int dbPersoonId = adres.getPersoonId();
		
		dbPersoonAdres.setAdresId(dbAdresId);
		dbPersoonAdres.setPersoonId(dbPersoonId);
		dbPersoonAdresDao.voegToe(dbPersoonAdres);
		
		
		return true;
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
			session.getTransaction().commit();
		} catch (Exception e){
			if (transaction != null){
				transaction.rollback();
			}
			e.printStackTrace();
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
	public boolean wijzig(Object o) {
		return false;
	}

	@Override
	public boolean verwijder(int adresId) {
		
		DbPersoonAdresDao dbPersoonAdresDao = new DbPersoonAdresDao();
		DbPersoonAdres dbPersoonAdres = null;
		
		
		// persoonId ophalen
		dbPersoonAdres =  (DbPersoonAdres) dbPersoonAdresDao.lees(adresId);
		int persoonId = dbPersoonAdres.getPersoonId();
		
		//persoonAdres verwijderen
		dbPersoonAdresDao.verwijder(persoonId, adresId);
		
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
		return true;
	}

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
	
	public boolean verwijderSelectief(String kolomnaam, int id) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		String query = "DELETE FROM Adres where {kolomnaam} = :id";
		query = query.replace("{kolomnaam}", kolomnaam);
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", id);
			q.executeUpdate();
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

	public void verwijderVanPersoon(int persoonId) {
		System.out.println(TAG + "ik wil dit adres verwijderen " + persoonId);
		
		DbPersoonAdresDao dbPersoonAdresDao = new DbPersoonAdresDao();
		
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

	public int geefMaxId() {
		DbAdresDao dbAdresDao = new DbAdresDao();
		int maxId = dbAdresDao.zoekMaxId();
		return maxId;
	}
	
}

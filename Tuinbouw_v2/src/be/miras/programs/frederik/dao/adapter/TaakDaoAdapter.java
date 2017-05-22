package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import be.miras.programs.frederik.dao.DbOpdrachtTaakDao;
import be.miras.programs.frederik.dao.DbStatusDao;
import be.miras.programs.frederik.dao.DbTaakDao;
import be.miras.programs.frederik.dao.DbVooruitgangDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dao.ICRUD;
import be.miras.programs.frederik.dbo.DbOpdrachtTaak;
import be.miras.programs.frederik.dbo.DbStatus;
import be.miras.programs.frederik.dbo.DbTaak;
import be.miras.programs.frederik.dbo.DbVooruitgang;
import be.miras.programs.frederik.dbo.DbWerknemerOpdrachtTaak;
import be.miras.programs.frederik.model.Personeel;
import be.miras.programs.frederik.model.Planning;
import be.miras.programs.frederik.model.Taak;

/**
  * @author Frederik Vanden Bussche
 * 
 * Adapter die het model Taak
 * koppelt aan de databankobjecten : DbOpdrachtTaak, DbTaak, DbVooruitgang
 *
 */
public class TaakDaoAdapter implements ICRUD {
	private DbOpdrachtTaakDao dbOpdrachtTaakDao;
	private DbTaakDao dbTaakDao;

	/**
	 * 
	 */
	public TaakDaoAdapter() {
		super();
		this.dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
		this.dbTaakDao = new DbTaakDao();
	}

	@Override
	public int voegToe(Object o) {
		Taak taak = (Taak) o;
		
		DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
		
		DbTaak dbTaak = new DbTaak();
		DbOpdrachtTaak dbOpdrachtTaak= new DbOpdrachtTaak();
		DbVooruitgang dbVooruitgang = new DbVooruitgang();
		
		//nieuwe DbTaak toevoegen
		dbTaak.setNaam(taak.getTaakNaam());
		dbTaak.setVisible(1);
		int dbTaakId =  dbTaakDao.geefIdVan(dbTaak.getNaam(), dbTaak.getVisible());
		if (dbTaakId < 0){
			dbTaakId = dbTaakDao.voegToe(dbTaak);
		}
		dbTaak.setId(dbTaakId);
		
		//nieuwe DbVooruitgang toevoegen
		dbVooruitgang.setPercentage(0);
		// een statusId van 1 == "Niet gestart"
		dbVooruitgang.setStatusId(1); 
		dbVooruitgangDao.voegToe(dbVooruitgang);
		
		//nieuwe dbOpdrachtTaak toevoegen
		int opdrachtId = taak.getOpdrachtId();
		int vooruitgangId = dbVooruitgang.getId();

		dbOpdrachtTaak.setOpdrachtId(opdrachtId);
		dbOpdrachtTaak.setTaakId(dbTaak.getId());
		dbOpdrachtTaak.setVooruitgangId(vooruitgangId);
		dbOpdrachtTaak.setOpmerking(taak.getOpmerking());
		dbOpdrachtTaakDao.voegToe(dbOpdrachtTaak);
		
		
		return dbTaak.getId();
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
		Taak taak = (Taak) o;
		
		DbTaak dbTaak = (DbTaak) dbTaakDao.lees(taak.getId());
		
		if (!dbTaak.getNaam().equals(taak.getTaakNaam())){
			/*
			 * indien dbTaak enkel hier gebruikt wordt : wijzigen
			 * anders : maak een nieuwe dbTaak aan.
			 */
			dbTaak.setNaam(taak.getTaakNaam());
			
			long aantal = dbOpdrachtTaakDao.hoeveelMetTaakId(taak.getId());
			if (aantal == 1 ){
				//wijzigen
				dbTaak.setId(taak.getId());
				dbTaakDao.wijzig(dbTaak);
			} else {
				//nieuw toevoegen
				dbTaakDao.voegToe(dbTaak);
			}	
		}
				
		int dbOpdrachtId = taak.getOpdrachtId();
		int dbTaakId = dbTaak.getId();
		String dbOpmerking = taak.getOpmerking();
		dbOpdrachtTaakDao.wijzigOpmerking(dbOpdrachtId, dbTaakId, dbOpmerking);
		
	}

	@Override
	public void verwijder(int taakId) {
		System.out.println("taakId: " + taakId);
		DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
		DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
		
		DbOpdrachtTaak dbOpdrachtTaak = (DbOpdrachtTaak) dbOpdrachtTaakDao.leesWaarTaakId(taakId);
		int opdrachtTaakId = dbOpdrachtTaak.getId();
		dbWerknemerOpdrachtTaakDao.verwijderWaarTaakId(taakId);
		dbOpdrachtTaakDao.verwijder(opdrachtTaakId);
		
		// verwijder de DbTaak als ze enkel in deze dbOpdrachtTaak wordt gebruikt 
		long aantal = dbOpdrachtTaakDao.hoeveelMetTaakId(taakId);
		if (aantal <= 0){
			dbTaakDao.verwijder(taakId);
		}
		dbVooruitgangDao.verwijder(dbOpdrachtTaak.getVooruitgangId());
		
	}

	/**
	 * @param opdrachtId int
	 * @return List<Taak>
	 * 
	 * haal alle Taak op met een bepaalde opdrachtId 
	 */
	public List<Taak> leesAlle(int opdrachtId) {
		List<Taak> taakLijst = new ArrayList<Taak>();
		DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
		DbStatusDao dbStatusDao = new DbStatusDao();
		
		List<DbOpdrachtTaak> dbOpdrachtTaakLijst = dbOpdrachtTaakDao.leesLijst(opdrachtId);
		
		Iterator<DbOpdrachtTaak> it = dbOpdrachtTaakLijst.iterator();
		while(it.hasNext()){
			
			Taak taak = new Taak();
			
			DbOpdrachtTaak dbOpdrachtTaak = it.next();
						
			int dbTaakId = dbOpdrachtTaak.getTaakId();
			
			DbTaak dbTaak = (DbTaak) dbTaakDao.lees(dbTaakId);
			int dbVooruitgangId = dbOpdrachtTaak.getVooruitgangId();
			DbVooruitgang dbVooruitgang = (DbVooruitgang) dbVooruitgangDao.lees(dbVooruitgangId);
			int statusId = dbVooruitgang.getStatusId();
			DbStatus dbStatus = (DbStatus) dbStatusDao.lees(statusId);
			
			int taakId = dbTaak.getId();
			String taakNaam = dbTaak.getNaam();
			int visible = dbTaak.getVisible();
			boolean isVisible = true;
			if (visible == 0){
				isVisible = false;
			}
			String opmerking = dbOpdrachtTaak.getOpmerking();
			int vooruitgangPercentage = dbVooruitgang.getPercentage();
			String status = dbStatus.getNaam();
	
			taak.setId(taakId);
			taak.setOpdrachtId(opdrachtId);
			taak.setTaakNaam(taakNaam);
			taak.setVisible(isVisible);
			taak.setOpmerking(opmerking);
			taak.setVooruitgangPercentage(vooruitgangPercentage);
			taak.setStatus(status);
			
			taakLijst.add(taak);
		}
		
		return taakLijst;
	}

	/**
	 * @param taakId int
	 * @return Taak
	 * 
	 * haal de Taak op met de geparameteriseerde Id
	 */
	public Taak haalTaak(int taakId){
		Taak taak = new Taak();
		
		DbStatusDao dbStatusDao = new DbStatusDao();
		DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
		DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
		DbTaakDao dbTaakDao = new DbTaakDao();
		DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
		
		List<Planning> planningLijst = new ArrayList<Planning>();
		List<Planning> gewerkteUrenLijst = new ArrayList<Planning>();
		
		// zoek de taak
		DbTaak dbTaak = (DbTaak) dbTaakDao.lees(taakId);
		DbOpdrachtTaak dbOpdrachtTaak = dbOpdrachtTaakDao.leesWaarTaakId(taakId);
		DbVooruitgang dbVooruitgang = (DbVooruitgang) dbVooruitgangDao.lees(dbOpdrachtTaak.getVooruitgangId());
		DbStatus dbStatus = (DbStatus) dbStatusDao.lees(dbVooruitgang.getStatusId());
		
		taak.setId(taakId);
		taak.setOpdrachtId(dbOpdrachtTaak.getId());
		taak.setTaakNaam(dbTaak.getNaam());
		taak.setOpmerking(dbOpdrachtTaak.getOpmerking());
		taak.setVooruitgangPercentage(dbVooruitgang.getPercentage());
		taak.setStatus(dbStatus.getNaam());
		
		// planningLijst ophalen
		List<DbWerknemerOpdrachtTaak> dbWerknemerOpdrachtTaakLijst = dbWerknemerOpdrachtTaakDao
				.leesWaarTaakId(taakId);

		Iterator<DbWerknemerOpdrachtTaak> wotIt = dbWerknemerOpdrachtTaakLijst.iterator();
		while (wotIt.hasNext()) {
			DbWerknemerOpdrachtTaak wot = wotIt.next();
			if (wot.getEinduur() == null) {
				HashMap<Integer, String> werknemerMap = geefWerknemerMap();
				Planning planning = new Planning();
				planning.setId(wot.getId());
				int werknemerId = wot.getWerknemerId();
				
				String werknemerNaam = werknemerMap.get(werknemerId);
				planning.setWerknemer(werknemerNaam);
				planning.setBeginuur(wot.getBeginuur());
				planning.setEinduur(wot.getEinduur());
				planning.setIsAanwezig(wot.getAanwezig());

				planningLijst.add(planning);

			} else {
				
				Planning gewerkteUren = new Planning();
				gewerkteUren.setId(wot.getId());
				int werknemerId = wot.getWerknemerId();
				HashMap<Integer, String> werknemerMap = geefWerknemerMap();
				String werknemerNaam = werknemerMap.get(werknemerId);
				gewerkteUren.setWerknemer(werknemerNaam);
				gewerkteUren.setBeginuur(wot.getBeginuur());
				gewerkteUren.setEinduur(wot.getEinduur());
				gewerkteUren.setIsAanwezig(wot.getAanwezig());

				gewerkteUrenLijst.add(gewerkteUren);
			}
		}
		
		taak.setPlanningLijst(planningLijst);
		taak.setGewerkteUrenLijst(gewerkteUrenLijst);
		
		return taak;
	}

	/**
	 * @return HashMap<Integer, String>
	 * 
	 * return een Hashmap met werknemerId en werknemer voornaam+""+naam
	 */
	public HashMap<Integer, String> geefWerknemerMap() {

		PersoneelDaoAdapter dao = new PersoneelDaoAdapter();
		List<Personeel> lijst = new ArrayList<Personeel>();
		HashMap<Integer, String> werknemerMap = new HashMap<Integer, String>();

		lijst = (List<Personeel>) (Object) dao.leesAlle();

		Iterator<Personeel> it = lijst.iterator();
		while (it.hasNext()) {
			Personeel p = it.next();
			int id = p.getWerknemerId();
			String naam = p.getVoornaam().concat(" ").concat(p.getNaam());
			werknemerMap.put(id, naam);
		}

		return werknemerMap;
	}
	
	
}

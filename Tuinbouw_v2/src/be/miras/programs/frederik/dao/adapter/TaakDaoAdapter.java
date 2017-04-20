package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
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
import be.miras.programs.frederik.model.Taak;

public class TaakDaoAdapter implements ICRUD {
	

	@Override
	public boolean voegToe(Object o) {
		Taak taak = (Taak) o;
		
		DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
		DbTaakDao dbTaakDao = new DbTaakDao();
		DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
		DbTaak dbTaak = new DbTaak();
		DbOpdrachtTaak dbOpdrachtTaak= new DbOpdrachtTaak();
		DbVooruitgang dbVooruitgang = new DbVooruitgang();
		
		//nieuwe DbTaak toevoegen
		dbTaak.setNaam(taak.getTaakNaam());
		dbTaak.setVisible(1);
		int dbTaakId =  dbTaakDao.geefIdVan(dbTaak.getNaam(), dbTaak.getVisible());
		if (dbTaakId < 0){
			dbTaakDao.voegToe(dbTaak);
		} else {
			dbTaak.setId(dbTaakId);
		}
		

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
		
		
		return false;
	}

	@Override
	public Object lees(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> leesAlle() {
		return null;
		
	}

	@Override
	public boolean wijzig(Object o) {
		Taak taak = (Taak) o;
		
		DbTaakDao dbTaakDao = new DbTaakDao();
		DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
		
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
		
		return false;
	}

	@Override
	public boolean verwijder(int id) {
				
		DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
		DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
		DbTaakDao dbTaakDao = new DbTaakDao();
		DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
		
		DbOpdrachtTaak dbOpdrachtTaak = (DbOpdrachtTaak) dbOpdrachtTaakDao.lees(id);
		
		/*
		 * verwijder de DbTaak als ze enkel in deze dbOpdrachtTaak wordt gebruikt 
		 */
		int dbTaakId = dbOpdrachtTaak.getTaakId();
		
		dbWerknemerOpdrachtTaakDao.verwijderWaarTaakId(dbTaakId);
		
		dbOpdrachtTaakDao.verwijder(id);
		dbVooruitgangDao.verwijder(dbOpdrachtTaak.getVooruitgangId());
				
		if (dbOpdrachtTaakDao.hoeveelMetTaakId(dbTaakId) <= 0) {
			dbTaakDao.verwijder(dbTaakId);
		}
		
		
		return false;
	}

	public List<Taak> leesAlle(int opdrachtId) {
		List<Taak> taakLijst = new ArrayList<Taak>();
		DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
		
		List<DbOpdrachtTaak> dbOpdrachtTaakLijst = dbOpdrachtTaakDao.leesLijst(opdrachtId);
		
		Iterator<DbOpdrachtTaak> it = dbOpdrachtTaakLijst.iterator();
		while(it.hasNext()){
			DbTaakDao dbTaakDao = new DbTaakDao();
			DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
			DbStatusDao dbStatusDao = new DbStatusDao();
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

}

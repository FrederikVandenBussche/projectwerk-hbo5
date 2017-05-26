package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.miras.programs.frederik.dao.DbKlantAdresDao;
import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.DbOpdrachtMateriaalDao;
import be.miras.programs.frederik.dao.DbOpdrachtTaakDao;
import be.miras.programs.frederik.dao.DbStatusDao;
import be.miras.programs.frederik.dao.DbTaakDao;
import be.miras.programs.frederik.dao.DbVooruitgangDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dbo.DbOpdracht;

public class KlantDaoAdapter {

	
	public KlantDaoAdapter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id int
	 * @return boolean
	 * 
	 * return true indien deze klant geen opdrachtTaak heeft waarvan de
	 * statudID van de Vooruitgang op niet op "gefactureerd" staat.
	 */
	public boolean isOngefactureerd(int id) {
		boolean isOngefactureerd = false;
		
		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
		DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
		DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
		DbStatusDao dbStatusDao = new DbStatusDao();
		List<DbOpdracht> dbOpdrachtLijst = new ArrayList<DbOpdracht>();
		List<Integer> vooruitgangIdList = new ArrayList<Integer>();
		
		dbOpdrachtLijst = dbOpdrachtDao.leesWaarKlantId(id);
		
		String benodigdeStatus = "Gefactureerd";
		int benodigdeStatusId = dbStatusDao.lees(benodigdeStatus);
		
		Iterator<DbOpdracht> dbOpdrachtIt = dbOpdrachtLijst.iterator();
		while(dbOpdrachtIt.hasNext()){
			DbOpdracht dbOpdracht = dbOpdrachtIt.next();
			
			if (!isOngefactureerd){
				
				int opdrachtId = dbOpdracht.getId();
				vooruitgangIdList = dbOpdrachtTaakDao.leesVooruitgangIds(opdrachtId);
				
				Iterator<Integer> vooruitgangIt = vooruitgangIdList.iterator();
				while (vooruitgangIt.hasNext()){
					int vooruitgangId = vooruitgangIt.next();
					
					int statusId = dbVooruitgangDao.leesStatusId(vooruitgangId);
					
					if (statusId != benodigdeStatusId){
						isOngefactureerd = true;
					}
				}	
			}
		}
		return isOngefactureerd;
	}

	/**
	 * @param id int
	 * 
	 * verwijder alle gegevens van de klant met geparameteriseerde id
	 */
	public void verwijder(int id) {
		DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
		DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
		DbTaakDao dbTaakDao = new DbTaakDao();
		DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
		DbOpdrachtMateriaalDao dbOpdrachtMateriaalDao = new DbOpdrachtMateriaalDao();
		DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
		DbKlantDao dbKlantDao = new DbKlantDao();
		AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();
		int opdrachtId = Integer.MIN_VALUE;
		int adresId = Integer.MIN_VALUE;
		
		List<DbOpdracht> dbOpdrachtLijst = dbOpdrachtDao.leesWaarKlantId(id);
		
		Iterator<DbOpdracht> dbOpdrachtIt = dbOpdrachtLijst.iterator();
		while (dbOpdrachtIt.hasNext()){
			DbOpdracht dbOpdracht = dbOpdrachtIt.next();
			
			opdrachtId = dbOpdracht.getId();
			
			// de opdracht uit de databank verwijderen
			
			// 1. Werknemer_Opdracht_Taak
			dbWerknemerOpdrachtTaakDao.verwijderWaarOpdrachtId(opdrachtId);
			
			// 2. Opdracht_Materiaal
			dbOpdrachtMateriaalDao.verwijderWaarOpdrachtId(opdrachtId);
			
			// 3. Opdracht_Taak
			// ik wil eerst een lijst met Vooruitgag Ids en taakIds
			List<Integer> vooruitgangIdLijst = dbOpdrachtTaakDao.leesVooruitgangIds(opdrachtId);
			List<Integer> taakIdLijst = dbOpdrachtTaakDao.leesTaakIds(opdrachtId);
			dbOpdrachtTaakDao.verwijderWaarOpdrachtId(opdrachtId);
			
			// 4. Vooruitgang
			for (int vId : vooruitgangIdLijst) {
				
				dbVooruitgangDao.verwijder(vId);
			}
			
			// 5. Taak
			for (int tId : taakIdLijst) {
				
				dbTaakDao.verwijder(tId);
			}
			
			// 6. Opdracht
			dbOpdrachtDao.verwijder(opdrachtId);
		}
		
		//klantAdressen Verwijderen
		List<Integer> adresIdLijst = dbKlantAdresDao.leesLijst(id);
		dbKlantAdresDao.verwijder(id);
		
		Iterator<Integer> adresIdIt = adresIdLijst.iterator();
		while(adresIdIt.hasNext()){
			adresId = adresIdIt.next();
			
			adresDaoAdapter.verwijder(adresId);
		}
		
		dbKlantDao.verwijder(id);
	}

	
}

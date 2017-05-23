package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dbo.DbBedrijf;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbParticulier;
import be.miras.programs.frederik.model.Opdracht;

/**
 * @author Frederik Vanden Bussche
 * 
 * adapter die het model opdracht koppelt aan de databankobjecten DbOpdracht en DbKlant
 *
 */
public class OpdrachtDaoAdapter {

	
	public OpdrachtDaoAdapter() {
	}
	
	/**
	 * @return List<Opdracht>
	 * 
	 * Haal een lijst op met alle opdrachten
	 */
	public List<Opdracht> haalOpdrachtenOp(){
		List<Opdracht> opdrachtLijst = new ArrayList<Opdracht>();
		
		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
		DbKlantDao dbKlantDao = new DbKlantDao();
		
		List<DbOpdracht> dbOpdrachtLijst = (List<DbOpdracht>) (Object) dbOpdrachtDao.leesAlle();
		
		Iterator<DbOpdracht> it = dbOpdrachtLijst.iterator();
		DbOpdracht dbOpdracht = null;
				
		while(it.hasNext()){
			dbOpdracht = it.next();
			
			Opdracht opdracht = new Opdracht();
			
			int klantId = dbOpdracht.getKlantId();
			int klantAdresId = dbOpdracht.getKlantAdresId();
			
			DbKlant dbKlant = (DbKlant) dbKlantDao.lees(klantId);
			String naam = null;
			
			if(dbKlant.getClass().getSimpleName().equals("DbParticulier")) {
				String voornaam = ((DbParticulier) dbKlant).getVoornaam();
				String familienaam = ((DbParticulier) dbKlant).getNaam();
					naam = familienaam.concat(" ").concat(voornaam);
			} else if(dbKlant.getClass().getSimpleName().equals("DbBedrijf")){
				naam = ((DbBedrijf) dbKlant).getBedrijfnaam();
			} else {
				// DbKlant is geen DbParticulier en ook geen DbBedrijf
			}
					
			int id = dbOpdracht.getId();
			String opdrachtNaam = dbOpdracht.getNaam();
			Date startDatum = dbOpdracht.getStartdatum();
			Date eindDatum = dbOpdracht.getEinddatum();
			
			opdracht.setId(id);
			opdracht.setKlantId(klantId);
			opdracht.setKlantAdresId(klantAdresId);
			opdracht.setKlantNaam(naam);
			opdracht.setOpdrachtNaam(opdrachtNaam);
			opdracht.setStartDatum(startDatum);
			opdracht.setEindDatum(eindDatum);
			opdracht.setLatitude(dbOpdracht.getLatitude());
			opdracht.setLongitude(dbOpdracht.getLongitude());
			
			opdrachtLijst.add(opdracht);
		}
			
		return opdrachtLijst;
	}


}

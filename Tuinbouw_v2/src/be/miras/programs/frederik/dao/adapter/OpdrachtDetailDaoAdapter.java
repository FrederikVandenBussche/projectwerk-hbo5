package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dbo.DbBedrijf;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbParticulier;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.GoogleApis;
import be.miras.programs.frederik.util.Sorteer;

/**
 * @author Frederik Vanden Bussche
 * 
 * adapter die het object OpdrachtDetail koppeld aan de databankobjecten
 *
 */
public class OpdrachtDetailDaoAdapter {	
	
	
	public OpdrachtDetailDaoAdapter(){
	}
	
	/**
	 * @param id int
	 * @return OpdrachtDetailData
	 * 
	 * return de benodigde data om de details van de opdracht met geparameteriseerde id te bekijken
	 */
	public OpdrachtDetailData haalOpdrachtdetailDataOp(int id) {
		// maak een lijst met alle klanten met hun aanspreeknaam
		// op het scherm OpdrachtDedail.jsp wodt een keuzemenu samengesteld
		// waardoor men een reeds bestaande klant kan kiezen
		DbKlant dbKlant = null;
		
		Map<Integer, String> klantNaamMap = new HashMap<Integer, String>();
		
		DbKlantDao dbKlantDao = new DbKlantDao();
		MateriaalDaoAdapter materiaalDaoAdapter = new MateriaalDaoAdapter();
		List<Materiaal> materiaalLijst = new ArrayList<Materiaal>();
		List<Materiaal> gebruikteMaterialenLijst = new ArrayList<Materiaal>();

		ArrayList<DbKlant> klantLijst = (ArrayList<DbKlant>) (Object) dbKlantDao.leesAlle();
		Iterator<DbKlant> it = klantLijst.iterator();
		while (it.hasNext()) {
			dbKlant = it.next();
			int itKlantId = dbKlant.getId();
			String itKlantNaam = dbKlant.geefAanspreekNaam();
			klantNaamMap.put(itKlantId, itKlantNaam);
		}
		
		//sorteer de klantNaamMap op naam
		klantNaamMap = Sorteer.SorteerMap(klantNaamMap);

		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
		AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();

		Opdracht opdracht = new Opdracht();
		
		String variabelveld1 = ". Opdrachtgever wijzigen: ";
		String variabelveld2 = ", wijzigen :";

		String aanspreeknaam = null;
		String buttonNaam = null;
		String adresString = null;
		HashMap<Integer, String> adresMap = null;
		String staticmap = null;
		String googlemap = null;
		
		if (id < 0){
			aanspreeknaam = "";
			buttonNaam = "Opslaan";
			
		}else {
			// zoek de opdracht aan de hand van de opdrachtId
			DbOpdracht dbOpdracht = (DbOpdracht) dbOpdrachtDao.lees(id);
			opdracht.setId(id);
			opdracht.setKlantId(dbOpdracht.getKlantAdresId());
			opdracht.setKlantAdresId(dbOpdracht.getKlantAdresId());
			opdracht.setOpdrachtNaam(dbOpdracht.getNaam());
			opdracht.setStartDatum(dbOpdracht.getStartdatum());
			opdracht.setEindDatum(dbOpdracht.getEinddatum());
			
			// aanspreeknaam definiëren
			dbKlant = (DbKlant) dbKlantDao.lees(dbOpdracht.getKlantId());
			String naam = null;

			if (dbKlant.getClass().getSimpleName().equals("DbParticulier")) {
				String voornaam = ((DbParticulier) dbKlant).getVoornaam();
				String familienaam = ((DbParticulier) dbKlant).getNaam();
				naam = familienaam.concat(" ").concat(voornaam);
			} else if (dbKlant.getClass().getSimpleName().equals("DbBedrijf")) {
				naam = ((DbBedrijf) dbKlant).getBedrijfnaam();
			} else {
				// DbKlant is geen DbParticulier en ook geen DbBedrijf
			}

			opdracht.setKlantNaam(naam);

			aanspreeknaam = "voor ";
			aanspreeknaam = aanspreeknaam.concat(naam);
			buttonNaam = "Wijziging opslaan";

			// adreslijst die bij de opdrachtgever van deze opdracht hoort
			// ophalen.
			List<Adres> adresLijst = adresDaoAdapter.leesWaarKlantId(dbOpdracht.getKlantId());

			adresMap = new HashMap<Integer, String>();

			Iterator<Adres> adresIter = adresLijst.iterator();
			while (adresIter.hasNext()) {
				Adres adres = adresIter.next();
				adresMap.put(adres.getId(), adres.toString());
			}

			int klantAdresId = dbOpdrachtDao.geefKlantAdresId(id);
			Adres adres = adresDaoAdapter.leesWaarKlantAdresId(klantAdresId);
			adresString = adres.toString();

			staticmap = GoogleApis.urlBuilderStaticMap(adres);
			googlemap = GoogleApis.urlBuilderGoogleMaps(adres);
		
			leesTakenLijst(opdracht);

			// lijst met alle materialen ophalen
			materiaalLijst = (List<Materiaal>) (Object) materiaalDaoAdapter.leesAlle();

			// lijst met de gebruikteMaterialen van deze opdracht ophalen
			gebruikteMaterialenLijst = materiaalDaoAdapter.leesOpdrachtMateriaal(opdracht.getId());
			opdracht.setGebruiktMateriaalLijst(gebruikteMaterialenLijst);
		}
		
		OpdrachtDetailData opdrachtDetailData = new OpdrachtDetailData(aanspreeknaam, variabelveld1, variabelveld2,
			buttonNaam, opdracht, klantNaamMap, adresString, adresMap, materiaalLijst, staticmap, googlemap);
	
	return opdrachtDetailData;
	}
	
	/**
	 * @param opdracht Opdracht
	 * @return Opdracht
	 * 
	 *  voegt de takenlijst toe aan de opdracht
	 */
	private Opdracht leesTakenLijst(Opdracht opdracht) {
		// lees de takenlijst die bij deze opdracht hoort.
		TaakDaoAdapter taakDaoAdapter = new TaakDaoAdapter();

		List<Taak> taakLijst = (List<Taak>) (Object) taakDaoAdapter.leesAlle(opdracht.getId());

		opdracht.setTaakLijst(taakLijst);
		
		return opdracht;
	}

	
}

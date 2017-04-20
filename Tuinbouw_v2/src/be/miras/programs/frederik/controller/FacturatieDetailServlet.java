package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbMateriaalDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.DbOpdrachtMateriaalDao;
import be.miras.programs.frederik.dao.DbOpdrachtTaakDao;
import be.miras.programs.frederik.dao.DbStatusDao;
import be.miras.programs.frederik.dao.DbTaakDao;
import be.miras.programs.frederik.dao.DbTypeMateriaalDao;
import be.miras.programs.frederik.dao.DbVooruitgangDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dao.adapter.AdresAdapter;
import be.miras.programs.frederik.dao.adapter.WerkgeverDaoAdapter;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbMateriaal;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbOpdrachtMateriaal;
import be.miras.programs.frederik.dbo.DbOpdrachtTaak;
import be.miras.programs.frederik.dbo.DbTypeMateriaal;
import be.miras.programs.frederik.dbo.DbVooruitgang;
import be.miras.programs.frederik.dbo.DbWerknemerOpdrachtTaak;
import be.miras.programs.frederik.export.Factuur;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.model.Planning;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.model.Verplaatsing;
import be.miras.programs.frederik.model.Werkgever;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.Datum;
import be.miras.programs.frederik.util.GoogleApis;

/**
 * Servlet implementation class FacturatieDetailServlet
 */
@WebServlet("/FacturatieDetailServlet")
public class FacturatieDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FacturatieDetailServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		HttpSession session = request.getSession();
		Boolean isIngelogd =  (Boolean) session.getAttribute("isIngelogd");
		
		if (isIngelogd == null || isIngelogd == false ){
			RequestDispatcher view = request.getRequestDispatcher("/logout");
			view.forward(request, response);
		} else {
			
		
		// geselecteerde klantId;
		int klantId =  Datatype.stringNaarInt(request.getParameter("geselecteerdeKlant"));
		
		Factuur factuur = new Factuur();
		
		AdresAdapter adresAdapter = new AdresAdapter();
		DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
		DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
		DbStatusDao dbStatusDao = new DbStatusDao();
				
		
		
		//klantlijst ophalen
		List<DbKlant> klantlijst = (List<DbKlant>) session.getAttribute("klantlijst");
		
		//geselecteerde klant zoeken
		Iterator<DbKlant> klantIt = klantlijst.iterator();
		while(klantIt.hasNext()){
			DbKlant k = klantIt.next();
			if (k.getId() == klantId){
				factuur.setKlantNaam(k.geefAanspreekNaam());
				if(k.getClass().getSimpleName().equals("DbBedrijf")){
					factuur.setBtwAanrekenen(false);
				} else {
					factuur.setBtwAanrekenen(true);
				}
			}
		}
		
		//aanmaakdatum + vervaldatum instellen
		Date aanmaakDatum = new Date();
		Date vervalDatum = berekenVervalDatum(aanmaakDatum);
		
		// er is een klant-adreslijst nodig
		List<Adres> adresLijst = adresAdapter.leesWaarKlantId(klantId);
		HashMap<Integer, String> adresMap = new HashMap<Integer, String>();
		Iterator<Adres> adresLijstIterator = adresLijst.iterator();
		while(adresLijstIterator.hasNext()){
			Adres a = adresLijstIterator.next();
			int id = a.getId();
			String adresString = a.toString();
			adresMap.put(id, adresString);
		}
		
		// adresgegevens van tuinbouwbedrijf
		WerkgeverDaoAdapter werkgeverDaoAdapter = new WerkgeverDaoAdapter();
		Werkgever werkgever = (Werkgever) werkgeverDaoAdapter.lees(0);
		Adres bedrijfAdres = werkgever.getAdreslijst().get(0);
		factuur.setBedrijfsAdres(bedrijfAdres);
		factuur.setBedrijfsEmail(werkgever.getEmail());
		
		List<Verplaatsing> verplaatsingLijst = new ArrayList<Verplaatsing>();
	
		// kies opdrachten die afgewerkt zijn 
		List<Opdracht> opdrachtLijst = new ArrayList<Opdracht>();
		
		int benodigdeStatusId = dbStatusDao.lees("Afgewerkt");
		
		// dbOpdrachtLijst waar klantId = geselecteerde id;
		List<DbOpdracht> dbOpdrachtLijst = dbOpdrachtDao.leesWaarKlantId(klantId);
		
		Iterator<DbOpdracht> dbOpdrachtLijstIterator = dbOpdrachtLijst.iterator();
		while (dbOpdrachtLijstIterator.hasNext()){
			
			DbOpdracht dbOpdracht = dbOpdrachtLijstIterator.next();
			//Een opdracht is afgewerkt als alle dbOpdrachtTaken afgewerkt zijn
			boolean isAfgewerkt = true;
						
			ArrayList<DbOpdrachtTaak> dbOpdrachtTaakLijst = new ArrayList<DbOpdrachtTaak>();

			//dbOpdrachtTaakLijst van deze opdracht
			dbOpdrachtTaakLijst = (ArrayList<DbOpdrachtTaak>) dbOpdrachtTaakDao.leesLijst(dbOpdracht.getId());
			
			Iterator<DbOpdrachtTaak> dbOpdrachtTaakLijstIterator = dbOpdrachtTaakLijst.iterator();
			while(dbOpdrachtTaakLijstIterator.hasNext()){
				DbOpdrachtTaak dbOpdrachtTaak = dbOpdrachtTaakLijstIterator.next();
				DbVooruitgang dbVooruitgang = (DbVooruitgang) dbVooruitgangDao.lees(dbOpdrachtTaak.getVooruitgangId());
			
				if (dbVooruitgang.getStatusId() == benodigdeStatusId){
					
					// we hebben een dbopdracht 
					//waarvan de opdrachttaak  vooruitgang : statusId op "afgewerkt" staat.
				} else {
					// er is ten minste 1 opdracht_taak niet afgewerkt
					isAfgewerkt = false;
				}

			}
			
			if (isAfgewerkt) {
				//Een opdracht waarvan alle opdracht_taak afgewerkt zijn
				
				Opdracht opdracht = new Opdracht();
				List<Taak> taakLijst = new ArrayList<Taak>();
				
				DbTaakDao dbTaakDao = new DbTaakDao();
				DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
				DbOpdrachtMateriaalDao dbOpdrachtMateriaalDao = new DbOpdrachtMateriaalDao();
				DbMateriaalDao dbMateriaalDao = new DbMateriaalDao();
				DbTypeMateriaalDao dbTypeMateriaalDao = new DbTypeMateriaalDao();
				
				opdracht.setId(dbOpdracht.getId());
				opdracht.setOpdrachtNaam(dbOpdracht.getNaam());
				opdracht.setLatitude(dbOpdracht.getLatitude());
				opdracht.setLongitude(dbOpdracht.getLongitude());
				
				//bereken het aantal km tussen werkgever en klant;
				double aantalKm = Double.MIN_VALUE;
				double opdrachtLat = opdracht.getLatitude();
				double opdrachtLng = opdracht.getLongitude();
				Adres adresOpdracht = GoogleApis.zoekAdres(opdrachtLat, opdrachtLng);
				
				//bereken de afstand tussen het bedrijfsadres en de opdrachtAdres
				aantalKm = GoogleApis.berekenAantalKilometers(bedrijfAdres, adresOpdracht);
				
				//Haal de taaklijst op van deze opdracht
				List<DbOpdrachtTaak> afgewerktDbOpdrachtTaakLijst = new ArrayList<DbOpdrachtTaak>();
				afgewerktDbOpdrachtTaakLijst = dbOpdrachtTaakDao.leesLijst(dbOpdracht.getId());
				Iterator<DbOpdrachtTaak> afgewerktDbOpdrachtTaakLijstIterator = afgewerktDbOpdrachtTaakLijst.iterator();
				while (afgewerktDbOpdrachtTaakLijstIterator.hasNext()){
					DbOpdrachtTaak dot = afgewerktDbOpdrachtTaakLijstIterator.next();
					int taakId = dot.getTaakId();
					String taakNaam = dbTaakDao.selectNaam(taakId);
					
					Taak taak = new Taak();
					taak.setTaakNaam(taakNaam);
					taak.setOpmerking(dot.getOpmerking());
					
					//stel planningLijst op 
					List<Planning> planningLijst = new ArrayList<Planning>();
					List<DbWerknemerOpdrachtTaak> dbWerknemerOpdrachtTaakLijst = dbWerknemerOpdrachtTaakDao.leesWaarTaakId(taakId);
					
					Iterator<DbWerknemerOpdrachtTaak> dbwotlIt = dbWerknemerOpdrachtTaakLijst.iterator();
					while (dbwotlIt.hasNext()){
						DbWerknemerOpdrachtTaak dbWerknemerOpdrachtTaak = dbwotlIt.next();
						
						Planning planning = new Planning();
						planning.setWerknemerId(dbWerknemerOpdrachtTaak.getWerknemerId());
						planning.setBeginuur(dbWerknemerOpdrachtTaak.getBeginuur());
						planning.setEinduur(dbWerknemerOpdrachtTaak.getEinduur());
						planning.setAantalUren();
						
						planning.setAantalKm(aantalKm);
						planningLijst.add(planning);

					}
					
					taak.setPlanningLijst(planningLijst);
					
					taakLijst.add(taak);
					
				}
				
				opdracht.setTaakLijst(taakLijst);
				
				List<Materiaal> gebruiktMateriaalLijst = new ArrayList<Materiaal>();
				
				List<DbOpdrachtMateriaal> dbOpdrachtMateriaalLijst = dbOpdrachtMateriaalDao.leesWaarOpdrachtId(dbOpdracht.getId());
				Iterator<DbOpdrachtMateriaal> dbomlIt = dbOpdrachtMateriaalLijst.iterator();
				while (dbomlIt.hasNext()){
					DbOpdrachtMateriaal dbOpdrachtMateriaal = dbomlIt.next();
					DbMateriaal dbMateriaal = (DbMateriaal) dbMateriaalDao.lees(dbOpdrachtMateriaal.getMateriaalId());
					DbTypeMateriaal dbTypeMateriaal = (DbTypeMateriaal) dbTypeMateriaalDao.lees(dbMateriaal.getTypeMateriaalId());
					
					Materiaal materiaal = new Materiaal();
					materiaal.setNaam(dbMateriaal.getNaam());
					materiaal.setEenheidsmaat(dbMateriaal.getEenheid());
					materiaal.setSoort(dbTypeMateriaal.getNaam());
					materiaal.setHoeveelheid(dbOpdrachtMateriaal.getVerbruik());
					materiaal.setEenheidsprijs(dbMateriaal.getEenheidsprijs());
					
					gebruiktMateriaalLijst.add(materiaal);
				}
				opdracht.setGebruiktMateriaalLijst(gebruiktMateriaalLijst);
					
				opdrachtLijst.add(opdracht);
				
				// bereken verplaatsingskosten
				Iterator<Taak> verplaatsingTaakIt = taakLijst.iterator();
				while (verplaatsingTaakIt.hasNext()){
					Taak verplaatsingTaak = verplaatsingTaakIt.next();
					List<Planning> verplaatsingPlanningLijst = verplaatsingTaak.getPlanningLijst();
					Iterator<Planning> verplaatsingPlanningIt = verplaatsingPlanningLijst.iterator();
					while (verplaatsingPlanningIt.hasNext()){
						Planning verplaatsingPlanning = verplaatsingPlanningIt.next();
						Date dag = new Date();
						dag.setDate(verplaatsingPlanning.getBeginuur().getDate());
						dag.setMonth(verplaatsingPlanning.getBeginuur().getMonth());
						dag.setYear(verplaatsingPlanning.getBeginuur().getYear());
						int werknemerId = verplaatsingPlanning.getWerknemerId();
						int opdrachtId = opdracht.getId();
						// int aantalKm

						/*
						 * controleer of deze werknemer op deze dag zich nog
						 * niet reeds naar deze locatie verplaatst heeft.
						 */
						boolean reedsVerplaatst = false;
						Iterator<Verplaatsing> verplaatsingIt = verplaatsingLijst.iterator();
						while(verplaatsingIt.hasNext()){
							Verplaatsing verplaatsing = verplaatsingIt.next();
							
							if (verplaatsing.getDag().getDate() == dag.getDate()
									&& verplaatsing.getDag().getMonth() == dag.getMonth()
									&& verplaatsing.getDag().getYear() == dag.getYear()
									&& verplaatsing.getWerknemerId() == werknemerId
									&& verplaatsing.getOpdrachtId() == opdrachtId){
								reedsVerplaatst = true;
								int aantal = verplaatsing.getAantalVerplaatsingen() + 2;
								verplaatsing.setAantalVerplaatsingen(aantal);
							}	
						}
						if (!reedsVerplaatst){
							Verplaatsing nieuweVerplaatsing = new Verplaatsing();
							nieuweVerplaatsing.setDag(dag);
							nieuweVerplaatsing.setWerknemerId(werknemerId);
							nieuweVerplaatsing.setOpdrachtId(opdrachtId);
							nieuweVerplaatsing.setAantalKm(aantalKm);
							nieuweVerplaatsing.setAantalVerplaatsingen(2);
							verplaatsingLijst.add(nieuweVerplaatsing);
						}
					}
				}
				
			}
			
		}
		// er is een lijst met alle verplaatsingen appart
		// een lijst met alle verplaatsingen per werknemer is nodig
		List<Verplaatsing> teFacturerenVerplaatsingLijst = new ArrayList<Verplaatsing>();
		
		Iterator<Verplaatsing> verplaatsingLijstIt = verplaatsingLijst.iterator();
		while (verplaatsingLijstIt.hasNext()){
			Verplaatsing verplaatsing = verplaatsingLijstIt.next();
			boolean verplaatsingGevonden = false;
			
			ListIterator<Verplaatsing> teFacturerenVerplaatsingIt = teFacturerenVerplaatsingLijst.listIterator();
			while (teFacturerenVerplaatsingIt.hasNext()){
				Verplaatsing teFacturerenVerplaatsing = teFacturerenVerplaatsingIt.next();
				if( teFacturerenVerplaatsing.getOpdrachtId() == verplaatsing.getOpdrachtId()
						&& teFacturerenVerplaatsing.getDag().getDate() == verplaatsing.getDag().getDate()
						&& teFacturerenVerplaatsing.getDag().getMonth() == verplaatsing.getDag().getMonth()
						&& teFacturerenVerplaatsing.getDag().getYear() == verplaatsing.getDag().getYear()
						){
					verplaatsingGevonden = true;
					int aantalVerplaatsingen = teFacturerenVerplaatsing.getAantalVerplaatsingen();
					aantalVerplaatsingen += verplaatsing.getAantalVerplaatsingen();
					teFacturerenVerplaatsing.setAantalVerplaatsingen(aantalVerplaatsingen);
		
				}
			}
		
			if (!verplaatsingGevonden){
				Verplaatsing nieuweVerplaatsing = new Verplaatsing();
				nieuweVerplaatsing.setDag(verplaatsing.getDag());
				nieuweVerplaatsing.setOpdrachtId(verplaatsing.getOpdrachtId());
				nieuweVerplaatsing.setAantalKm(verplaatsing.getAantalKm());
				nieuweVerplaatsing.setAantalVerplaatsingen(2);
				teFacturerenVerplaatsingLijst.add(nieuweVerplaatsing);
				
			}
		}
		factuur.setVerplaatsingLijst(teFacturerenVerplaatsingLijst);		
		
		factuur.setAanmaakDatum(aanmaakDatum);
		factuur.setVervalDatum(vervalDatum);
		factuur.setOpdrachtLijst(opdrachtLijst);
		
		
	
		
		session.setAttribute("adresMap", adresMap);
		session.setAttribute("factuur", factuur);
		// button : genereer PDF
		// markeer de opdracht als 'gefactureerd' (Vooruitgang.statusId
		
		RequestDispatcher view = request.getRequestDispatcher("/FacturatieDetail.jsp");
		view.forward(request, response);
		}
	}

	private Date berekenVervalDatum(Date aanmaakDatum) {
		Date datum = new Date();
		datum.setDate(aanmaakDatum.getDate());
		if (aanmaakDatum.getMonth() < 12){
			datum.setMonth(aanmaakDatum.getMonth());
			datum.setYear(aanmaakDatum.getYear());
		} else {
			datum.setMonth(0);
			int jaar = aanmaakDatum.getYear();
			datum.setYear(jaar++);
		}
		return datum;
	}

}

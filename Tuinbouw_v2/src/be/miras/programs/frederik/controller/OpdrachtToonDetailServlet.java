package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.adapter.AdresDaoAdapter;
import be.miras.programs.frederik.dao.adapter.MateriaalDaoAdapter;
import be.miras.programs.frederik.dao.adapter.TaakDaoAdapter;
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
import be.miras.programs.frederik.util.Datatype;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class OpdrachtToonDetailServlet
 */
@WebServlet("/OpdrachtToonDetailServlet")
public class OpdrachtToonDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OpdrachtToonDetailServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		int id = Datatype.stringNaarInt(request.getParameter("id"));

		HttpSession session = request.getSession();
		
		// bovenaan de content wordt de aanspreektitel van de
		// opdrachtgever weergegeven
		String aanspreeknaam = null;
		// buttonNaam = "Voeg toe" of "Wijzigingen opslaan"
		String buttonNaam = null;
		// variabelveld = bij een nieuwe opdracht leeg
		// variabelveld = bij een bestaande opdracht ". Opdrachtgever wijzigen:
		// ";
		String variabelveld1 = " ";
		String variabelveld2 = " ";
		String adresString;
		String staticmap = null;
		String googlemap = null;

		Opdracht opdracht = new Opdracht();

		Map<Integer, String> adresMap = new HashMap<Integer, String>();

		// klantlijst ophalen
		DbKlantDao dbKlantDao = new DbKlantDao();
		DbKlant dbKlant = null;
		adresString = null;

		// maak een lijst met alle klanten met hun aanspreeknaam
		// op het scherm OpdrachtDedail.jsp wodt een keuzemenu samengesteld
		// waardoor
		// men een reeds bestaande klant kan kiezen
		Map<Integer, String> klantNaamMap = new HashMap<Integer, String>();

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
		    
		if (id < 0) {
			// het gaat om de aanmaak van een nieuwe opdracht
			aanspreeknaam = " ";
			buttonNaam = "Voeg toe";
			opdracht.setId(Integer.MIN_VALUE);

		} else {
			DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
			AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();

			// het gaat om het wijzigen van een bestaande opdracht
			variabelveld1 = ". Opdrachtgever wijzigen: ";
			variabelveld2 = ", wijzigen :";

			// zoek de opdracht aan de hand van de opdrachtId
			DbOpdracht dbOpdracht = (DbOpdracht) dbOpdrachtDao.lees(id);
			
			opdracht.setId(id);
			opdracht.setKlantId(dbOpdracht.getKlantAdresId());
			opdracht.setKlantAdresId(dbOpdracht.getKlantAdresId());
			opdracht.setOpdrachtNaam(dbOpdracht.getNaam());
			opdracht.setStartDatum(dbOpdracht.getStartdatum());
			opdracht.setEindDatum(dbOpdracht.getEinddatum());
			
			//aanspreeknaam definiëren
			dbKlant = (DbKlant) dbKlantDao.lees(dbOpdracht.getKlantId());
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
			
			opdracht.setKlantNaam(naam);
			
			aanspreeknaam = "voor ";
			aanspreeknaam = aanspreeknaam.concat(naam);
			buttonNaam = "Wijziging opslaan";

			// adreslijst die bij de opdrachtgever van deze opdracht hoort
			// ophalen.
			List<Adres> adresLijst = adresDaoAdapter.leesWaarKlantId(dbOpdracht.getKlantId());

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
		}

		taakLeeslijst(id, opdracht);

		// lijst met alle materialen ophalen
		materiaalLijst = (List<Materiaal>) (Object) materiaalDaoAdapter.leesAlle();

		// lijst met de gebruikteMaterialen van deze opdracht ophalen
		gebruikteMaterialenLijst = materiaalDaoAdapter.leesOpdrachtMateriaal(opdracht.getId());
		opdracht.setGebruiktMateriaalLijst(gebruikteMaterialenLijst);

		OpdrachtDetailData opdrachtDetailData = new OpdrachtDetailData(aanspreeknaam, variabelveld1, variabelveld2,
				buttonNaam, opdracht, klantNaamMap, adresString, adresMap, materiaalLijst, staticmap, googlemap);

		request.setAttribute("opdrachtDetailData", opdrachtDetailData);
		session.setAttribute("id", id);

		RequestDispatcher view = request.getRequestDispatcher("/OpdrachtDetail.jsp");
		view.forward(request, response);
	}

	/**
	 * @param opdrachtId int
	 * @param opdracht int
	 * 
	 *  voegt een takenlijst toe aan de opdracht
	 */
	private void taakLeeslijst(int opdrachtId, Opdracht opdracht) {
		// lees de takenlijst die bij deze opdracht hoort.
		TaakDaoAdapter taakDaoAdapter = new TaakDaoAdapter();

		List<Taak> taakLijst = (List<Taak>) (Object) taakDaoAdapter.leesAlle(opdrachtId);

		opdracht.setTaakLijst(taakLijst);
	}

	
}

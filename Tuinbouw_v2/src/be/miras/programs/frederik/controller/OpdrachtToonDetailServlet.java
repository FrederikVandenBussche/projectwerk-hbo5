package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbKlantAdresDao;
import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.adapter.AdresAdapter;
import be.miras.programs.frederik.dao.adapter.MateriaalDaoAdapter;
import be.miras.programs.frederik.dao.adapter.TaakDaoAdapter;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.GoogleApis;
import be.miras.programs.frederik.util.Datatype;

/**
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
		ArrayList<Opdracht> opdrachtLijst = (ArrayList<Opdracht>) session.getAttribute("lijst");

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

		Opdracht opdracht = null;

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

		if (id < 0) {
			// het gaat om de aanmaak van een nieuwe opdracht

			opdracht = new Opdracht();
			aanspreeknaam = " ";
			buttonNaam = "Voeg toe";
			opdracht.setId(Integer.MIN_VALUE);

		} else {
			DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
			AdresAdapter adresAdapter = new AdresAdapter();
			GoogleApis googleApis = new GoogleApis();

			// het gaat om het wijzigen van een bestaande opdracht
			variabelveld1 = ". Opdrachtgever wijzigen: ";
			variabelveld2 = ", wijzigen :";

			// zoek de opdracht aan de hand van de opdrachtId
			Iterator<Opdracht> iter = opdrachtLijst.iterator();
			while (iter.hasNext()) {
				Opdracht o = iter.next();
				if (o.getId() == id) {
					opdracht = o;
				}
			}

			aanspreeknaam = "voor ";
			aanspreeknaam = aanspreeknaam.concat(opdracht.getKlantNaam());
			buttonNaam = "Wijziging opslaan";

			// adreslijst die bij de opdrachtgever van deze opdracht hoort
			// ophalen.

			// aanmaak van adresMap<adresId, adresString>
			List<Integer> adresIdLijst = dbKlantAdresDao.leesLijst(opdracht.getKlantId());

			Iterator<Integer> adresIdIter = adresIdLijst.iterator();
			while (adresIdIter.hasNext()) {
				int adresId = adresIdIter.next();
				Adres adres = (Adres) adresAdapter.lees(adresId);
				adresMap.put(adresId, adres.toString());
			}

			double lat = opdracht.getLatitude();
			double lng = opdracht.getLongitude();
			Adres adres = GoogleApis.zoekAdres(lat, lng);
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

		session.setAttribute("opdrachtDetailData", opdrachtDetailData);
		request.setAttribute("id", id);

		RequestDispatcher view = request.getRequestDispatcher("/OpdrachtDetail.jsp");
		view.forward(request, response);
	}

	private void taakLeeslijst(int opdrachtId, Opdracht opdracht) {
		// lees de takenlijst die bij deze opdracht hoort.
		TaakDaoAdapter taakDaoAdapter = new TaakDaoAdapter();

		List<Taak> taakLijst = (List<Taak>) (Object) taakDaoAdapter.leesAlle(opdrachtId);

		opdracht.setTaakLijst(taakLijst);
	}

}

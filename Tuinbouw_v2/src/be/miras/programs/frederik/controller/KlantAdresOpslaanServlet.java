package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbAdresDao;
import be.miras.programs.frederik.dao.DbGemeenteDao;
import be.miras.programs.frederik.dao.DbKlantAdresDao;
import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.DbStraatDao;
import be.miras.programs.frederik.dao.adapter.AdresDaoAdapter;
import be.miras.programs.frederik.dbo.DbAdres;
import be.miras.programs.frederik.dbo.DbBedrijf;
import be.miras.programs.frederik.dbo.DbGemeente;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbKlantAdres;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbParticulier;
import be.miras.programs.frederik.dbo.DbStraat;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.GoogleApis;
import be.miras.programs.frederik.util.InputValidatieStrings;
import be.miras.programs.frederik.util.InputValidatie;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class KlantAdresOpslaanServlet
 */
@WebServlet("/KlantAdresOpslaanServlet")
public class KlantAdresOpslaanServlet extends HttpServlet implements IinputValidatie{
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KlantAdresOpslaanServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String aanspreeknaam = request.getParameter("aanspreeknaam");
		String variabelVeldnaam1 = request.getParameter("variabelVeldnaam1");
		String variabelVeld1 = request.getParameter("variabelVeld1");
		String variabelVeldnaam2 = request.getParameter("variabelVeldnaam2");
		String variabelVeld2 = request.getParameter("variabelVeld2");
		int id = Datatype.stringNaarInt(request.getParameter("klant_id"));
		String straat = request.getParameter("straat").trim();
		String nummerString = request.getParameter("nr").trim();
		String bus = request.getParameter("bus").trim();
		String postcodeString = request.getParameter("postcode").trim();
		String plaats = request.getParameter("plaats").trim();
		
		String inputValidatieErrorMsg = inputValidatie(
				new String[]{straat, nummerString, postcodeString, plaats});
		
		HttpSession session = request.getSession();
		String type = (String) session.getAttribute("type");
		
		if (inputValidatieErrorMsg.isEmpty()) {
			
			int nummer = Datatype.stringNaarInt(nummerString);
			int postcode = Datatype.stringNaarInt(postcodeString);

			DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
			DbAdresDao dbAdresDao = new DbAdresDao();
			DbStraatDao dbStraatDao = new DbStraatDao();
			DbGemeenteDao dbGemeenteDao = new DbGemeenteDao();
			DbAdres dbAdres = new DbAdres();
			DbKlantAdres dbKlantAdres = new DbKlantAdres();

			Adres adres = new Adres();

			int gemeenteId = dbGemeenteDao.geefIdVan(postcode, plaats);
			
			if (gemeenteId < 0) {
				DbGemeente gemeente = new DbGemeente();
				gemeente.setNaam(plaats);
				gemeente.setPostcode(postcode);
				dbGemeenteDao.voegToe(gemeente);
				gemeenteId = dbGemeenteDao.geefIdVan(postcode, plaats);
			}
			
			int straatId = dbStraatDao.geefIdVan(straat);
			
			if (straatId < 0) {
				DbStraat dbStraat = new DbStraat();
				dbStraat.setNaam(straat);
				dbStraatDao.voegToe(dbStraat);
				straatId = dbStraatDao.geefIdVan(straat);
			}

			dbAdres.setStraatId(straatId);
			dbAdres.setGemeenteId(gemeenteId);
			dbAdres.setHuisnummer(nummer);
			dbAdres.setBus(bus);
			int adresId = dbAdresDao.voegToe(dbAdres);

			dbKlantAdres.setKlantId(id);
			dbKlantAdres.setAdresId(adresId);
			dbKlantAdresDao.voegToe(dbKlantAdres);

			adres.setId(adresId);
			adres.setStraat(straat);
			adres.setNummer(nummer);
			adres.setBus(bus);
			adres.setPostcode(postcode);
			adres.setPlaats(plaats);
			adres.setPersoonId(id);

			String staticmap = GoogleApis.urlBuilderStaticMap(adres);
			adres.setStaticmap(staticmap);

			String googlemap = GoogleApis.urlBuilderGoogleMaps(adres);
			adres.setGooglemap(googlemap);
		}else {
			
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
		}
		
		// de klantDetails terug ophalen
		DbKlant klant = null;
		ArrayList<DbOpdracht> opdrachtLijst = null;
		HashMap<Integer, String> opdrachtMap = null;

		if (type.equals("particulier")) {
			
			klant = new DbParticulier();
			variabelVeldnaam1 = "Voornaam";
			variabelVeldnaam2 = "Naam";
		} else if (type.equals("bedrijf")) {
			
			klant = new DbBedrijf();
			variabelVeldnaam1 = "Naam";
			variabelVeldnaam2 = "Btw nummer";
		} else {
			// Er is geen type klant gedefinieerd
		}
		
		// het gaat niet om een nieuwe klant.
		DbKlantDao dbKlantDao = new DbKlantDao();
		
		// de klant met de corresponderende id opzoeken.
		if (type.equals("particulier")) {
			
			klant = dbKlantDao.leesParticulier(id);
			
			variabelVeld1 = ((DbParticulier) klant).getVoornaam();
			variabelVeld2 = ((DbParticulier) klant).getNaam();
			aanspreeknaam = variabelVeld1.concat(" ").concat(variabelVeld2);	
		} else if (type.equals("bedrijf")) {
			
			klant = dbKlantDao.leesBedrijf(id);
			
			variabelVeld1 = ((DbBedrijf) klant).getBedrijfnaam();
			variabelVeld2 = ((DbBedrijf) klant).getBtwNummer();
			aanspreeknaam = variabelVeld1;
		}

		/*
		 * de adreslijst van deze klant ophalen
		 */
		AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();
		ArrayList<Adres> adreslijst = (ArrayList<Adres>) adresDaoAdapter.leesWaarKlantId(id);

		ListIterator<Adres> adresLijstIt = adreslijst.listIterator();
		while (adresLijstIt.hasNext()) {
			Adres adres = adresLijstIt.next();

			String staticmap = GoogleApis.urlBuilderStaticMap(adres);
			adres.setStaticmap(staticmap);

			String googlemap = GoogleApis.urlBuilderGoogleMaps(adres);
			adres.setGooglemap(googlemap);
		}

		klant.setAdreslijst(adreslijst);
		
		// de opdrachtenlijst van deze klant ophalen
		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
		opdrachtLijst = (ArrayList<DbOpdracht>) dbOpdrachtDao.leesWaarKlantId(id);

		opdrachtMap = new HashMap<Integer, String>();
		Iterator<DbOpdracht> dbOpdrachtIt = opdrachtLijst.iterator();
		while (dbOpdrachtIt.hasNext()) {
			DbOpdracht opdracht = dbOpdrachtIt.next();

			int opdrachtId = opdracht.getId();
			String opdrachtNaam = opdracht.getNaam();

			opdrachtMap.put(opdrachtId, opdrachtNaam);
		}
		
		request.setAttribute("tabKiezer", "adres");
		request.setAttribute("id", id); // klant_id
		request.setAttribute("aanspreeknaam", aanspreeknaam);
		request.setAttribute("variabelVeld1", variabelVeld1);
		request.setAttribute("variabelVeldnaam1", variabelVeldnaam1);
		request.setAttribute("variabelVeld2", variabelVeld2);
		request.setAttribute("variabelVeldnaam2", variabelVeldnaam2);
		request.setAttribute("klant", klant);
		request.setAttribute("opdrachtMap", opdrachtMap);
		
		RequestDispatcher view = request.getRequestDispatcher("/KlantDetail.jsp");
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		RequestDispatcher view = request.getRequestDispatcher("/logout");
		view.forward(request, response);
	}

	@Override
	public String inputValidatie(String[] teValideren) {
		String straat = teValideren[0];
		String nummerString = teValideren[1];
		String postcodeString = teValideren[2];
		String plaats = teValideren[3];
		
		String inputValidatieErrorMsg = "";
		
		String msg = null;
		
		msg = InputValidatie.enkelAlfabetisch(straat);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Straat).concat(msg);
		}
		
		msg = InputValidatie.geheelGetal(nummerString);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Huisnummer).concat(msg);
		}
		
		msg = InputValidatie.geheelGetal(postcodeString);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Postcode).concat(msg);
		}
		
		msg = InputValidatie.enkelAlfabetisch(plaats);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Plaats).concat(msg);
		}

		return inputValidatieErrorMsg;
	}

	
}

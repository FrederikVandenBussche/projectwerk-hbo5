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
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbParticulier;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.GoogleApis;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class KlantAdresVerwijderenServlet
 */
@WebServlet("/KlantAdresVerwijderenServlet")
public class KlantAdresVerwijderenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KlantAdresVerwijderenServlet() {
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
		int adresId = Datatype.stringNaarInt(request.getParameter("adres_id"));

		HttpSession session = request.getSession();
		int klantId = (int) session.getAttribute("klantId");
		String type = (String) session.getAttribute("type");
		
		DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
		
		int klantAdresId= dbKlantAdresDao.geefId(klantId, adresId);
		boolean isKomtVoor = dbOpdrachtDao.isKlantAdresKomtVoor(klantAdresId);
		
		if (isKomtVoor){
			String errorMsg = "Kan dit adres niet verwijderen omdat er nog opdrachten op dit adres uit te voeren zijn.";
			request.setAttribute("inputValidatieErrorMsg", errorMsg);
		} else {
			// adres verwijderen
			adresVerwijderen(klantId, adresId);
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
					
					klant = dbKlantDao.leesParticulier(klantId);
					
					variabelVeld1 = ((DbParticulier) klant).getVoornaam();
					variabelVeld2 = ((DbParticulier) klant).getNaam();
					aanspreeknaam = variabelVeld1.concat(" ").concat(variabelVeld2);
					
					
				} else if (type.equals("bedrijf")) {
					
					klant = dbKlantDao.leesBedrijf(klantId);
					
					variabelVeld1 = ((DbBedrijf) klant).getBedrijfnaam();
					variabelVeld2 = ((DbBedrijf) klant).getBtwNummer();
					aanspreeknaam = variabelVeld1;
					
				}

				/*
				 * de adreslijst van deze klant ophalen
				 */
				AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();
				ArrayList<Adres> adreslijst = (ArrayList<Adres>) adresDaoAdapter.leesWaarKlantId(klantId);

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
				opdrachtLijst = (ArrayList<DbOpdracht>) dbOpdrachtDao.leesWaarKlantId(klantId);

				opdrachtMap = new HashMap<Integer, String>();
				Iterator<DbOpdracht> dbOpdrachtIt = opdrachtLijst.iterator();
				while (dbOpdrachtIt.hasNext()) {
					DbOpdracht opdracht = dbOpdrachtIt.next();

					int opdrachtId = opdracht.getId();
					String opdrachtNaam = opdracht.getNaam();
					opdrachtMap.put(opdrachtId, opdrachtNaam);
				}
		
		request.setAttribute("tabKiezer", "adres");
		request.setAttribute("aanspreeknaam", aanspreeknaam);
		request.setAttribute("variabelVeldnaam1", variabelVeldnaam1);
		request.setAttribute("variabelVeld1", variabelVeld1);
		request.setAttribute("variabelVeldnaam2", variabelVeldnaam2);
		request.setAttribute("variabelVeld2", variabelVeld2);
		request.setAttribute("klant", klant);
		request.setAttribute("opdrachtMap", opdrachtMap);

		RequestDispatcher view = request.getRequestDispatcher("/KlantDetail.jsp");
		
		view.forward(request, response);

	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		RequestDispatcher view = request.getRequestDispatcher("/logout");
		view.forward(request, response);
	}
	

	/**
	 * @param klantId int
	 * @param adresId int
	 * 
	 * verwijderd het adres met een bepaalde klantId en adresId uit de databank
	 */
	private void adresVerwijderen(int klantId, int adresId) {
		DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
		DbAdresDao dbAdresdao = new DbAdresDao();

		// klantAdres verwijderen
		dbKlantAdresDao.verwijder(klantId, adresId);

		// straatId en gemeenteId ophalen
		DbAdres dbadres = (DbAdres) dbAdresdao.lees(adresId);
		int straatId = dbadres.getStraatId();
		int gemeenteId = dbadres.getGemeenteId();

		// delete dbAdres
		dbAdresdao.verwijder(adresId);

		// indien de straat nergens anders gebruikt wordt.
		// deze uit de db verwijderen
		boolean straatInGebruik = dbAdresdao.isStraatInGebruik(straatId);

		if (!straatInGebruik) {
			DbStraatDao dbStraatDao = new DbStraatDao();

			dbStraatDao.verwijder(straatId);

		}
		// indien de gemeente nergens anders gebruikt wordt
		// deze uit de db verwijderen
		boolean gemeenteInGebruik = dbAdresdao.isGemeenteInGebruik(gemeenteId);

		if (!gemeenteInGebruik) {
			DbGemeenteDao dbGemeenteDao = new DbGemeenteDao();
			dbGemeenteDao.verwijder(gemeenteId);
		}
	}

}

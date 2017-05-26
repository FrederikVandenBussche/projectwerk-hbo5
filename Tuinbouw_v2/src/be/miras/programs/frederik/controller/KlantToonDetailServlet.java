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

import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.adapter.AdresDaoAdapter;
import be.miras.programs.frederik.dbo.DbBedrijf;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbParticulier;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.util.GoogleApis;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class KlantParticulierToonDetailsServlet
 */
@WebServlet("/KlantParticulierToonDetailsServlet")
public class KlantToonDetailServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KlantToonDetailServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		RequestDispatcher view = null;

		HttpSession session = request.getSession();
		Boolean isIngelogd = (Boolean) session.getAttribute("isIngelogd");

		if (isIngelogd == null || isIngelogd == false) {
			view = request.getRequestDispatcher("/logout");

		} else {

			int id = Integer.parseInt(request.getParameter("id"));
			String type = request.getParameter("type");

			// bovenaan de content wordt de aanspreeknaam van een personeelslid
			// weergegeven.
			String aanspreeknaam = null;
			//
			/*
			 * bij een particuliere klant wordt de 'naam' en 'voornaam'
			 * weergegeven bij een bedrijf klant wordt de 'bedrijfnaam' en
			 * 'BTWnummer' weergegeven
			 * 
			 * Om dit weer te geven wordt er gebruik gemaakt van 2 variabele
			 * velden en daarbij horen 2 variabele veldnamen.
			 */
			String variabelVeldnaam1 = null;
			String variabelVeldnaam2 = null;
			String variabelVeld1 = null;
			String variabelVeld2 = null;

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
			if (id == -1) {
				/*
				 * het gaat om een nieuwe klant. De aanspreeknaam van een klant
				 * word weergegeven bovenaan de content van 'KlantDetail.jsp'.
				 * Daarom stellen we nu de aanspreeknaam van het personeelslid
				 * tijdelijk in.
				 */
				aanspreeknaam = "";

			} else {
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
			}

			session.setAttribute("aanspreeknaam", aanspreeknaam);
			session.setAttribute("id", id);
			session.setAttribute("variabelVeldnaam1", variabelVeldnaam1);
			session.setAttribute("variabelVeldnaam2", variabelVeldnaam2);
			session.setAttribute("variabelVeld1", variabelVeld1);
			session.setAttribute("variabelVeld2", variabelVeld2);
			session.setAttribute("klantId", klant.getId());
			session.setAttribute("type", type);
			
			request.setAttribute("klant", klant);
			request.setAttribute("opdrachtMap", opdrachtMap);

			view = this.getServletContext().getRequestDispatcher("/KlantDetail.jsp");
		}
		
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	
}

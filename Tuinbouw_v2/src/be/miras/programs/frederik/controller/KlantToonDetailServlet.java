package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
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

import be.miras.programs.frederik.dao.DbAdresDao;
import be.miras.programs.frederik.dao.DbGemeenteDao;
import be.miras.programs.frederik.dao.DbKlantAdresDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.DbStraatDao;
import be.miras.programs.frederik.dbo.DbAdres;
import be.miras.programs.frederik.dbo.DbBedrijf;
import be.miras.programs.frederik.dbo.DbGemeente;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbParticulier;
import be.miras.programs.frederik.dbo.DbStraat;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.GoogleApis;

/**
 * Servlet implementation class KlantParticulierToonDetailsServlet
 */
@WebServlet("/KlantParticulierToonDetailsServlet")
public class KlantToonDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "KlantToonDatailServlet: ";

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

			int id = Datatype.stringNaarInt(request.getParameter("id"));

			// bovenaan de content wordt de aanspreeknaam van een personeelslid
			// weergegevne.
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

			if (request.getParameter("particulier") != null) {
				klant = new DbParticulier();
				variabelVeldnaam1 = "Voornaam";
				variabelVeldnaam2 = "Naam";

			} else if (request.getParameter("bedrijf") != null) {
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
				aanspreeknaam = "een nieuwe klant";

			} else {
				// het gaat niet om een nieuwe klant.
				// de lijst met klanten uit de session halen
				ArrayList<DbParticulier> particulierLijst = (ArrayList<DbParticulier>) session
						.getAttribute("particulierLijst");
				ArrayList<DbBedrijf> bedrijfLijst = (ArrayList<DbBedrijf>) session.getAttribute("bedrijfLijst");

				// de klant met de corresponderende id opzoeken.
				if (klant.getClass().getSimpleName().equals("DbParticulier")) {
					Iterator<DbParticulier> it = particulierLijst.iterator();
					while (it.hasNext()) {
						DbParticulier particulier = it.next();
						if (particulier.getId() == id) {
							klant = particulier;

							variabelVeld1 = ((DbParticulier) klant).getVoornaam();
							variabelVeld2 = ((DbParticulier) klant).getNaam();
							aanspreeknaam = variabelVeld1.concat(" ").concat(variabelVeld2);
						}
					}
				} else if (klant.getClass().getSimpleName().equals("DbBedrijf")) {
					Iterator<DbBedrijf> iterator = bedrijfLijst.iterator();
					while (iterator.hasNext()) {
						DbBedrijf bedrijf = iterator.next();
						if (bedrijf.getId() == id) {
							klant = bedrijf;

							variabelVeld1 = ((DbBedrijf) klant).getBedrijfnaam();
							variabelVeld2 = ((DbBedrijf) klant).getBtwNummer();
							aanspreeknaam = variabelVeld1;
						}
					}
				}

				/*
				 * de adreslijst van deze klant ophalen
				 */
				ArrayList<Adres> adreslijst = haalAdresLijstOp(id);

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

					System.out.println(TAG + "opdrachtId voor deze klant= " + opdrachtId);
				}

			}

			session.setAttribute("aanspreeknaam", aanspreeknaam);
			session.setAttribute("id", id);
			session.setAttribute("variabelVeldnaam1", variabelVeldnaam1);
			session.setAttribute("variabelVeldnaam2", variabelVeldnaam2);
			session.setAttribute("variabelVeld1", variabelVeld1);
			session.setAttribute("variabelVeld2", variabelVeld2);

			session.setAttribute("klant", klant);
			session.setAttribute("opdrachtLijst", opdrachtLijst);
			session.setAttribute("opdrachtMap", opdrachtMap);

			view = this.getServletContext().getRequestDispatcher("/KlantDetail.jsp");

		}
		view.forward(request, response);
	}

	private ArrayList<Adres> haalAdresLijstOp(int id) {

		ArrayList<Adres> adreslijst = new ArrayList<Adres>();

		DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
		DbAdresDao dbAdresDao = new DbAdresDao();
		DbGemeenteDao dbGemeenteDao = new DbGemeenteDao();
		DbStraatDao dbStraatDao = new DbStraatDao();

		List<Integer> adresIdLijst = new ArrayList<Integer>();

		adresIdLijst = dbKlantAdresDao.leesLijst(id);

		Iterator<Integer> it = adresIdLijst.iterator();
		while (it.hasNext()) {
			int adresId = it.next();

			DbAdres dbAdres = (DbAdres) dbAdresDao.lees(adresId);
			int gemeenteId = dbAdres.getGemeenteId();
			DbGemeente dbGemeente = (DbGemeente) dbGemeenteDao.lees(gemeenteId);
			int straatId = dbAdres.getStraatId();
			DbStraat dbStraat = (DbStraat) dbStraatDao.lees(straatId);

			Adres adres = new Adres();
			adres.setId(dbAdres.getId());
			adres.setStraat(dbStraat.getNaam());
			adres.setNummer(dbAdres.getHuisnummer());
			adres.setBus(dbAdres.getBus());
			adres.setPostcode(dbGemeente.getPostcode());
			adres.setPlaats(dbGemeente.getNaam());

			adreslijst.add(adres);

		}

		return adreslijst;
	}

}

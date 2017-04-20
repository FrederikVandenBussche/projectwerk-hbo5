package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import be.miras.programs.frederik.dbo.DbBedrijf;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbParticulier;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class KlantOpslaanServlet
 */
@WebServlet("/KlantOpslaanServlet")
public class KlantOpslaanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "KlantOpslaanServlet: ";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KlantOpslaanServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		int id = Datatype.stringNaarInt(request.getParameter("id"));

		String variabelVeld1 = request.getParameter("variabelVeld1");
		String variabelVeld2 = request.getParameter("variabelVeld2");

		DbKlantDao dbKlantDao = new DbKlantDao();

		HttpSession session = request.getSession();

		ArrayList<DbParticulier> particulierLijst = (ArrayList<DbParticulier>) session.getAttribute("particulierLijst");
		ArrayList<DbBedrijf> bedrijfLijst = (ArrayList<DbBedrijf>) session.getAttribute("bedrijfLijst");

		DbKlant klant = null;

		System.out.println(TAG + "");
		System.out.println(TAG + "id: " + id);

		// instelling soort van klant
		if (id < 0) {
			if (request.getParameter("variabelVeldnaam1").equals("Voornaam")) {
				klant = new DbParticulier();
			} else if (request.getParameter("variabelVeldnaam1").equals("Naam")) {
				klant = new DbBedrijf();
			}
		} else {
			// de klant met de corresponderende id opzoeken.
			Iterator<DbParticulier> it = particulierLijst.iterator();
			while (it.hasNext()) {
				DbParticulier particulier = it.next();
				if (particulier.getId() == id) {
					klant = new DbParticulier();
					klant = particulier;
				}
			}
			// de klant met de corresponderende id opzoeken.
			Iterator<DbBedrijf> iterator = bedrijfLijst.iterator();
			while (iterator.hasNext()) {
				DbBedrijf bedrijf = iterator.next();
				if (bedrijf.getId() == id) {
					klant = new DbBedrijf();
					klant = bedrijf;
				}
			}
			klant.setId(id);
		}

		// de 2 variabelen uit de veriabelevelden vastleggen
		if (klant.getClass().getSimpleName().equals("DbParticulier")) {
			((DbParticulier) klant).setVoornaam(variabelVeld1);
			((DbParticulier) klant).setNaam(variabelVeld2);
		} else if (klant.getClass().getSimpleName().equals("DbBedrijf")) {
			((DbBedrijf) klant).setBedrijfnaam(variabelVeld1);
			((DbBedrijf) klant).setBtwNummer(variabelVeld2);
		}

		// wijzigingen aanbrengen in de databijs en de sessionlijsten
		if (id < 0) {
			// nieuw Klant toevoegen
			dbKlantDao.voegToe(klant);
			id = dbKlantDao.zoekMakId();
			klant.setId(id);
			if (klant.getClass().getSimpleName().equals("DbParticulier")) {
				// dbKlantDao.voegToePartiulier((DbParticulier) klant);
				particulierLijst.add((DbParticulier) klant);
			} else if (klant.getClass().getSimpleName().equals("DbBedrijf")) {
				// dbKlantDao.voegToeBedrijf((DbBedrijf) klant);
				bedrijfLijst.add((DbBedrijf) klant);

			}
		} else { // ( id !< 0)
			// een bestaande Klant wijzigen
			dbKlantDao.wijzig(klant);
			if (klant.getClass().getSimpleName().equals("DbParticulier")) {
				ListIterator<DbParticulier> it = particulierLijst.listIterator();
				while (it.hasNext()) {
					DbParticulier dbParticulier = it.next();
					if (dbParticulier.getId() == id) {
						if (dbParticulier.isVerschillend(klant, dbParticulier)) {
							klant.setId(id);
							// dbKlantDao.wijzigParticulier((DbParticulier)
							// klant);
							it.set((DbParticulier) klant);
						}
					}
				}
			} else if (klant.getClass().getSimpleName().equals("DbBedrijf")) {

				ListIterator<DbBedrijf> it = bedrijfLijst.listIterator();
				while (it.hasNext()) {
					DbBedrijf bedrijf = it.next();
					if (bedrijf.getId() == id) {
						if (bedrijf.isVerschillend(klant, bedrijf)) {
							klant.setId(id);
							// dbKlantDao.wijzigBedrijf((DbBedrijf) klant);
							it.set((DbBedrijf) klant);
						}
					}

				}
			}
		}

		session.setAttribute("particulierLijst", particulierLijst);
		session.setAttribute("bedrijfLijst", bedrijfLijst);

		RequestDispatcher view = request.getRequestDispatcher("/Klantbeheer.jsp");
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

}

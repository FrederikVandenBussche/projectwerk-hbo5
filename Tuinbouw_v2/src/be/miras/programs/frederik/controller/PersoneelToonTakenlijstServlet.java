package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.DbTaakDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbTaak;
import be.miras.programs.frederik.dbo.DbWerknemerOpdrachtTaak;
import be.miras.programs.frederik.model.Personeel;
import be.miras.programs.frederik.model.PersoneelbeheerTakenlijstTaak;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class PersoneelToonTakenlijstServlet
 */
@WebServlet("/PersoneelToonTakenlijstServlet")
public class PersoneelToonTakenlijstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String TAG = "PersoneelToonTakenlijstServlet: ";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersoneelToonTakenlijstServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		HttpSession session = request.getSession();
		Boolean isIngelogd = (Boolean) session.getAttribute("isIngelogd");
		RequestDispatcher view = null;

		if (isIngelogd == null || isIngelogd == false) {
			view = request.getRequestDispatcher("/logout");

		} else {

			int persoonId = Datatype.stringNaarInt(request.getParameter("id"));

			System.out.println(TAG + "de persoonId = " + persoonId);

			List<Personeel> personeelLijst = (ArrayList<Personeel>) session.getAttribute("lijst");

			DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
			DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
			DbTaakDao dbTaakDao = new DbTaakDao();
			DbKlantDao dbKlantDao = new DbKlantDao();

			List<PersoneelbeheerTakenlijstTaak> lijst = new ArrayList<PersoneelbeheerTakenlijstTaak>();

			// haal het de juiste persoon op;
			Personeel werknemer = new Personeel();

			Iterator<Personeel> it = personeelLijst.iterator();
			while (it.hasNext()) {
				Personeel p = it.next();
				if (p.getPersoonId() == persoonId) {
					werknemer = p;
				}
			}

			// Naam van de persoon
			String persoonNaam = werknemer.getVoornaam().concat(" ").concat(werknemer.getNaam());

			/*
			 * een lijst van taken die bij deze persoon horen
			 * 
			 * Datum, Klantnaam, Opdracht, Taak , Verwijderbutton
			 * WerknemerOpdrachtTaakDb OpdrachtDb TaakDb
			 */
			int werknemerId = werknemer.getWerknemerId();
			int opdrachtId = Integer.MIN_VALUE;
			int taakId = Integer.MIN_VALUE;
			Date startdatum = new Date();

			List<Object> objectenLijst = new ArrayList<Object>();
			objectenLijst = dbWerknemerOpdrachtTaakDao.leesOpdrachtIdTaakIdBeginuur(werknemerId);
			Iterator<Object> iter = objectenLijst.iterator();

			while (iter.hasNext()) {
				System.out.println("New object: id, opdrachtId,taakId, beginuur ");
				Object[] obj = (Object[]) iter.next();
				PersoneelbeheerTakenlijstTaak taak = new PersoneelbeheerTakenlijstTaak();

				int dbWerknemerOpdrachtTaakId = (int) obj[0];

				opdrachtId = (int) obj[1];
				taakId = (int) obj[2];
				startdatum = (Date) obj[3];

				String[] klantIdEnOpdrachtNaam = dbOpdrachtDao.selectKlantIdEnNaam(opdrachtId);
				int klantId = Datatype.stringNaarInt(klantIdEnOpdrachtNaam[0]);
				String opdrachtNaam = klantIdEnOpdrachtNaam[1];

				String taakNaam = dbTaakDao.selectNaam(taakId);
				DbKlant klant = (DbKlant) dbKlantDao.lees(klantId);

				String klantNaam = klant.geefAanspreekNaam();

				taak.setDbWerknemerOpdrachtTaakId(dbWerknemerOpdrachtTaakId);
				taak.setOpdrachtnaam(opdrachtNaam);
				taak.setTaaknaam(taakNaam);
				taak.setKlantnaam(klantNaam);
				taak.setDatum(startdatum);
				System.out.println(TAG + "de datum = " + startdatum);
				lijst.add(taak);

			}

			session.setAttribute("personeelsnaam", persoonNaam);

			session.setAttribute("takenLijst", lijst);

			view = request.getRequestDispatcher("/PersoneelTakenlijst.jsp");

		}
		view.forward(request, response);
	}

}

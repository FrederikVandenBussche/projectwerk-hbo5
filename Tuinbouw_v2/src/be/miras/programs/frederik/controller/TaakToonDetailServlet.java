package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dao.adapter.PersoneelDaoAdapter;
import be.miras.programs.frederik.dbo.DbWerknemerOpdrachtTaak;
import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.model.Personeel;
import be.miras.programs.frederik.model.Planning;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class TaakToonDetailServlet
 */
@WebServlet("/TaakToonDetailServlet")
public class TaakToonDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "TaakToonDetailServlet: ";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TaakToonDetailServlet() {
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

			Taak taak = null;

			int id = Datatype.stringNaarInt(request.getParameter("id"));

			OpdrachtDetailData opdrachtDetailData = (OpdrachtDetailData) session.getAttribute("opdrachtDetailData");

			// lijst van alle werknemers
			HashMap<Integer, String> werknemerMap = geefWerknemerMap();

			if (id < 0) {
				// het gaat om de aanmaak van een nieuwe taak
				taak = new Taak();

				taak.setId(Integer.MIN_VALUE);

			} else {
				// het gaat om het wijzigen van een bestaande taak
				List<Planning> planningLijst = new ArrayList<Planning>();
				DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();

				// zoek de taak
				Opdracht opdracht = opdrachtDetailData.getOpdracht();
				List<Taak> taakLijst = opdracht.getTaakLijst();
				Iterator<Taak> it = taakLijst.iterator();
				while (it.hasNext()) {
					Taak t = it.next();
					if (t.getId() == id) {
						taak = t;
					}
				}

				System.out.println(TAG + "de taakId = " + id);
				// planningLijst ophalen

				List<DbWerknemerOpdrachtTaak> dbWerknemerOpdrachtTaakLijst = dbWerknemerOpdrachtTaakDao
						.leesWaarTaakId(id);

				Iterator<DbWerknemerOpdrachtTaak> wotIt = dbWerknemerOpdrachtTaakLijst.iterator();
				while (wotIt.hasNext()) {
					DbWerknemerOpdrachtTaak wot = wotIt.next();
					Planning planning = new Planning();
					planning.setId(wot.getId());
					int werknemerId = wot.getWerknemerId();
					String werknemerNaam = werknemerMap.get(werknemerId);
					planning.setWerknemer(werknemerNaam);
					planning.setBeginuur(wot.getBeginuur());
					planning.setEinduur(wot.getEinduur());
					planning.setIsAanwezig(wot.getAanwezig());

					planningLijst.add(planning);
				}
				taak.setPlanningLijst(planningLijst);

			}

			session.setAttribute("taak", taak);
			request.setAttribute("id", id);
			session.setAttribute("werknemerMap", werknemerMap);

			view = request.getRequestDispatcher("/Taakbeheer.jsp");

		}
		view.forward(request, response);
	}

	/*
	 * return: HashMap<
	 */
	private HashMap<Integer, String> geefWerknemerMap() {

		PersoneelDaoAdapter dao = new PersoneelDaoAdapter();
		List<Personeel> lijst = new ArrayList<Personeel>();
		HashMap<Integer, String> werknemerMap = new HashMap<Integer, String>();

		lijst = (List<Personeel>) (Object) dao.leesAlle();

		Iterator<Personeel> it = lijst.iterator();
		while (it.hasNext()) {
			Personeel p = it.next();
			int id = p.getWerknemerId();
			String naam = p.getVoornaam().concat(" ").concat(p.getNaam());
			werknemerMap.put(id, naam);
		}

		return werknemerMap;
	}

}

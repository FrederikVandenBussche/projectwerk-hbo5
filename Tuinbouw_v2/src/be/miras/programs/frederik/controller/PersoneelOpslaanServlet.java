package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.PersoneelDaoAdapter;
import be.miras.programs.frederik.model.Personeel;
import be.miras.programs.frederik.util.Datum;

/**
 * Servlet implementation class PersoneelOpslaanServlet
 */
@WebServlet("/PersoneelOpslaanServlet")
public class PersoneelOpslaanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "PersoneelOpslaanServlet: ";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersoneelOpslaanServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		int id = Integer.parseInt(request.getParameter("id"));

		String voornaam = request.getParameter("voornaam").trim();
		String naam = request.getParameter("naam").trim();
		String loonString = request.getParameter("loon").trim();
		String email = request.getParameter("email").trim();
		String geboortedatumString = request.getParameter("geboortedatum").trim();
		String aanwervingsdatumString = request.getParameter("aanwervingsdatum").trim();
		String nieuweGeboortedatumString = request.getParameter("nieuweGeboortedatum").trim();
		String nieuweAanwervingsdatumString = request.getParameter("nieuweAanwervingsdatum").trim();

		Personeel personeel = new Personeel();
		PersoneelDaoAdapter dao = new PersoneelDaoAdapter();

		double loon = 0;
		try {
			loon = Double.parseDouble(loonString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (nieuweGeboortedatumString.equals("") == false) {
			geboortedatumString = nieuweGeboortedatumString;
		}
		if (!nieuweAanwervingsdatumString.equals("")) {
			aanwervingsdatumString = nieuweAanwervingsdatumString;
			;
		}
		Date geboortedatum = Datum.creeerDatum(geboortedatumString);
		Date aanwervingsdatum = Datum.creeerDatum(aanwervingsdatumString);

		personeel.setVoornaam(voornaam);
		personeel.setNaam(naam);
		personeel.setLoon(loon);
		personeel.setEmail(email);
		personeel.setGeboortedatum(geboortedatum);
		personeel.setAanwervingsdatum(aanwervingsdatum);

		if (id < 0) {
			// nieuw Personeelslid toevoegen
			List<Personeel> lijst = new ArrayList<Personeel>();

			dao.voegToe(personeel);

			// de lijst opnieuw ophalen

			lijst = (List<Personeel>) (Object) dao.leesAlle();

			HttpSession session = request.getSession();
			session.setAttribute("lijst", lijst);

		} else {
			// indien er iets gewijzigd werd, de wijzigingen opslaan
			HttpSession session = request.getSession();
			ArrayList<Personeel> lijst = (ArrayList<Personeel>) session.getAttribute("lijst");

			// Je kan geen elementen wijzigen in Iterator
			// Dit kan wel in een ListIterator
			ListIterator<Personeel> it = lijst.listIterator();
			while (it.hasNext()) {
				Personeel p = it.next();
				if (p.getPersoonId() == id) {
					System.out.println(TAG + "de persoonId = " + p.getPersoonId());
					System.out.println(TAG + "De werknemersId = " + p.getWerknemerId());
					if (p.isVerschillend(personeel, p)) {
						personeel.setPersoonId(id);
						personeel.setWerknemerId(p.getWerknemerId());
						dao.wijzig(personeel);
						it.set(personeel);
					}
				}

			}
		}

		RequestDispatcher view = request.getRequestDispatcher("/Personeelsbeheer.jsp");
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

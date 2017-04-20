package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.MateriaalDaoAdapter;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class MateriaalOpslaanServlet
 */
@WebServlet("/MateriaalOpslaanServlet")
public class MateriaalOpslaanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MateriaalOpslaanServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String idString = request.getParameter("id").trim();
		String naam = request.getParameter("naam").trim();
		String soort = request.getParameter("soort").trim();
		String eenheidsmaat = request.getParameter("eenheidsmaat").trim();
		String eenheidsprijsString = request.getParameter("eenheidsprijs").trim();

		// de Materialenlijst ophalen
		HttpSession session = request.getSession();
		ArrayList<Materiaal> lijst = (ArrayList<Materiaal>) session.getAttribute("lijst");

		Materiaal materiaal = new Materiaal();
		MateriaalDaoAdapter dao = new MateriaalDaoAdapter();

		double eenheidsprijs = 0;
		int id = Datatype.stringNaarInt(idString);
		if (eenheidsprijsString != "") {
			try {
				eenheidsprijs = Double.parseDouble(eenheidsprijsString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			eenheidsprijs = 0;
		}

		materiaal.setNaam(naam);
		materiaal.setSoort(soort);
		materiaal.setEenheidsmaat(eenheidsmaat);
		materiaal.setEenheidsprijs(eenheidsprijs);

		if (id < 0) {
			// nieuwMateriaal
			dao.voegToe(materiaal);

			// de lijst bewerken
			lijst.add(materiaal);

		} else {

			// indien er iets gewijzigd werd, de wijzigingen opslaan

			ListIterator<Materiaal> it = lijst.listIterator();
			while (it.hasNext()) {
				Materiaal m = it.next();
				if (m.getId() == id) {
					if (m.isVerschillend(materiaal, m)) {
						materiaal.setId(id);

						dao.wijzig(materiaal);
						it.set(materiaal);
					}

				}
			}

		}

		RequestDispatcher view = request.getRequestDispatcher("/Materiaalbeheer.jsp");
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

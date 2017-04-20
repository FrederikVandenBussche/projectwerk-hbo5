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

import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dbo.DbKlant;

/**
 * Servlet implementation class FacturatiebeheerServlet
 */
@WebServlet("/FacturatiebeheerServlet")
public class FacturatiebeheerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FacturatiebeheerServlet() {
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

			// DB initialiseren
			DbKlantDao dbKlantDao = new DbKlantDao();

			// andere initialisaties
			List<DbKlant> klantlijst = new ArrayList<DbKlant>();
			Map<Integer, String> klantMap = new HashMap<Integer, String>();

			// haal klantlijst op
			klantlijst = (List<DbKlant>) (Object) dbKlantDao.leesAlle();
			// er is een klantenlijst nodig
			Iterator<DbKlant> klantIterator = klantlijst.iterator();
			while (klantIterator.hasNext()) {
				DbKlant dbKlant = klantIterator.next();

				int itKlantId = dbKlant.getId();
				String itKlantNaam = dbKlant.geefAanspreekNaam();

				klantMap.put(itKlantId, itKlantNaam);

			}

			request.setAttribute("klantMap", klantMap);
			session.setAttribute("klantlijst", klantlijst);

			view = request.getRequestDispatcher("/Facturatie.jsp");

		}
		view.forward(request, response);
	}

}

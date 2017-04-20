package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dbo.DbBedrijf;
import be.miras.programs.frederik.dbo.DbParticulier;

/**
 * Servlet implementation class KlantLeeslijstServlet
 */
@WebServlet("/KlantLeeslijstServlet")
public class KlantLeeslijstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KlantLeeslijstServlet() {
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

			DbKlantDao dbKlantDao = new DbKlantDao();

			List<DbParticulier> particulierLijst = (ArrayList<DbParticulier>) (Object) dbKlantDao.leesAlleParticulier();
			List<DbBedrijf> bedrijfLijst = (ArrayList<DbBedrijf>) (Object) dbKlantDao.leesAlleBedrijf();

			session.setAttribute("particulierLijst", particulierLijst);
			session.setAttribute("bedrijfLijst", bedrijfLijst);

			view = request.getRequestDispatcher("/Klantbeheer.jsp");

		}
		view.forward(request, response);

	}

}

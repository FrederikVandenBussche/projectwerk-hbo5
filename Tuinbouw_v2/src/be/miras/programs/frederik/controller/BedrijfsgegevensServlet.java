package be.miras.programs.frederik.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.WerkgeverDaoAdapter;
import be.miras.programs.frederik.model.Werkgever;

/**
 * Servlet implementation class BedrijfsgegevensServlet
 */
@WebServlet("/BedrijfsgegevensServlet")
public class BedrijfsgegevensServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String TAG = "BedrijfsgegevensServlet: ";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BedrijfsgegevensServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
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
			
			int gebruikerId = (int) session.getAttribute("gebruikerId");
			
			WerkgeverDaoAdapter wDao = new WerkgeverDaoAdapter();
			Werkgever werkgever = new Werkgever();

			System.out.println(TAG + "GebruikerId= " + gebruikerId);
			werkgever = (Werkgever) wDao.lees(gebruikerId);

			System.out.println(TAG + "de werkgeversvoornaam: " + werkgever.getVoornaam());
			session.setAttribute("werkgever", werkgever);

			view = request.getRequestDispatcher("/Bedrijfsgegevens.jsp");

		}
		view.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

}

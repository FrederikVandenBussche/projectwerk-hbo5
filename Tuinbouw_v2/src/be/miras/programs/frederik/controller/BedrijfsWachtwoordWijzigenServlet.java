package be.miras.programs.frederik.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbGebruikerDao;
import be.miras.programs.frederik.model.Werkgever;

/**
 * Servlet implementation class BedrijfsWachtwoordWijzigenServlet
 */
@WebServlet("/BedrijfsWachtwoordWijzigenServlet")
public class BedrijfsWachtwoordWijzigenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BedrijfsWachtwoordWijzigenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String oudWachtwoord = request.getParameter("oud").trim();
		String nieuwWachtwoord1 = request.getParameter("nieuw1").trim();
		String nieuwWachtwoord2 = request.getParameter("nieuw2").trim();

		HttpSession session = request.getSession();
		Werkgever werkgever = (Werkgever) session.getAttribute("werkgever");

		DbGebruikerDao dao = new DbGebruikerDao();

		int id = werkgever.getId();

		String daoWachtwoord = (String) dao.leesWachtwoord(id);

		if (oudWachtwoord.equals(daoWachtwoord)) {
			System.out.println("het oud wachtwoord is correct ");
			if (nieuwWachtwoord1.equals(nieuwWachtwoord2) && nieuwWachtwoord1.length() > 1) {
				dao.wijzigWachtwoord(id, nieuwWachtwoord1);
				werkgever.setWachtwoord(nieuwWachtwoord1);
				session.setAttribute("werkgever", werkgever);
			}
		}

		RequestDispatcher view = request.getRequestDispatcher("/Bedrijfsgegevens.jsp");
		view.forward(request, response);

	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		RequestDispatcher view = request.getRequestDispatcher("/logout");
		view.forward(request, response);
	}
	

}

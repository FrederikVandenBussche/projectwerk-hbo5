package be.miras.programs.frederik.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbBevoegdheidDao;
import be.miras.programs.frederik.dao.DbGebruikerDao;
import be.miras.programs.frederik.dbo.DbBevoegdheid;
import be.miras.programs.frederik.dbo.DbGebruiker;

/**
 * Servlet implementation class InlogServlet
 */
@WebServlet("/InlogServlet")
public class InlogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InlogServlet() {
		super();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String rol = "werkgever";

		String gebruikersnaam = request.getParameter("gebruikersnaam");
		String wachtwoord = request.getParameter("wachtwoord");

		DbGebruikerDao dbGebruikerDao = new DbGebruikerDao();
		DbBevoegdheidDao dbBevoegdheidDao = new DbBevoegdheidDao();

		DbGebruiker dbGebruiker = dbGebruikerDao.getGebruiker(gebruikersnaam);
		
		int gebruikerId = Integer.MIN_VALUE;
		boolean isIngelogd = false;
		if (dbGebruiker != null && wachtwoord != null) {
			if (wachtwoord.equals(dbGebruiker.getWachtwoord())) {
				DbBevoegdheid dbBevoegdheid = (DbBevoegdheid) dbBevoegdheidDao.lees(dbGebruiker.getBevoegdheidId());
				if (dbBevoegdheid.getRol().equals(rol)) {
					isIngelogd = true;
					gebruikerId = dbGebruiker.getId();
					
				}
			}
		}

		/*
		 * 
		 * 
		 * tijdens het ontwerp van deze applicatie ben ik steeds ingelogd:
		 * 
		 * 
		 */
		isIngelogd = true;
		gebruikerId = 1;
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		if (isIngelogd) {
			HttpSession session = request.getSession();
			session.setAttribute("isIngelogd", isIngelogd);
			session.setAttribute("gebruikerId", gebruikerId);

			RequestDispatcher view = request.getRequestDispatcher("/opdrachtenMenu");
			view.forward(request, response);
		} else {

			RequestDispatcher view = request.getRequestDispatcher("/main.html");
			view.forward(request, response);
		}

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

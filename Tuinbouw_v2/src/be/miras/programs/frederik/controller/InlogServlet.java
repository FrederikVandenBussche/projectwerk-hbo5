package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

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
import be.miras.programs.frederik.util.InputValidatieStrings;
import be.miras.programs.frederik.util.SHA1;

/**
 * @author Frederik Vanden Bussche
 * 
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

		String errorMsg = "";
		String rol = "werkgever";

		String gebruikersnaam = request.getParameter("gebruikersnaam");
		String wachtwoord = request.getParameter("wachtwoord");
		
		DbGebruikerDao dbGebruikerDao = new DbGebruikerDao();
		DbBevoegdheidDao dbBevoegdheidDao = new DbBevoegdheidDao();
		DbGebruiker dbGebruiker = dbGebruikerDao.getGebruiker(gebruikersnaam);
		
		boolean isIngelogd = false;
		int gebruikerId = Integer.MIN_VALUE;
		
		if (dbGebruiker.getGebruikersnaam() == null){
			
			errorMsg = InputValidatieStrings.InlogGebruikersnaamNietGekend;
		} else {
			
			if (dbGebruiker != null && wachtwoord != null) {
				String encryptie = null;
				try {
					encryptie = SHA1.Sha1(wachtwoord);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				
				if (encryptie.equals(dbGebruiker.getWachtwoord())) {
					
					DbBevoegdheid dbBevoegdheid = (DbBevoegdheid) dbBevoegdheidDao.lees(dbGebruiker.getBevoegdheidId());
					
					if (dbBevoegdheid.getRol().equals(rol)) {
					
						isIngelogd = true;
						gebruikerId = dbGebruiker.getId();		
					} else {
						
						errorMsg = InputValidatieStrings.InlogBevoegdheid;
					}

				} else {
					
					errorMsg = InputValidatieStrings.InlogWachtwoordNietCorrect;
				}
			} else {
				
				errorMsg = InputValidatieStrings.InlogWachtwoordNietCorrect;
			}
		}
		
		if (isIngelogd) {
			HttpSession session = request.getSession();
			session.setAttribute("isIngelogd", isIngelogd);
			session.setAttribute("gebruikerId", gebruikerId);

			RequestDispatcher view = request.getRequestDispatcher("/opdrachtenMenu");
			view.forward(request, response);
		} else {
			
			request.setAttribute("errorMsg", errorMsg);
			
			RequestDispatcher view = request.getRequestDispatcher("/main.jsp");
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

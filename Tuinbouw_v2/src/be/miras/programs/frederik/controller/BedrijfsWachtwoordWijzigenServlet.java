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

import be.miras.programs.frederik.dao.DbGebruikerDao;
import be.miras.programs.frederik.dao.adapter.WerkgeverDaoAdapter;
import be.miras.programs.frederik.model.Werkgever;
import be.miras.programs.frederik.util.InputValidatieStrings;
import be.miras.programs.frederik.util.SHA1;
import be.miras.programs.frederik.util.InputValidatie;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class BedrijfsWachtwoordWijzigenServlet
 */
@WebServlet("/BedrijfsWachtwoordWijzigenServlet")
public class BedrijfsWachtwoordWijzigenServlet extends HttpServlet implements IinputValidatie{
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
		int gebruikerId = (int) session.getAttribute("gebruikerId");
		
		String inputValidatieErrorMsg = inputValidatie(
				new String[]{oudWachtwoord, nieuwWachtwoord1, nieuwWachtwoord2});
		
		if (inputValidatieErrorMsg.isEmpty()) {
			
			WerkgeverDaoAdapter wDao = new WerkgeverDaoAdapter();
			Werkgever werkgever = new Werkgever();

			werkgever = (Werkgever) wDao.lees(gebruikerId);

			DbGebruikerDao dao = new DbGebruikerDao();

			int id = werkgever.getGebruikerId();

			String daoWachtwoord = (String) dao.leesWachtwoord(id);

			if (oudWachtwoord.equals(daoWachtwoord)) {
				if (nieuwWachtwoord1.equals(nieuwWachtwoord2)) {
					if (nieuwWachtwoord1.length() > 8){
						String encryptie = null;
						try {
							encryptie = SHA1.Sha1(nieuwWachtwoord1);
						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace();
						}
						dao.wijzigWachtwoord(id, encryptie);
						
					} else {
						inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Het nieuwe wachtwoord moet tenminste 8 karakters lang zijn.");
					}
					
				} else {
					inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" De twee nieuwe wachtwoorden zijn niet dezelfde.");
				}
			} else {
				
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Het oude wachtwoord niet gekend.");
			}
		} WerkgeverDaoAdapter wDao = new WerkgeverDaoAdapter();
		Werkgever werkgever = new Werkgever();

		werkgever = (Werkgever) wDao.lees(gebruikerId);
		
		request.setAttribute("werkgever", werkgever);

		request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);

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

	@Override
	public String inputValidatie(String[] teValideren) {
		String oudWachtwoord = teValideren[0];
		String nieuwWachtwoord1 = teValideren[1];
		String nieuwWachtwoord2 = teValideren[2];
				
		String inputValidatieErrorMsg = "";
		
		String msg = null;
		
		msg = InputValidatie.ingevuld(oudWachtwoord);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.OudWachtwoord).concat(msg);
		}
		
		msg = InputValidatie.ingevuld(nieuwWachtwoord1);
		String msg2 = InputValidatie.ingevuld(nieuwWachtwoord2);
		if (msg != null || msg2 != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.NieuwWachtwoord);
		}
		
		return inputValidatieErrorMsg;
	}
	

}

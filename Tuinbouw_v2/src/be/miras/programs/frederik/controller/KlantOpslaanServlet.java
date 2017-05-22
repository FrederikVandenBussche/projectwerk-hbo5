package be.miras.programs.frederik.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dbo.DbBedrijf;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbParticulier;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.InputValidatieStrings;
import be.miras.programs.frederik.util.InputValidatie;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class KlantOpslaanServlet
 */
@WebServlet("/KlantOpslaanServlet")
public class KlantOpslaanServlet extends HttpServlet implements IinputValidatie {
	private static final long serialVersionUID = 1L;
	private String type;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KlantOpslaanServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		int id = Datatype.stringNaarInt(request.getParameter("id"));

		String variabelVeld1 = request.getParameter("variabelVeld1");
		String variabelVeld2 = request.getParameter("variabelVeld2");
		
		DbKlant klant = null;

		DbKlantDao dbKlantDao = new DbKlantDao();

		HttpSession session = request.getSession();
		RequestDispatcher view = null;
		
		if (request.getParameter("variabelVeldnaam1").equals("Voornaam")) {
			this.type = "particulier";
		} else if (request.getParameter("variabelVeldnaam1").equals("Naam")) {
			this.type = "bedrijf";
		}
		// instelling soort van klant
		if (id < 0) {
			if (request.getParameter("variabelVeldnaam1").equals("Voornaam")) {
				klant = new DbParticulier();
			} else if (request.getParameter("variabelVeldnaam1").equals("Naam")) {
				klant = new DbBedrijf();
			}
		} else {
			// de klant met de corresponderende id opzoeken.
			if (this.type.equals("particulier")){
				klant = dbKlantDao.leesParticulier(id);
			}else if (this.type.equals("bedrijf")){
				klant = dbKlantDao.leesBedrijf(id);
			}
		}

		String inputValidatieErrorMsg = inputValidatie(
				new String[] { variabelVeld1, variabelVeld2 });

		if (inputValidatieErrorMsg.isEmpty()) {
			
			// de 2 variabelen uit de veriabelevelden vastleggen
			if (this.type.equals("particulier")) {
				((DbParticulier) klant).setVoornaam(variabelVeld1);
				((DbParticulier) klant).setNaam(variabelVeld2);
			} else if (this.type.equals("bedrijf")) {
				((DbBedrijf) klant).setBedrijfnaam(variabelVeld1);
				((DbBedrijf) klant).setBtwNummer(variabelVeld2);
			}

			// wijzigingen aanbrengen in de databank
			if (id < 0) {
				// nieuw Klant toevoegen
				id = dbKlantDao.voegToe(klant);
				
			} else { // ( id !< 0)
				// een bestaande Klant wijzigen
				dbKlantDao.wijzig(klant);			
			}
			
			session.setAttribute("id", id);
			
			//view = request.getRequestDispatcher("/Klantbeheer.jsp");
			view = this.getServletContext().getRequestDispatcher("/klantenMenu");
			
		} else {

			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);

			view = request.getRequestDispatcher("/KlantDetail.jsp");
		}

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

	@Override
	public String inputValidatie(String[] teValideren) {
		String variabelVeld1 = teValideren[0];
		String variabelVeld2 = teValideren[1];
		
		String inputValidatieErrorMsg = "";
		
		String msg = null;
		if (this.type.equals("particulier")){
			
			msg = InputValidatie.enkelAlfabetisch(variabelVeld1);
			if (msg != null) {
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Voornaam).concat(msg);
			}
			
			msg = InputValidatie.enkelAlfabetisch(variabelVeld2);
			if (msg != null) {
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Naam).concat(msg);
			}
			
		} else if (this.type.equals("bedrijf")){
			
			msg = InputValidatie.enkelAlfabetisch(variabelVeld1);
			if (msg != null) {
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Naam).concat(msg);
			}
			
			msg = InputValidatie.geldigBtwNummer(variabelVeld2);
			if (msg != null) {
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.BtwNummer).concat(msg);
			}
		}
		
		return inputValidatieErrorMsg;
	}

	
}

package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbGebruikerDao;
import be.miras.programs.frederik.dao.DbPersoonDao;
import be.miras.programs.frederik.dbo.DbGebruiker;
import be.miras.programs.frederik.dbo.DbPersoon;
import be.miras.programs.frederik.model.Werkgever;
import be.miras.programs.frederik.util.Datum;
import be.miras.programs.frederik.util.InputValidatieStrings;
import be.miras.programs.frederik.util.InputValidatie;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class BedrijfsgegevensWijzigenServlet
 */
@WebServlet("/BedrijfsgegevensWijzigenServlet")
public class BedrijfsgegevensWijzigenServlet extends HttpServlet implements IinputValidatie {
	private static final long serialVersionUID = 1L;
	private Werkgever werkgever;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BedrijfsgegevensWijzigenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String naam = request.getParameter("naam").trim();
		String voornaam = request.getParameter("voornaam").trim();
		String geboortedatum = request.getParameter("nieuweGeboortedatum").trim();
		String email = request.getParameter("email").trim();
		String gebruikersnaam = request.getParameter("gebruikersnaam").trim();
		
		HttpSession session = request.getSession();
		werkgever = (Werkgever) session.getAttribute("werkgever");
		
		String inputValidatieErrorMsg = inputValidatie(
				new String[]{naam, voornaam, geboortedatum, email, gebruikersnaam});
		
		if (inputValidatieErrorMsg.isEmpty()) {
			
			Date datum = null;

			if (!geboortedatum.equals("")) {
				datum = Datum.creeerDatum(geboortedatum);
			} else {
				datum = werkgever.getGeboortedatum();
			}

			// DbGebruiker wijzigen
			if (!werkgever.getEmail().equals(email) || !werkgever.getGebruikersnaam().equals(gebruikersnaam)) {

				DbGebruiker gebruiker = new DbGebruiker();
				DbGebruikerDao dao = new DbGebruikerDao();

				gebruiker.setId(werkgever.getGebruikerId());
				gebruiker.setEmail(email);
				gebruiker.setWachtwoord(werkgever.getWachtwoord());
				gebruiker.setGebruikersnaam(gebruikersnaam);
				gebruiker.setBevoegdheidId(werkgever.getBevoegdheidID());
				gebruiker.setPersoonId(werkgever.getPersoonId());
				
				Thread thread = new Thread(new Runnable(){

					@Override
					public void run() {
						
						dao.wijzig(gebruiker);	
					}	
				});
				thread.start();
				
				werkgever.setEmail(email);
				werkgever.setGebruikersnaam(gebruikersnaam);
			}

			// DbPersoon wijzigen
			if (!werkgever.getNaam().equals(naam) || !werkgever.getVoornaam().equals(voornaam)
					|| !werkgever.getGeboortedatum().equals(datum)) {
				;
				DbPersoon persoon = new DbPersoon();
				DbPersoonDao dao = new DbPersoonDao();

				persoon.setId(werkgever.getPersoonId());
				persoon.setNaam(naam);
				persoon.setVoornaam(voornaam);
				persoon.setGeboortedatum(datum);

				Thread thread = new Thread(new Runnable(){

					@Override
					public void run() {
						dao.wijzig(persoon);		
					}
				});
				thread.start();

				werkgever.setNaam(naam);
				werkgever.setVoornaam(voornaam);
				werkgever.setGeboortedatum(datum);
			}

			session.setAttribute("werkgever", werkgever);
			
		} else {
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
		}
		
		RequestDispatcher view = request.getRequestDispatcher("/Bedrijfsgegevens.jsp");
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
		String naam = teValideren[0];
		String voornaam = teValideren[1];
		String geboortedatum = teValideren[2];
		String email = teValideren[3];
		String gebruikersnaam = teValideren[4];
		
		String inputValidatieErrorMsg = "";
		
		String msg = null;
		
		msg = InputValidatie.enkelAlfabetisch(naam);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Naam).concat(msg);
		}
		
		msg = InputValidatie.enkelAlfabetisch(voornaam);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Voornaam).concat(msg);
		}
		
		if(!geboortedatum.isEmpty()){
			msg = InputValidatie.correcteDatum(geboortedatum);
			if (msg!= null ) {
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.GeboorteDatum).concat(msg);
			} else {
				Date datum = Datum.creeerDatum(geboortedatum);
				Date nu = new Date();
				nu.setYear(nu.getYear() - 18);
				if (datum.after(nu)){
					inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.GeboortedatumBedrijfsleider);
				}
			}
		}
		
		msg = InputValidatie.correctEmailadres(email);
		if (msg!= null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Email").concat(msg);
		}
		
		if (gebruikersnaam.isEmpty()){
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.GebruikersNaamInGeven);
		} else {
			DbGebruikerDao dbGebruikerDao = new DbGebruikerDao();
			int aantalMetGebruikersnaam = dbGebruikerDao.aantalMetGebruikersnaam(gebruikersnaam);
				if(aantalMetGebruikersnaam > 0){
					if (!this.werkgever.getGebruikersnaam().equals(gebruikersnaam)){
						
						inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.GebruikersNaamInGebruik);
					}	
				}
		}

		return inputValidatieErrorMsg;
	}

	
}

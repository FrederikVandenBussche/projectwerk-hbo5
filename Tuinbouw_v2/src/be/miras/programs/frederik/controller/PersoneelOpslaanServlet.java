package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.miras.programs.frederik.dao.adapter.PersoneelDaoAdapter;
import be.miras.programs.frederik.model.Personeel;
import be.miras.programs.frederik.util.Datum;
import be.miras.programs.frederik.util.InputValidatie;
import be.miras.programs.frederik.util.InputValidatieStrings;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class PersoneelOpslaanServlet
 */
@WebServlet("/PersoneelOpslaanServlet")
public class PersoneelOpslaanServlet extends HttpServlet implements IinputValidatie{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String geboortedatumString;
	private String aanwervingsdatumString;

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersoneelOpslaanServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		this.id = Integer.parseInt(request.getParameter("id"));
		
		String voornaam = request.getParameter("voornaam").trim();
		String naam = request.getParameter("naam").trim();
		String loonString = request.getParameter("loon").trim();
		String email = request.getParameter("email").trim();
		String nieuweGeboortedatumString = request.getParameter("nieuweGeboortedatum").trim();
		String nieuweAanwervingsdatumString = request.getParameter("nieuweAanwervingsdatum").trim();

		RequestDispatcher view = null;
		
		String inputValidatieErrorMsg = inputValidatie(
				new String[]{voornaam, naam, loonString, email, 
						nieuweGeboortedatumString, nieuweAanwervingsdatumString});

		PersoneelDaoAdapter personeelDaoAdapter = new PersoneelDaoAdapter();
		
		if (inputValidatieErrorMsg.isEmpty()) {
			
			Personeel personeel = new Personeel();
			List<Personeel> personeelLijst = new ArrayList<Personeel>();
			
			double loon = Double.parseDouble(loonString);
			Date geboortedatum = null;
			Date aanwervingsdatum = null;
			
			if (this.geboortedatumString != null)
				geboortedatum = Datum.creeerDatum(geboortedatumString);
			
			if (this.aanwervingsdatumString != null)
				aanwervingsdatum = Datum.creeerDatum(aanwervingsdatumString);

			personeel.setVoornaam(voornaam);
			personeel.setNaam(naam);
			personeel.setLoon(loon);
			personeel.setEmail(email);
			personeel.setGeboortedatum(geboortedatum);
			personeel.setAanwervingsdatum(aanwervingsdatum);

			if (this.id < 0) {
				
				// er kunnen geen 2 personen met dezelfde voornaam, naam en 
				// geboortedatum geregistreerd worden.
				int bestaandPersoneelId = personeelDaoAdapter.haalId(personeel);
				if (bestaandPersoneelId <= 0){
					id = personeelDaoAdapter.voegToe(personeel);
				} else {
					id = bestaandPersoneelId;
				}
				

				request.setAttribute("id", id);

			} else {
				// indien er iets gewijzigd werd, de wijzigingen opslaan
				Personeel p = (Personeel) personeelDaoAdapter.lees(id);
				
				if (personeel.getGeboortedatum() == null)
						personeel.setGeboortedatum(p.getGeboortedatum());
				
				if (personeel.getAanwervingsdatum() == null)
						personeel.setAanwervingsdatum(p.getAanwervingsdatum());
					
				if (p.isVerschillend(personeel, p)) {
					
					personeel.setPersoonId(this.id);
					personeel.setWerknemerId(p.getWerknemerId());
					personeel.setGebruikerId(p.getGebruikerId());
					personeel.setWerknemerId(p.getWerknemerId());
						
					personeelDaoAdapter.wijzig(personeel);
				}
			}
		
			personeelLijst = (List<Personeel>) (Object) personeelDaoAdapter.leesAlle();
			
			request.setAttribute("personeelLijst", personeelLijst);
			
			view = request.getRequestDispatcher("/Personeelsbeheer.jsp");
		} else {
			//inputvalidatie Error
			
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
			
			Personeel personeel = (Personeel) personeelDaoAdapter.lees(id);

			request.setAttribute("tabKiezer", "gegevens");
			request.setAttribute("personeelslid", personeel);
			
			view = request.getRequestDispatcher("/PersoneelDetail.jsp");	
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
		
		String voornaam = teValideren[0];
		String naam = teValideren[1];
		String loonString = teValideren[2];
		String email = teValideren[3];
		String nieuweGeboortedatumString = teValideren[4];
		String nieuweAanwervingsdatumString = teValideren[5];
					
		String inputValidatieErrorMsg = "";
		
		String msg = null;
		
		// inputvalidatie
		msg = InputValidatie.enkelAlfabetisch(voornaam);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Voornaam).concat(msg);
		}
		
		msg = InputValidatie.enkelAlfabetisch(naam);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Naam).concat(msg);
		}
		
		msg = InputValidatie.kommagetal(loonString);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Loon).concat(msg);
		}
		
		msg = InputValidatie.correctEmailadres(email);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Email).concat(msg);
		}
		
		this.geboortedatumString = null;
		this.aanwervingsdatumString = null;
		if (this.id < 0 || 
				(this.id > 0 && !nieuweGeboortedatumString.trim().isEmpty())){
			msg = InputValidatie.correcteDatum(nieuweGeboortedatumString);
			if (msg!= null) {
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.GeboorteDatum).concat(msg);
			} else {
				Date datum = Datum.creeerDatum(nieuweGeboortedatumString);
				Date nu = new Date();
				if (datum.after(nu)){
					inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.GeboortedatumInVerleden);
				} else {
					this.geboortedatumString = nieuweGeboortedatumString;
				}
			}
		}
		
		if (this.id < 0 ||
				(this.id > 0 && !nieuweAanwervingsdatumString.trim().isEmpty() )){
			
			msg = InputValidatie.correcteDatum(nieuweAanwervingsdatumString);
			if (msg!= null) {
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Aanwervingsdatum).concat(msg);
			} else {
				Date datum = Datum.creeerDatum(nieuweAanwervingsdatumString);
				Date nu = new Date();
				
				if (datum.after(nu)){
					inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.AanwervingsdatumNietInToekomst);
				} else {
					this.aanwervingsdatumString = nieuweAanwervingsdatumString;
				}
			}
		}
		
		return inputValidatieErrorMsg;
	}

	
}

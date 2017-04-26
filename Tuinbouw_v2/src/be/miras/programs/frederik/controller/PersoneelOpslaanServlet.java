package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.PersoneelDaoAdapter;
import be.miras.programs.frederik.model.Personeel;
import be.miras.programs.frederik.util.Datum;
import be.miras.programs.frederik.util.InputValidatie;

/**
 * Servlet implementation class PersoneelOpslaanServlet
 */
@WebServlet("/PersoneelOpslaanServlet")
public class PersoneelOpslaanServlet extends HttpServlet implements IinputValidatie{
	private static final long serialVersionUID = 1L;
	private int id;
	private String geboortedatumString;
	private String aanwervingsdatumString;
	private String TAG = "PersoneelOpslaanServlet: ";

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

		System.out.println(TAG + "request.getParameter('id') = " + this.id);
		
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
		
		

		if (inputValidatieErrorMsg.isEmpty()) {
			
			Personeel personeel = new Personeel();
			PersoneelDaoAdapter dao = new PersoneelDaoAdapter();
			
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
				// nieuw Personeelslid toevoegen
				List<Personeel> lijst = new ArrayList<Personeel>();

				dao.voegToe(personeel);

				// de lijst opnieuw ophalen

				lijst = (List<Personeel>) (Object) dao.leesAlle();

				HttpSession session = request.getSession();
				session.setAttribute("lijst", lijst);

			} else {
				// indien er iets gewijzigd werd, de wijzigingen opslaan
				HttpSession session = request.getSession();
				ArrayList<Personeel> lijst = (ArrayList<Personeel>) session.getAttribute("lijst");

				// Je kan geen elementen wijzigen in Iterator
				// Dit kan wel in een ListIterator
				ListIterator<Personeel> it = lijst.listIterator();
				while (it.hasNext()) {
					Personeel p = it.next();
					if (p.getPersoonId() == this.id) {
						System.out.println(TAG + "de persoonId = " + p.getPersoonId());
						System.out.println(TAG + "De werknemersId = " + p.getWerknemerId());
						System.out.println(TAG + "De gebruikerId = " + p.getGebruikerId());
						
						if (personeel.getGeboortedatum() == null)
							personeel.setGeboortedatum(p.getGeboortedatum());
						if (personeel.getAanwervingsdatum() == null)
							personeel.setAanwervingsdatum(p.getAanwervingsdatum());
						System.out.println(TAG + personeel.toString());
						if (p.isVerschillend(personeel, p)) {
							personeel.setPersoonId(this.id);
							personeel.setWerknemerId(p.getWerknemerId());
							personeel.setGebruikerId(p.getGebruikerId());
							personeel.setWerknemerId(p.getWerknemerId());
							System.out.println(TAG + p.toString());
							
								
							dao.wijzig(personeel);
							it.set(personeel);
						}
					}

				}
			}
	
			view = request.getRequestDispatcher("/Personeelsbeheer.jsp");
		} else {
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
			
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
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Voornaam").concat(msg);
		}
		
		msg = InputValidatie.enkelAlfabetisch(naam);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Naam").concat(msg);
		}
		
		msg = InputValidatie.kommagetal(loonString);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Loon").concat(msg);
		}
		
		msg = InputValidatie.correctEmailadres(email);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Email").concat(msg);
		}
		
		this.geboortedatumString = null;
		this.aanwervingsdatumString = null;
		if (this.id < 0 || 
				(this.id > 0 && !nieuweGeboortedatumString.trim().isEmpty())){
			msg = InputValidatie.correcteDatum(nieuweGeboortedatumString);
			if (msg!= null) {
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Geboortedatum").concat(msg);
			} else {
				Date datum = Datum.creeerDatum(nieuweGeboortedatumString);
				Date nu = new Date();
				if (datum.after(nu)){
					inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Geboortedatum moet in het verleden liggen.");
				} else {
					this.geboortedatumString = nieuweGeboortedatumString;
				}
			}
		}
		
		if (this.id < 0 ||
				(this.id > 0 && !nieuweAanwervingsdatumString.trim().isEmpty() )){
			
			msg = InputValidatie.correcteDatum(nieuweAanwervingsdatumString);
			if (msg!= null) {
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Aanwervingsdatum").concat(msg);
			} else {
				Date datum = Datum.creeerDatum(nieuweAanwervingsdatumString);
				Date nu = new Date();
				
				if (datum.after(nu)){
					inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Aanwervingsdatum mag niet in de toekomst liggen.");
				} else {
					this.aanwervingsdatumString = nieuweAanwervingsdatumString;
				}
			}
		}
		
		return inputValidatieErrorMsg;
	}

}

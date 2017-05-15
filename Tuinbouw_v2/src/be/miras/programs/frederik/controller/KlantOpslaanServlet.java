package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.ListIterator;

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

		ArrayList<DbParticulier> particulierLijst = (ArrayList<DbParticulier>) session.getAttribute("particulierLijst");
		ArrayList<DbBedrijf> bedrijfLijst = (ArrayList<DbBedrijf>) session.getAttribute("bedrijfLijst");

		RequestDispatcher view = null;

		// instelling soort van klant
		if (id < 0) {
			if (request.getParameter("variabelVeldnaam1").equals("Voornaam")) {
				klant = new DbParticulier();
			} else if (request.getParameter("variabelVeldnaam1").equals("Naam")) {
				klant = new DbBedrijf();
			}
		} else {
			// de klant met de corresponderende id opzoeken.
			Iterator<DbParticulier> it = particulierLijst.iterator();
			while (it.hasNext()) {
				DbParticulier particulier = it.next();
				if (particulier.getId() == id) {
					klant = new DbParticulier();
					klant = particulier;
				}
			}
			// de klant met de corresponderende id opzoeken.
			Iterator<DbBedrijf> iterator = bedrijfLijst.iterator();
			while (iterator.hasNext()) {
				DbBedrijf bedrijf = iterator.next();
				if (bedrijf.getId() == id) {
					klant = new DbBedrijf();
					klant = bedrijf;
				}
			}
			klant.setId(id);
		}

		String inputValidatieErrorMsg = inputValidatie(
				new String[] { klant.getClass().getSimpleName(), variabelVeld1, variabelVeld2 });

		if (inputValidatieErrorMsg.isEmpty()) {
			// de 2 variabelen uit de veriabelevelden vastleggen
			if (klant.getClass().getSimpleName().equals("DbParticulier")) {
				((DbParticulier) klant).setVoornaam(variabelVeld1);
				((DbParticulier) klant).setNaam(variabelVeld2);
			} else if (klant.getClass().getSimpleName().equals("DbBedrijf")) {
				((DbBedrijf) klant).setBedrijfnaam(variabelVeld1);
				((DbBedrijf) klant).setBtwNummer(variabelVeld2);
			}

			// wijzigingen aanbrengen in de databank en de sessionlijsten
			if (id < 0) {
				// nieuw Klant toevoegen
				id = dbKlantDao.voegToe(klant);
				klant.setId(id);
				if (klant.getClass().getSimpleName().equals("DbParticulier")) {
					
					particulierLijst.add((DbParticulier) klant);
				} else if (klant.getClass().getSimpleName().equals("DbBedrijf")) {
					
					bedrijfLijst.add((DbBedrijf) klant);

				}
			} else { // ( id !< 0)
				// een bestaande Klant wijzigen
				DbKlant teWijzigenOpDb = klant;
				Thread thread = new Thread(new Runnable(){

					@Override
					public void run() {
						dbKlantDao.wijzig(teWijzigenOpDb);
						
					}	
				});
				thread.start();
				
				if (klant.getClass().getSimpleName().equals("DbParticulier")) {
					ListIterator<DbParticulier> it = particulierLijst.listIterator();
					while (it.hasNext()) {
						DbParticulier dbParticulier = it.next();
						if (dbParticulier.getId() == id) {
							if (dbParticulier.isVerschillend(klant, dbParticulier)) {
								klant.setId(id);
								
								it.set((DbParticulier) klant);
							}
						}
					}
				} else if (klant.getClass().getSimpleName().equals("DbBedrijf")) {

					ListIterator<DbBedrijf> it = bedrijfLijst.listIterator();
					while (it.hasNext()) {
						DbBedrijf bedrijf = it.next();
						if (bedrijf.getId() == id) {
							if (bedrijf.isVerschillend(klant, bedrijf)) {
								klant.setId(id);
								// dbKlantDao.wijzigBedrijf((DbBedrijf) klant);
								it.set((DbBedrijf) klant);
							}
						}
					}
				}
			}

			session.setAttribute("particulierLijst", particulierLijst);
			session.setAttribute("bedrijfLijst", bedrijfLijst);

			view = request.getRequestDispatcher("/Klantbeheer.jsp");

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
		String klantSimpleName = teValideren[0];
		String variabelVeld1 = teValideren[1];
		String variabelVeld2 = teValideren[2];
		
		String inputValidatieErrorMsg = "";
		
		String msg = null;
		if (klantSimpleName.equals("DbParticulier")){
			
			msg = InputValidatie.enkelAlfabetisch(variabelVeld1);
			if (msg != null) {
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Voornaam).concat(msg);
			}
			
			msg = InputValidatie.enkelAlfabetisch(variabelVeld2);
			if (msg != null) {
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Naam).concat(msg);
			}
			
		} else if (klantSimpleName.equals("DbBedrijf")){
			
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

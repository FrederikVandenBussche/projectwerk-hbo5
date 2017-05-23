package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.miras.programs.frederik.dao.adapter.MateriaalDaoAdapter;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.InputValidatieStrings;
import be.miras.programs.frederik.util.InputValidatie;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class MateriaalOpslaanServlet
 */
@WebServlet("/MateriaalOpslaanServlet")
public class MateriaalOpslaanServlet extends HttpServlet  implements IinputValidatie{
	
	private static final long serialVersionUID = 1L;
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MateriaalOpslaanServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		int id = Integer.parseInt(request.getParameter("id"));
		String naam = request.getParameter("naam").trim();
		String soort = request.getParameter("soort").trim();
		String eenheidsmaat = request.getParameter("eenheidsmaat").trim();
		String eenheidsprijsString = request.getParameter("eenheidsprijs").trim();

		String inputValidatieErrorMsg = inputValidatie(
				new String[]{naam, soort, eenheidsmaat, eenheidsprijsString});
		
		MateriaalDaoAdapter dao = new MateriaalDaoAdapter();
		Materiaal materiaal = null;
				
		if (inputValidatieErrorMsg.isEmpty()) {
			materiaal = new Materiaal();
			
			double eenheidsprijs = 0;
			
			if (eenheidsprijsString != "") {
			
				eenheidsprijs = Datatype.stringNaarDouble(eenheidsprijsString);
			} else {
				
				eenheidsprijs = 0;
			}
			
			materiaal.setNaam(naam);
			materiaal.setSoort(soort);
			materiaal.setEenheidsmaat(eenheidsmaat);
			materiaal.setEenheidsprijs(eenheidsprijs);

			if (id < 0) {
				// nieuwMateriaal
				dao.voegToe(materiaal);
				
			} else {

				materiaal.setId(id);
				dao.wijzig(materiaal);
			}
		} else {
			
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);	
			
			// het materiaal ophalen zoals het was
			materiaal = new Materiaal();
			materiaal = (Materiaal) dao.lees(id);
		}
		
		List<Materiaal> lijst = new ArrayList<Materiaal>();
				
		lijst = (List<Materiaal>) (Object) dao.leesAlle();
		
		request.setAttribute("materiaalLijst", lijst);
		request.setAttribute("materiaal", materiaal);
		
		RequestDispatcher view = request.getRequestDispatcher("/Materiaalbeheer.jsp");
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
		String soort = teValideren[1];
		String eenheidsmaat = teValideren[2];
		String eenheidsprijsString = teValideren[3];
		
		String inputValidatieErrorMsg = "";
		
		String msg = null;
	
		msg = InputValidatie.ingevuld(naam);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Naam).concat(msg);
		}
		
		msg = InputValidatie.enkelAlfabetisch(soort);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Soort).concat(msg);
		}
		
		msg = InputValidatie.ingevuld(eenheidsmaat);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Eenheidsmaat).concat(msg);
		}
		
		msg = InputValidatie.kommagetal(eenheidsprijsString);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Eenheidsprijs).concat(msg);
		} else if (Datatype.stringNaarDouble(eenheidsprijsString) <= 0){
			msg = InputValidatieStrings.EenheidsprijsNietCorrect;
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(msg);
		}
		
		return inputValidatieErrorMsg;
	}

	
}

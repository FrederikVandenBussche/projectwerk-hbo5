package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.miras.programs.frederik.dao.adapter.MateriaalDaoAdapter;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.model.TypeMateriaal;
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

		int materiaalId = Datatype.stringNaarInt(request.getParameter("materiaalId"));
		int typeMateriaalId = Datatype.stringNaarInt(request.getParameter("typeMateriaalId"));
		String naam = request.getParameter("naam").trim();
		String eenheidsmaat = request.getParameter("eenheidsmaat").trim();
		String eenheidsprijsString = request.getParameter("eenheidsprijs").trim();
		
		String inputValidatieErrorMsg = inputValidatie(
				new String[]{naam, Integer.toString(typeMateriaalId), eenheidsmaat, eenheidsprijsString});
		
		MateriaalDaoAdapter dao = new MateriaalDaoAdapter();
		Materiaal materiaal = null;
		List<TypeMateriaal> lijst = new ArrayList<TypeMateriaal>();
		String tabKiezer = null;
				
		if (inputValidatieErrorMsg.isEmpty()) {
			materiaal = new Materiaal();
			
			double eenheidsprijs = 0;
			
			if (eenheidsprijsString != "") {
			
				eenheidsprijs = Datatype.stringNaarDouble(eenheidsprijsString);
			} else {
				
				eenheidsprijs = 0;
			}
			
			materiaal.setNaam(naam);
			materiaal.setEenheidsmaat(eenheidsmaat);
			materiaal.setEenheidsprijs(eenheidsprijs);
			materiaal.setSoortId(typeMateriaalId);

			if (materiaalId < 0) {
				// nieuwMateriaal
				int bestaandMateriaalId = dao.haalId(materiaal);
				
				if (bestaandMateriaalId <= 0){
					materiaalId = dao.voegToe(materiaal);
				} else {
					materiaalId = bestaandMateriaalId;
					
					materiaal.setId(materiaalId);
					dao.wijzig(materiaal);
				}
			} else {

				materiaal.setId(materiaalId);
				dao.wijzig(materiaal);
			}
		} else {
			
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);	
		}
				
		lijst = (List<TypeMateriaal>) (Object) dao.leesAlle();
		Iterator<TypeMateriaal> it = lijst.iterator();
		while (it.hasNext()) {
			TypeMateriaal tm = it.next();
			if (tm.getId() == typeMateriaalId) {
				
				tabKiezer = tm.getNaam();
				
				if (inputValidatieErrorMsg.isEmpty()){
					
					materiaal.setId(-1);
					materiaal.setNaam("");
					materiaal.setEenheidsmaat("");
					materiaal.setEenheidsprijs(0.0);
					materiaal.setSoortId(Integer.MIN_VALUE);
				} else {
					Iterator<Materiaal> materiaalIt = tm.getMateriaalLijst().iterator();
					while (materiaalIt.hasNext()){
						Materiaal m = materiaalIt.next();
						
						if(m.getId() == materiaalId){
							materiaal = m;
						}
					}
				}
			}
		}
		
		request.setAttribute("tabKiezer", tabKiezer);
		request.setAttribute("typeMateriaalLijst", lijst);
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
		String typeMateriaalId = teValideren[1];
		String eenheidsmaat = teValideren[2];
		String eenheidsprijsString = teValideren[3];
		
		String inputValidatieErrorMsg = "";
		
		String msg = null;
	
		msg = InputValidatie.ingevuld(naam);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Naam).concat(msg);
		}
		msg = InputValidatie.ingevuld(typeMateriaalId);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat("Type materiaal is niet ingevuld. ");
		}
		
		msg = InputValidatie.ingevuld(eenheidsmaat);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Eenheidsmaat).concat(msg);
		}
		
		msg = InputValidatie.kommagetal(eenheidsprijsString);
		String msg2 = InputValidatie.ingevuld(eenheidsprijsString);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Eenheidsprijs).concat(msg);
		} else if (Datatype.stringNaarDouble(eenheidsprijsString) <= 0 ){
			msg = InputValidatieStrings.EenheidsprijsNietCorrect;
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(msg);
		} else if(msg2 != null){
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Eenheidsprijs).concat(msg2);
		}
		
		return inputValidatieErrorMsg;
	}

	
}

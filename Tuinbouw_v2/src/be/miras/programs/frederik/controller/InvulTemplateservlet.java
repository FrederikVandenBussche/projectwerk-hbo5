package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.PersoonAdresDaoAdapter;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Werkgever;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.Datum;
import be.miras.programs.frederik.util.GoogleApis;
import be.miras.programs.frederik.util.InputValidatie;

/**
 * Servlet implementation class InvulTemplateservlet
 */
@WebServlet("/InvulTemplateservlet")
public class InvulTemplateservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InvulTemplateservlet() {
		super();
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	response.setContentType("text/html");

	String straat = request.getParameter("straat").trim();
	String nummerString = request.getParameter("nr").trim();
	String geboortedatum = request.getParameter("nieuweGeboortedatum").trim();
	String email = request.getParameter("email").trim();

	String inputValidatieErrorMsg = "";
	
	String msg = null;
	
	msg = InputValidatie.enkelAlfabetisch(straat);
	if (msg != null) {
		inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Straat").concat(msg);
	}
	
	msg = InputValidatie.geheelGetal(nummerString);
	if (msg != null) {
		inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Huisnummer").concat(msg);
	}
	
	msg = InputValidatie.correcteDatum(geboortedatum);
	if (msg!= null) {
		inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Geboortedatum").concat(msg);
	} else {
		Date datum = Datum.creeerDatum(geboortedatum);
		Date nu = new Date();
		nu.setYear(nu.getYear() - 18);
		if (datum.after(nu)){
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Geboortedatum: Een bedrijfsleider is ten minsten 18 jaar.");
		}
	}
	
	msg = InputValidatie.correctEmailadres(email);
	if (msg!= null) {
		inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Email").concat(msg);
	}
	
	

	if (inputValidatieErrorMsg.isEmpty()) {
		
	} else {
		request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
		
	}
	//deze blijft staat
	RequestDispatcher view = request.getRequestDispatcher("/bedrijfsgegevensMenu");
	view.forward(request, response);
	}

	

}

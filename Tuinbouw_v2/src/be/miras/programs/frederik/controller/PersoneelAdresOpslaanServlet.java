package be.miras.programs.frederik.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.PersoneelDaoAdapter;
import be.miras.programs.frederik.dao.adapter.PersoonAdresDaoAdapter;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Personeel;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.GoogleApis;
import be.miras.programs.frederik.util.InputValidatie;
import be.miras.programs.frederik.util.InputValidatieStrings;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class AdresOpslaanServlet
 */
@WebServlet("/AdresOpslaanServlet")
public class PersoneelAdresOpslaanServlet extends HttpServlet implements IinputValidatie {
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersoneelAdresOpslaanServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String aanspreeknaam = request.getParameter("aanspreeknaam");
		String opslaanBtnNaam = request.getParameter("buttonNaam");
		String straat = request.getParameter("straat").trim();
		String nummerString = request.getParameter("nr").trim();
		String bus = request.getParameter("bus").trim();
		String postcodeString = request.getParameter("postcode").trim();
		String plaats = request.getParameter("plaats").trim();
		
		String inputValidatieErrorMsg = inputValidatie(
				new String[]{straat, nummerString, bus, postcodeString, plaats});
		
		HttpSession session = request.getSession();
		int persoonId = (int) session.getAttribute("id");
		
		PersoneelDaoAdapter personeelDaoAdapter = new PersoneelDaoAdapter();

		if (inputValidatieErrorMsg.isEmpty()) {
			
			PersoonAdresDaoAdapter persoonAdresDaoAdapter = new PersoonAdresDaoAdapter();
			Adres adres = new Adres();

			int nr = 0;
			int postcode = 0;

			nr = Datatype.stringNaarInt(nummerString);
			postcode = Datatype.stringNaarInt(postcodeString);

			adres.setStraat(straat);
			adres.setNummer(nr);
			adres.setBus(bus);
			adres.setPostcode(postcode);
			adres.setPlaats(plaats);
			adres.setPersoonId(persoonId);

			int adresId = persoonAdresDaoAdapter.voegToe(adres);
			adres.setId(adresId);

			String staticmap = GoogleApis.urlBuilderStaticMap(adres);
			adres.setStaticmap(staticmap);

			String googlemap = GoogleApis.urlBuilderGoogleMaps(adres);
			adres.setGooglemap(googlemap);
			
		} else {
			
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
		}
		
		Personeel personeel = (Personeel) personeelDaoAdapter.lees(persoonId);
		
		request.setAttribute("personeelslid", personeel);
		request.setAttribute("aanspreeknaam", aanspreeknaam);
		request.setAttribute("buttonNaam", opslaanBtnNaam);
		
		RequestDispatcher view = request.getRequestDispatcher("/PersoneelDetail.jsp");
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
		String straat = teValideren[0];
		String nummerString = teValideren[1];
		String postcodeString = teValideren[3];
		String plaats = teValideren[4];
		
		String inputValidatieErrorMsg = "";
		
		String msg = null;
		
		msg = InputValidatie.enkelAlfabetisch(straat);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Straat).concat(msg);
		}
		
		msg = InputValidatie.geheelGetal(nummerString);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Huisnummer).concat(msg);
		}
		
				
		msg = InputValidatie.geheelGetal(postcodeString);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Postcode).concat(msg);
		}
		
		msg = InputValidatie.enkelAlfabetisch(plaats);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Plaats).concat(msg);
		}
		
		return inputValidatieErrorMsg;
	}

	
}

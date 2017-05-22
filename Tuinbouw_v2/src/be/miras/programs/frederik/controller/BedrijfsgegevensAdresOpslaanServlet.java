package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.PersoonAdresDaoAdapter;
import be.miras.programs.frederik.dao.adapter.WerkgeverDaoAdapter;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Werkgever;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.GoogleApis;
import be.miras.programs.frederik.util.InputValidatieStrings;
import be.miras.programs.frederik.util.InputValidatie;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class BedrijfsgegevensAdresOpslaanServlet
 */
@WebServlet("/BedrijfsgegevensAdresOpslaanServlet")
public class BedrijfsgegevensAdresOpslaanServlet extends HttpServlet implements IinputValidatie {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BedrijfsgegevensAdresOpslaanServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String straat = request.getParameter("straat").trim();
		String nummerString = request.getParameter("nr").trim();
		String bus = request.getParameter("bus").trim();
		String postcodeString = request.getParameter("postcode").trim();
		String plaats = request.getParameter("plaats").trim();
		
		String inputValidatieErrorMsg = inputValidatie(
				new String[]{straat, nummerString, bus, postcodeString, plaats});
				if (inputValidatieErrorMsg.isEmpty()) {

			int nr = Datatype.stringNaarInt(nummerString);
			int postcode = Datatype.stringNaarInt(postcodeString);

			HttpSession session = request.getSession();
			
			int gebruikerId = (int) session.getAttribute("gebruikerId");

			WerkgeverDaoAdapter wDao = new WerkgeverDaoAdapter();
			Werkgever werkgever = new Werkgever();

			werkgever = (Werkgever) wDao.lees(gebruikerId);

			Adres adres = new Adres();
			

			adres.setStraat(straat);
			adres.setNummer(nr);
			adres.setBus(bus);
			adres.setPostcode(postcode);
			adres.setPlaats(plaats);
			adres.setPersoonId(werkgever.getPersoonId());
			
			PersoonAdresDaoAdapter adao = new PersoonAdresDaoAdapter();
			int maxId = adao.voegToe(adres);
				
			adres.setId(maxId);

			String staticmap = GoogleApis.urlBuilderStaticMap(adres);
			adres.setStaticmap(staticmap);

			String googlemap = GoogleApis.urlBuilderGoogleMaps(adres);
			adres.setGooglemap(googlemap);	
	
			ArrayList<Adres> adreslijst = werkgever.getAdreslijst();
			adreslijst.add(adres);
			werkgever.setAdreslijst(adreslijst);

			request.setAttribute("werkgever", werkgever);
			
		} else {
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
			
		}
		
		RequestDispatcher view = request.getRequestDispatcher("/bedrijfsgegevensMenu");
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

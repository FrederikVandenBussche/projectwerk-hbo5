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

import org.apache.log4j.Logger;

import be.miras.programs.frederik.dao.DbGebruikerDao;
import be.miras.programs.frederik.dao.adapter.PersoonAdresDaoAdapter;
import be.miras.programs.frederik.dao.adapter.WerkgeverDaoAdapter;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Werkgever;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.GoogleApis;
import be.miras.programs.frederik.util.InputValidatieStrings;
import be.miras.programs.frederik.util.InputValidatie;

/**
 * Servlet implementation class BedrijfsgegevensAdresOpslaanServlet
 */
@WebServlet("/BedrijfsgegevensAdresOpslaanServlet")
public class BedrijfsgegevensAdresOpslaanServlet extends HttpServlet implements IinputValidatie {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(DbGebruikerDao.class);

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
			Werkgever werkgever = (Werkgever) session.getAttribute("werkgever");

			Adres adres = new Adres();
			PersoonAdresDaoAdapter adao = new PersoonAdresDaoAdapter();

			adres.setStraat(straat);
			adres.setNummer(nr);
			adres.setBus(bus);
			adres.setPostcode(postcode);
			adres.setPlaats(plaats);
			adres.setPersoonId(werkgever.getPersoonId());

			
			Thread thread = new Thread(new Runnable(){

				@Override
				public void run() {
					
					adao.voegToe(adres);
					
					int maxId = adao.geefMaxId();
					
					adres.setId(maxId);
					
					String staticmap = GoogleApis.urlBuilderStaticMap(adres);
					adres.setStaticmap(staticmap);

					String googlemap = GoogleApis.urlBuilderGoogleMaps(adres);
					adres.setGooglemap(googlemap);
					
					ArrayList<Adres> adreslijst = werkgever.getAdreslijst();
					adreslijst.add(adres);
					werkgever.setAdreslijst(adreslijst);

					session.setAttribute("werkgever", werkgever);

				}
				
			});
			thread.start();

			

			
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
		String bus = teValideren[2];
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

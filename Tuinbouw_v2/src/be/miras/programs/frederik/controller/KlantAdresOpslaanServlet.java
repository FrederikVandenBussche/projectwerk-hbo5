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

import be.miras.programs.frederik.dao.DbAdresDao;
import be.miras.programs.frederik.dao.DbGemeenteDao;
import be.miras.programs.frederik.dao.DbKlantAdresDao;
import be.miras.programs.frederik.dao.DbStraatDao;
import be.miras.programs.frederik.dbo.DbAdres;
import be.miras.programs.frederik.dbo.DbGemeente;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbKlantAdres;
import be.miras.programs.frederik.dbo.DbStraat;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.GoogleApis;
import be.miras.programs.frederik.util.InputValidatieStrings;
import be.miras.programs.frederik.util.InputValidatie;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class KlantAdresOpslaanServlet
 */
@WebServlet("/KlantAdresOpslaanServlet")
public class KlantAdresOpslaanServlet extends HttpServlet implements IinputValidatie{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KlantAdresOpslaanServlet() {
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
		String variabelVeldnaam1 = request.getParameter("variabelVeldnaam1");
		String variabelVeld1 = request.getParameter("variabelVeld1");
		String variabelVeldnaam2 = request.getParameter("variabelVeldnaam2");
		String variabelVeld2 = request.getParameter("variabelVeld2");
		int id = Datatype.stringNaarInt(request.getParameter("klant_id"));
		
		String straat = request.getParameter("straat").trim();
		String nummerString = request.getParameter("nr").trim();
		String bus = request.getParameter("bus").trim();
		String postcodeString = request.getParameter("postcode").trim();
		String plaats = request.getParameter("plaats").trim();
		
		String inputValidatieErrorMsg = inputValidatie(
				new String[]{straat, nummerString, bus, postcodeString, plaats});
		
		if (inputValidatieErrorMsg.isEmpty()) {
			int nummer = Datatype.stringNaarInt(nummerString);
			int postcode = Datatype.stringNaarInt(postcodeString);
			
			HttpSession session = request.getSession();
			DbKlant klant = (DbKlant) session.getAttribute("klant");

			DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
			DbAdresDao dbAdresDao = new DbAdresDao();
			DbStraatDao dbStraatDao = new DbStraatDao();
			DbGemeenteDao dbGemeenteDao = new DbGemeenteDao();
			DbAdres dbAdres = new DbAdres();
			DbKlantAdres dbKlantAdres = new DbKlantAdres();

			Adres adres = new Adres();

			int gemeenteId = dbGemeenteDao.geefIdVan(postcode, plaats);
			if (gemeenteId < 0) {
				DbGemeente gemeente = new DbGemeente();
				gemeente.setNaam(plaats);
				gemeente.setPostcode(postcode);
				dbGemeenteDao.voegToe(gemeente);
				gemeenteId = dbGemeenteDao.geefIdVan(postcode, plaats);
			}
			
			int straatId = dbStraatDao.geefIdVan(straat);
			if (straatId < 0) {
				DbStraat dbStraat = new DbStraat();
				dbStraat.setNaam(straat);
				dbStraatDao.voegToe(dbStraat);
				straatId = dbStraatDao.geefIdVan(straat);
			}

			dbAdres.setStraatId(straatId);
			dbAdres.setGemeenteId(gemeenteId);
			dbAdres.setHuisnummer(nummer);
			dbAdres.setBus(bus);
			int adresId = dbAdresDao.voegToe(dbAdres);

			dbKlantAdres.setKlantId(id);
			dbKlantAdres.setAdresId(adresId);
			dbKlantAdresDao.voegToe(dbKlantAdres);

			ArrayList<Adres> adreslijst = klant.getAdreslijst();
			adres.setId(adresId);
			adres.setStraat(straat);
			adres.setNummer(nummer);
			adres.setBus(bus);
			adres.setPostcode(postcode);
			adres.setPlaats(plaats);
			adres.setPersoonId(id);

			String staticmap = GoogleApis.urlBuilderStaticMap(adres);
			adres.setStaticmap(staticmap);

			String googlemap = GoogleApis.urlBuilderGoogleMaps(adres);
			adres.setGooglemap(googlemap);

			adreslijst.add(adres);

			klant.setAdreslijst(adreslijst);

			session.setAttribute("klant", klant);
		}else {
			
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
		}
		request.setAttribute("id", id); // klant_id
		request.setAttribute("aanspreeknaam", aanspreeknaam);
		request.setAttribute("variabelVeld1", variabelVeld1);
		request.setAttribute("variabelVeldnaam1", variabelVeldnaam1);
		request.setAttribute("variabelVeld2", variabelVeld2);
		request.setAttribute("variabelVeldnaam2", variabelVeldnaam2);

		RequestDispatcher view = request.getRequestDispatcher("/KlantDetail.jsp");
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

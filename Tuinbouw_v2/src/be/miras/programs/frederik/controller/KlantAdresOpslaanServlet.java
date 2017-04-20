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

/**
 * Servlet implementation class KlantAdresOpslaanServlet
 */
@WebServlet("/KlantAdresOpslaanServlet")
public class KlantAdresOpslaanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "KlantAdresOpslaanServlet: ";

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
		int nummer = Datatype.stringNaarInt(request.getParameter("nr").trim());
		String bus = request.getParameter("bus").trim();
		int postcode = Datatype.stringNaarInt(request.getParameter("postcode").trim());
		String plaats = request.getParameter("plaats").trim();

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
		System.out.println(TAG + "de gemeenteId = " + gemeenteId);
		int straatId = dbStraatDao.geefIdVan(straat);
		if (straatId < 0) {
			DbStraat dbStraat = new DbStraat();
			dbStraat.setNaam(straat);
			dbStraatDao.voegToe(dbStraat);
			straatId = dbStraatDao.geefIdVan(straat);
		}
		System.out.println(TAG + "de straatId = " + straatId);

		dbAdres.setStraatId(straatId);
		dbAdres.setGemeenteId(gemeenteId);
		dbAdres.setHuisnummer(nummer);
		dbAdres.setBus(bus);
		dbAdresDao.voegToe(dbAdres);

		int adresId = dbAdresDao.zoekMaxId();
		System.out.println(TAG + "Naar dbKlantAdresDao");
		System.out.println(TAG + "Naar dbKlantAdresDao");
		System.out.println(TAG + "Naar dbKlantAdresDao");

		dbKlantAdres.setKlantId(id);
		dbKlantAdres.setAdresId(adresId);
		dbKlantAdresDao.voegToe(dbKlantAdres);
		System.out.println(TAG + "Ik ben uit de dbKlantAdresDao");

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

}

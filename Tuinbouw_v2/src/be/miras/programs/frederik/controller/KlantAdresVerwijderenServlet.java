package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

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
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class KlantAdresVerwijderenServlet
 */
@WebServlet("/KlantAdresVerwijderenServlet")
public class KlantAdresVerwijderenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "KlantAdresVerwijderenServlet: ";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KlantAdresVerwijderenServlet() {
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
		int adresId = Datatype.stringNaarInt(request.getParameter("adres_id"));

		HttpSession session = request.getSession();
		DbKlant klant = (DbKlant) session.getAttribute("klant");

		// adres verwijderen
		adresVerwijderen(klant.getId(), adresId);

		// De adresLIjst van de klant in de session wijzigen
		ArrayList<Adres> adresLijst = klant.getAdreslijst();

		ListIterator<Adres> it = adresLijst.listIterator();
		while (it.hasNext()) {
			Adres a = it.next();
			if (a.getId() == adresId) {
				it.remove();
			}
		}

		request.setAttribute("aanspreeknaam", aanspreeknaam);
		request.setAttribute("variabelVeldnaam1", variabelVeldnaam1);
		request.setAttribute("variabelVeld1", variabelVeld1);
		request.setAttribute("variabelVeldnaam2", variabelVeldnaam2);
		request.setAttribute("variabelVeld2", variabelVeld2);

		RequestDispatcher view = request.getRequestDispatcher("/KlantDetail.jsp");
		view.forward(request, response);

	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		RequestDispatcher view = request.getRequestDispatcher("/logout");
		view.forward(request, response);
	}
	

	private void adresVerwijderen(int klantId, int adresId) {
		DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
		DbAdresDao dbAdresdao = new DbAdresDao();

		System.out.println(TAG + "dbKlantAdresDao.verwijder() klantId = " + klantId + ", adresId = " + adresId);
		// klantAdres verwijderen
		dbKlantAdresDao.verwijder(klantId, adresId);
		System.out.println(TAG + "is gelukt");

		// straatId en gemeenteId ophalen
		DbAdres dbadres = (DbAdres) dbAdresdao.lees(adresId);
		int straatId = dbadres.getStraatId();
		int gemeenteId = dbadres.getGemeenteId();
		System.out.println(TAG + "dbAdresDao.verwijder() adresId = " + adresId);
		// delete dbAdres
		dbAdresdao.verwijder(adresId);
		System.out.println(TAG + "is gelukt");
		// indien de straat nergens anders gebruikt wordt.
		// deze uit de db verwijderen
		boolean straatInGebruik = dbAdresdao.isStraatInGebruik(straatId);

		if (!straatInGebruik) {
			DbStraatDao dbStraatDao = new DbStraatDao();
			System.out.println(TAG + "dbStraatDao.verwijder() straatId = " + straatId);
			dbStraatDao.verwijder(straatId);
			System.out.println(TAG + "is gelukt");
		}
		// indien de gemeente nergens anders gebruikt wordt
		// deze uit de db verwijderen
		boolean gemeenteInGebruik = dbAdresdao.isGemeenteInGebruik(gemeenteId);

		if (!gemeenteInGebruik) {
			DbGemeenteDao dbGemeenteDao = new DbGemeenteDao();
			System.out.println(TAG + "dbGemeenteDao.verwijder() gemeenteId = " + gemeenteId);
			dbGemeenteDao.verwijder(gemeenteId);
			System.out.println(TAG + "is gelukt");
		}
	}

}

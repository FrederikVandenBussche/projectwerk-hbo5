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
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Werkgever;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.GoogleApis;

/**
 * Servlet implementation class BedrijfsgegevensAdresOpslaanServlet
 */
@WebServlet("/BedrijfsgegevensAdresOpslaanServlet")
public class BedrijfsgegevensAdresOpslaanServlet extends HttpServlet {
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
		adres.setPersoonId(werkgever.getId());

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

}

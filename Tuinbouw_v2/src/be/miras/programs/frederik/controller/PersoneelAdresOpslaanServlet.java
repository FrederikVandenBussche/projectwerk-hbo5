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
import be.miras.programs.frederik.model.Personeel;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.GoogleApis;

/**
 * Servlet implementation class AdresOpslaanServlet
 */
@WebServlet("/AdresOpslaanServlet")
public class PersoneelAdresOpslaanServlet extends HttpServlet {
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

		int werknemerId = Datatype.stringNaarInt(request.getParameter("personeel_id"));

		String straat = request.getParameter("straat").trim();
		String nummerString = request.getParameter("nr").trim();
		String bus = request.getParameter("bus").trim();
		String postcodeString = request.getParameter("postcode").trim();
		String plaats = request.getParameter("plaats").trim();

		HttpSession session = request.getSession();
		Personeel personeelslid = (Personeel) session.getAttribute("personeelslid");

		Adres adres = new Adres();
		PersoonAdresDaoAdapter adao = new PersoonAdresDaoAdapter();

		int nr = 0;
		int postcode = 0;

		nr = Datatype.stringNaarInt(nummerString);
		postcode = Datatype.stringNaarInt(postcodeString);

		adres.setStraat(straat);
		adres.setNummer(nr);
		adres.setBus(bus);
		adres.setPostcode(postcode);
		adres.setPlaats(plaats);
		adres.setPersoonId(werknemerId);

		adao.voegToe(adres);
		int maxId = adao.geefMaxId();
		adres.setId(maxId);

		// Het personeelslid als attribute meegeven met de session.
		ArrayList<Adres> adreslijst = personeelslid.getAdreslijst();

		String staticmap = GoogleApis.urlBuilderStaticMap(adres);
		adres.setStaticmap(staticmap);

		String googlemap = GoogleApis.urlBuilderGoogleMaps(adres);
		adres.setGooglemap(googlemap);

		adreslijst.add(adres);
		personeelslid.setAdreslijst(adreslijst);

		session.setAttribute("personeelslid", personeelslid);

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

}

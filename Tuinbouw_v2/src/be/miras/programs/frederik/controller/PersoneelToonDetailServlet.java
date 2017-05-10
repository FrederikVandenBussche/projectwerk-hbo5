package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

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
import be.miras.programs.frederik.util.GoogleApis;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class PersoneelToonDetailServlet
 */
@WebServlet("/PersoneelToonDetailServlet")
public class PersoneelToonDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersoneelToonDetailServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		int id = Integer.parseInt(request.getParameter("id"));

		// bovenaan de content wordt de aanspreeknaam van een personeelslid
		// weergegevne.
		String aanspreeknaam = null;
		String buttonNaam = null; // 'Voeg toe' of 'Wijziging opslaan'
		Personeel personeelslid = new Personeel();

		if (id == -1) {
			/*
			 * het gaat om een nieuw personeelslid. De naam van een
			 * personeelslid word weergegeven bovenaan de content van
			 * 'PersoneelDetail.jsp'. Daarom stellen we nu de voornaam van het
			 * personeelslid tijdelijk in.
			 */
			aanspreeknaam = "";
			buttonNaam = "Voeg toe";

		} else {
			// het gaat niet om een nieuw personeelslid.
			PersoonAdresDaoAdapter dao = new PersoonAdresDaoAdapter();
			ArrayList<Adres> adreslijst = new ArrayList<Adres>();

			// de lijst met personeel uit de session halen
			HttpSession session = request.getSession();
			ArrayList<Personeel> lijst = (ArrayList<Personeel>) session.getAttribute("personeelLijst");

			// het personeelslid met de corresponderende id opzoeken.
			Iterator<Personeel> it = lijst.iterator();
			while (it.hasNext()) {
				Personeel p = it.next();
				if (p.getPersoonId() == id) {
					personeelslid = p;
				}
			}

			aanspreeknaam = personeelslid.getVoornaam() + " " + personeelslid.getNaam();
			buttonNaam = "Wijziging opslaan";

			/*
			 * de adreslijst van dit personeelslid ophalen
			 */

			adreslijst = (ArrayList<Adres>) dao.leesSelectief("persoon", id);

			ListIterator<Adres> adresLijstIt = adreslijst.listIterator();
			while (adresLijstIt.hasNext()) {
				Adres adres = adresLijstIt.next();

				String staticmap = GoogleApis.urlBuilderStaticMap(adres);
				adres.setStaticmap(staticmap);

				String googlemap = GoogleApis.urlBuilderGoogleMaps(adres);
				adres.setGooglemap(googlemap);
			}

			personeelslid.setAdreslijst(adreslijst);

		}

		/*
		 * de id in een httpSession zodat deze in een andere Servlet terug
		 * opgehaald kan worden
		 */
		HttpSession session = request.getSession();

		session.setAttribute("id", id);
		
		session.setAttribute("aanspreeknaam", aanspreeknaam);
		session.setAttribute("buttonNaam", buttonNaam);

		session.setAttribute("personeelslid", personeelslid);

		RequestDispatcher view = this.getServletContext().getRequestDispatcher("/PersoneelDetail.jsp");
		view.forward(request, response);
	}


}
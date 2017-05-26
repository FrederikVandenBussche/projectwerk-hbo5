package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.adapter.AdresDaoAdapter;
import be.miras.programs.frederik.dao.adapter.KlantDaoAdapter;
import be.miras.programs.frederik.dbo.DbBedrijf;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbParticulier;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.util.GoogleApis;

/**
 *  @author Frederik Vanden Bussche
 *  
 * Servlet implementation class KlantVerwijderServlet
 */
@WebServlet("/KlantVerwijderServlet")
public class KlantVerwijderServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KlantVerwijderServlet() {
        super();
    }
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		int id = Integer.parseInt(request.getParameter("id"));
		
		HttpSession session = request.getSession();
		String type = (String) session.getAttribute("type");
		
		KlantDaoAdapter klantDaoAdapter = new KlantDaoAdapter();
		RequestDispatcher view = null;
		
		//bekijk of ongefactureerde opdrachten
		boolean ongefactureerd = klantDaoAdapter.isOngefactureerd(id);
		
		if(!ongefactureerd){
			DbKlantDao dbKlantDao = new DbKlantDao();
			
			klantDaoAdapter.verwijder(id);
			
			List<DbParticulier> particulierLijst = (ArrayList<DbParticulier>) (Object) dbKlantDao.leesAlleParticulier();
			List<DbBedrijf> bedrijfLijst = (ArrayList<DbBedrijf>) (Object) dbKlantDao.leesAlleBedrijf();

			request.setAttribute("particulierLijst", particulierLijst);
			request.setAttribute("bedrijfLijst", bedrijfLijst);		
			request.setAttribute("tabKiezer", type);
			
			view = request.getRequestDispatcher("/Klantbeheer.jsp");
		} else {
			String errorMsg = "Kan deze klant niet verwijderen omdat er nog ongefactureerde opdrachten zijn.";
			
			DbKlantDao dbKlantDao = new DbKlantDao();
			AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();
			DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
			
			//Klant ophalen
			DbKlant klant = null;

			ArrayList<DbOpdracht> opdrachtLijst = null;
			HashMap<Integer, String> opdrachtMap = new HashMap<Integer, String>();

			if (type.equals("particulier")) {
				
				klant = new DbParticulier();
			} else if (type.equals("bedrijf")) {
				
				klant = new DbBedrijf();
			} else {
				// Er is geen type klant gedefinieerd
			}
				
			// de klant met de corresponderende id opzoeken.
			if (type.equals("particulier")) {
				klant = dbKlantDao.leesParticulier(id);		
			} else if (type.equals("bedrijf")) {
				klant = dbKlantDao.leesBedrijf(id);				
			}

			/*
			 * de adreslijst van deze klant ophalen
			 */
			
			ArrayList<Adres> adreslijst = (ArrayList<Adres>) adresDaoAdapter.leesWaarKlantId(id);
			ListIterator<Adres> adresLijstIt = adreslijst.listIterator();
			while (adresLijstIt.hasNext()) {
				Adres adres = adresLijstIt.next();

				String staticmap = GoogleApis.urlBuilderStaticMap(adres);
				adres.setStaticmap(staticmap);

				String googlemap = GoogleApis.urlBuilderGoogleMaps(adres);
				adres.setGooglemap(googlemap);
			}

			klant.setAdreslijst(adreslijst);

			// de opdrachtenlijst van deze klant ophalen
			opdrachtLijst = (ArrayList<DbOpdracht>) dbOpdrachtDao.leesWaarKlantId(id);

			Iterator<DbOpdracht> dbOpdrachtIt = opdrachtLijst.iterator();
			while (dbOpdrachtIt.hasNext()) {
				DbOpdracht opdracht = dbOpdrachtIt.next();

				int opdrachtId = opdracht.getId();
				String opdrachtNaam = opdracht.getNaam();

				opdrachtMap.put(opdrachtId, opdrachtNaam);
			}

			request.setAttribute("klant", klant);
			request.setAttribute("opdrachtMap", opdrachtMap);
			request.setAttribute("inputValidatieErrorMsg",  errorMsg);
			view = request.getRequestDispatcher("/KlantDetail.jsp");
		}
	
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

	
}

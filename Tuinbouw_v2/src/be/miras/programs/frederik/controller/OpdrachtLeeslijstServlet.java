package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dbo.DbBedrijf;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbParticulier;
import be.miras.programs.frederik.model.Opdracht;

/**
 * Servlet implementation class OpdrachtbeheerServlet
 */
@WebServlet("/OpdrachtbeheerServlet")
public class OpdrachtLeeslijstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OpdrachtLeeslijstServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		HttpSession session = request.getSession();
		Boolean isIngelogd =  (Boolean) session.getAttribute("isIngelogd");
		RequestDispatcher view = null;
		
		if (isIngelogd == null || isIngelogd == false ){
			view = request.getRequestDispatcher("/logout");
		
		} else {
			
		
		
		List<Opdracht> opdrachtLijst = new ArrayList<Opdracht>();
		
		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
		DbKlantDao dbKlantDao = new DbKlantDao();
		
		List<DbOpdracht> dbOpdrachtLijst = (List<DbOpdracht>) (Object) dbOpdrachtDao.leesAlle();
		
		Iterator<DbOpdracht> it = dbOpdrachtLijst.iterator();
		DbOpdracht dbOpdracht = null;
				
		while(it.hasNext()){
			dbOpdracht = it.next();
			
			Opdracht opdracht = new Opdracht();
			
			int klantId = dbOpdracht.getKlantId();
			
			DbKlant dbKlant = (DbKlant) dbKlantDao.lees(klantId);
			String naam = null;
			
			if(dbKlant.getClass().getSimpleName().equals("DbParticulier")) {
				String voornaam = ((DbParticulier) dbKlant).getVoornaam();
				String familienaam = ((DbParticulier) dbKlant).getNaam();
				naam = familienaam.concat(" ").concat(voornaam);
			} else if(dbKlant.getClass().getSimpleName().equals("DbBedrijf")){
				naam = ((DbBedrijf) dbKlant).getBedrijfnaam();
			} else {
				// DbKlant is geen DbParticulier en ook geen DbBedrijf
			}
						
			int id = dbOpdracht.getId();
			String opdrachtNaam = dbOpdracht.getNaam();
			Date startDatum = dbOpdracht.getStartdatum();
			Date eindDatum = dbOpdracht.getEinddatum();
			
			opdracht.setId(id);
			opdracht.setklantId(klantId);
			opdracht.setKlantNaam(naam);
			opdracht.setOpdrachtNaam(opdrachtNaam);
			opdracht.setStartDatum(startDatum);
			opdracht.setEindDatum(eindDatum);
			opdracht.setLatitude(dbOpdracht.getLatitude());
			opdracht.setLongitude(dbOpdracht.getLongitude());
			
			opdrachtLijst.add(opdracht);
		}
		
	
		
		session.setAttribute("lijst", opdrachtLijst);
		
		view = request.getRequestDispatcher("/Opdrachtbeheer.jsp");
		
		}
		view.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		doGet(request, response);
	}
}

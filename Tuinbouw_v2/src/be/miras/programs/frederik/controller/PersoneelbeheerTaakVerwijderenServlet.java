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
import be.miras.programs.frederik.dao.DbTaakDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dao.adapter.PersoneelDaoAdapter;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.model.Personeel;
import be.miras.programs.frederik.model.PersoneelbeheerTakenlijstTaak;
import be.miras.programs.frederik.util.Datatype;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class PersoneelbeheerTaakVerwijderenServlet
 */
@WebServlet("/PersoneelbeheerTaakVerwijderenServlet")
public class PersoneelbeheerTaakVerwijderenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersoneelbeheerTaakVerwijderenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		int id = Datatype.stringNaarInt(request.getParameter("id"));

		HttpSession session = request.getSession();
		int persoonId = (int) session.getAttribute("persoonId");

		DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
		DbTaakDao dbTaakDao = new DbTaakDao();
		DbKlantDao dbKlantDao = new DbKlantDao();
		PersoneelDaoAdapter personeelDaoAdapter = new PersoneelDaoAdapter();
		List<PersoneelbeheerTakenlijstTaak> lijst = new ArrayList<PersoneelbeheerTakenlijstTaak>();
		
		dbWerknemerOpdrachtTaakDao.verwijder(id);

		Personeel personeel = (Personeel) personeelDaoAdapter.lees(persoonId);

		// een lijst van taken die bij deze persoon horen
		int werknemerId = personeel.getWerknemerId();
		int opdrachtId = Integer.MIN_VALUE;
		int taakId = Integer.MIN_VALUE;
		Date startdatum = new Date();

		List<Object> objectenLijst = new ArrayList<Object>();
		objectenLijst = dbWerknemerOpdrachtTaakDao.leesOpdrachtIdTaakIdBeginuur(werknemerId);
		Iterator<Object> iter = objectenLijst.iterator();
		while (iter.hasNext()) {
			Object[] obj = (Object[]) iter.next();
			
			PersoneelbeheerTakenlijstTaak taak = new PersoneelbeheerTakenlijstTaak();

			int dbWerknemerOpdrachtTaakId = (int) obj[0];
			opdrachtId = (int) obj[1];
			taakId = (int) obj[2];
			startdatum = (Date) obj[3];

			String[] klantIdEnOpdrachtNaam = dbOpdrachtDao.selectKlantIdEnNaam(opdrachtId);
			int klantId = Datatype.stringNaarInt(klantIdEnOpdrachtNaam[0]);
			String opdrachtNaam = klantIdEnOpdrachtNaam[1];
			String taakNaam = dbTaakDao.selectNaam(taakId);
			DbKlant klant = (DbKlant) dbKlantDao.lees(klantId);
			String klantNaam = klant.geefAanspreekNaam();

			taak.setDbWerknemerOpdrachtTaakId(dbWerknemerOpdrachtTaakId);
			taak.setOpdrachtnaam(opdrachtNaam);
			taak.setTaaknaam(taakNaam);
			taak.setKlantnaam(klantNaam);
			taak.setDatum(startdatum);
			
			lijst.add(taak);
		}
		
		request.setAttribute("takenLijst", lijst);

		RequestDispatcher view = request.getRequestDispatcher("/PersoneelTakenlijst.jsp");
		view.forward(request, response);
	}

	
}

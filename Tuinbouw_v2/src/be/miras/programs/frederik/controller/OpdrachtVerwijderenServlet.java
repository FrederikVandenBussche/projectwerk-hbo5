package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.DbOpdrachtMateriaalDao;
import be.miras.programs.frederik.dao.DbOpdrachtTaakDao;
import be.miras.programs.frederik.dao.DbTaakDao;
import be.miras.programs.frederik.dao.DbVooruitgangDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dao.adapter.OpdrachtDaoAdapter;
import be.miras.programs.frederik.model.Opdracht;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class OpdrachtVerwijderen
 */
@WebServlet("/OpdrachtVerwijderen")
public class OpdrachtVerwijderenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OpdrachtVerwijderenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		HttpSession session = request.getSession();
		int opdrachtId = (int) session.getAttribute("id");

		DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
		DbOpdrachtMateriaalDao dbOpdrachtMateriaalDao = new DbOpdrachtMateriaalDao();
		DbTaakDao dbTaakDao = new DbTaakDao();
		DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
		DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
		
		// de opdracht uit de databank verwijderen
		
		// 1. Werknemer_Opdracht_Taak
		dbWerknemerOpdrachtTaakDao.verwijderWaarOpdrachtId(opdrachtId);
		
		// 2. Opdracht_Materiaal
		dbOpdrachtMateriaalDao.verwijderWaarOpdrachtId(opdrachtId);
		
		// 3. Opdracht_Taak
		// ik wil eerst een lijst met Vooruitgag Ids en taakIds
		List<Integer> vooruitgangIdLijst = dbOpdrachtTaakDao.leesVooruitgangIds(opdrachtId);
		List<Integer> taakIdLijst = dbOpdrachtTaakDao.leesTaakIds(opdrachtId);
		dbOpdrachtTaakDao.verwijderWaarOpdrachtId(opdrachtId);
		
		// 4. Vooruitgang
		for (int vId : vooruitgangIdLijst) {
			
			dbVooruitgangDao.verwijder(vId);
		}
		
		// 5. Taak
		for (int tId : taakIdLijst) {
			
			dbTaakDao.verwijder(tId);
		}
		
		// 6. Opdracht
		dbOpdrachtDao.verwijder(opdrachtId);
	
		OpdrachtDaoAdapter opdrachtDaoAdapter = new OpdrachtDaoAdapter();
		List<Opdracht> opdrachtLijst = opdrachtDaoAdapter.haalOpdrachtenOp();
		
		request.setAttribute("opdrachtLijst", opdrachtLijst);
		
		RequestDispatcher view = request.getRequestDispatcher("/Opdrachtbeheer.jsp");
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

package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dao.adapter.TaakDaoAdapter;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.Datatype;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class TaakGeplandeWerknemerVerwijderen
 */
@WebServlet("/TaakGeplandeWerknemerVerwijderen")
public class TaakGeplandeWerknemerVerwijderenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TaakGeplandeWerknemerVerwijderenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		HttpSession session = request.getSession();
		Boolean isIngelogd = (Boolean) session.getAttribute("isIngelogd");
		RequestDispatcher view = null;

		if (isIngelogd == null || isIngelogd == false) {
			
			view = request.getRequestDispatcher("/logout");
		} else {

			int planningId = Integer.parseInt(request.getParameter("planningId"));
			int taakId = Integer.parseInt(request.getParameter("taakId"));
			int opdrachtId = (int) session.getAttribute("id");
			
			DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakdao = new DbWerknemerOpdrachtTaakDao();
			DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
			DbKlantDao dbKlantDao = new DbKlantDao();
			TaakDaoAdapter taakDaoAdapter = new TaakDaoAdapter();
			
			dbWerknemerOpdrachtTaakdao.verwijder(planningId);
			
			Taak taak = taakDaoAdapter.haalTaak(taakId, opdrachtId);
			HashMap<Integer, String> werknemerMap = taakDaoAdapter.geefWerknemerMap();
			
			String[] klantIdEnNaam = dbOpdrachtDao.selectKlantIdEnNaam(opdrachtId);
			int klantId = Datatype.stringNaarInt(klantIdEnNaam[0]);
			String opdrachtNaam = klantIdEnNaam[1];
			
			DbKlant dbKlant = (DbKlant) dbKlantDao.lees(klantId);
			String klantNaam = dbKlant.geefAanspreekNaam();
			
			request.setAttribute("klantNaam", klantNaam);
			request.setAttribute("opdrachtNaam", opdrachtNaam);
			request.setAttribute("taak", taak);
			request.setAttribute("id", taakId);
			request.setAttribute("werknemerMap", werknemerMap);

			view = request.getRequestDispatcher("/Taakbeheer.jsp");
		}
		
		view.forward(request, response);
	}

	
}

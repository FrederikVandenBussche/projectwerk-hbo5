package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dbo.DbWerknemerOpdrachtTaak;
import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.model.Planning;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.Datum;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class TaakPlanningToevoegenServlet
 */
@WebServlet("/TaakPlanningToevoegenServlet")
public class TaakPlanningToevoegenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "TaakPlanningToevoegenServlet: ";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TaakPlanningToevoegenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		int werknemerId = Datatype.stringNaarInt(request.getParameter("werknemer"));
		String datumString = request.getParameter("datum").trim();

		HttpSession session = request.getSession();
		OpdrachtDetailData opdrachtDetailData = (OpdrachtDetailData) session.getAttribute("opdrachtDetailData");
		Taak taak = (Taak) session.getAttribute("taak");
		HashMap<Integer, String> werknemerMap = (HashMap<Integer, String>) session.getAttribute("werknemerMap");

		DbWerknemerOpdrachtTaak dbWerknemerOpdrachtTaak = new DbWerknemerOpdrachtTaak();
		DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
		List<Planning> planningLijst = new ArrayList<Planning>();
		Planning planning = new Planning();

		// DbWerknemerOpdrachtTaak aanmaken en toevoegen aan databank
		Opdracht opdracht = opdrachtDetailData.getOpdracht();

		Date beginuur = Datum.creeerDatum(datumString);

		int opdrachtTaakOpdrachtId = opdracht.getId();
		int opdrachtTaakTaakId = taak.getId();

		dbWerknemerOpdrachtTaak.setWerknemerId(werknemerId);
		dbWerknemerOpdrachtTaak.setOpdrachtTaakOpdrachtId(opdrachtTaakOpdrachtId);
		dbWerknemerOpdrachtTaak.setOpdrachtTaakTaakId(opdrachtTaakTaakId);
		dbWerknemerOpdrachtTaak.setBeginuur(beginuur);

		dbWerknemerOpdrachtTaakDao.voegToe(dbWerknemerOpdrachtTaak);

		// planningLijst aanpassen bij deze taak

		planningLijst = taak.getPlanningLijst();

		System.out.println(TAG + "planningLijst.size = " + planningLijst.size());

		planning.setWerknemer(werknemerMap.get(werknemerId));
		planning.setBeginuur(beginuur);

		planningLijst.add(planning);

		RequestDispatcher view = request.getRequestDispatcher("/Taakbeheer.jsp");
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

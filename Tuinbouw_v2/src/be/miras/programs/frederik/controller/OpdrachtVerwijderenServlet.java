package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

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
import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class OpdrachtVerwijderen
 */
@WebServlet("/OpdrachtVerwijderen")
public class OpdrachtVerwijderenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "OpdrachtVerwijderen: ";

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
		OpdrachtDetailData opdrachtDetailData = (OpdrachtDetailData) session.getAttribute("opdrachtDetailData");

		DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
		DbOpdrachtMateriaalDao dbOpdrachtMateriaalDao = new DbOpdrachtMateriaalDao();
		DbTaakDao dbTaakDao = new DbTaakDao();
		DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
		DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();

		// benodigde id's opvragen om de opdracht in zijn geheel te verwijderen
		int opdrachtId = opdrachtDetailData.getOpdracht().getId();

		System.out.println(TAG + "de te verwijderen opdracht: " + opdrachtId);

		// de opdracht uit de databank verwijderen
		// 1. Werknemer_Opdracht_Taak
		dbWerknemerOpdrachtTaakDao.verwijderWaarOpdrachtId(opdrachtId);
		// 2. Opdracht_Materiaal
		dbOpdrachtMateriaalDao.verwijderWaarOpdrachtId(opdrachtId);
		// 3. Opdracht_Taak
		// ik wil eerst een lijst met Vooruitgag Ids
		List<Integer> vooruitgangIdLijst = dbOpdrachtTaakDao.leesVooruitgangIds(opdrachtId);
		dbOpdrachtTaakDao.verwijderWaarOpdrachtId(opdrachtId);
		// 4. Vooruitgang
		for (int vId : vooruitgangIdLijst) {
			System.out.println(TAG + "ik verwijder de vooruitgang met id " + vId);
			dbVooruitgangDao.verwijder(vId);
		}
		// 5. Taak
		for (Taak t : opdrachtDetailData.getOpdracht().getTaakLijst()) {
			System.out.println(TAG + "ik verwijder de taak met id " + t.getId());
			dbTaakDao.verwijder(t.getId());
		}
		// 6. Opdracht
		dbOpdrachtDao.verwijder(opdrachtId);

		// de opdracht uit de session verwijderen
		List<Opdracht> opdrachtLijst = (List<Opdracht>) session.getAttribute("lijst");
		ListIterator<Opdracht> it = opdrachtLijst.listIterator();
		while (it.hasNext()) {
			Opdracht o = it.next();
			if (o.getId() == opdrachtId) {
				it.remove();
			}
		}

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

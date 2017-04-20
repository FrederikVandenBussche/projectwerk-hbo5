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

import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.model.Planning;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class TaakGeplandeWerknemerVerwijderen
 */
@WebServlet("/TaakGeplandeWerknemerVerwijderen")
public class TaakGeplandeWerknemerVerwijderenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "TaakGeplandeWerknemerVerwijderen: ";

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

			int id = Datatype.stringNaarInt(request.getParameter("id"));
			System.out.println(TAG + "de te verwijderen id = " + id);

			Taak taak = (Taak) session.getAttribute("taak");

			DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakdao = new DbWerknemerOpdrachtTaakDao();

			dbWerknemerOpdrachtTaakdao.verwijder(id);

			List<Planning> planningLijst = taak.getPlanningLijst();
			ListIterator<Planning> it = planningLijst.listIterator();
			while (it.hasNext()) {
				Planning p = it.next();
				if (p.getId() == id) {
					it.remove();
				}
			}

			view = request.getRequestDispatcher("/Taakbeheer.jsp");

		}
		view.forward(request, response);
	}

}

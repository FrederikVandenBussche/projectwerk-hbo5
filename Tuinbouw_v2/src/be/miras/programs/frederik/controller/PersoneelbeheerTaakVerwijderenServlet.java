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
import be.miras.programs.frederik.model.PersoneelbeheerTakenlijstTaak;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class PersoneelbeheerTaakVerwijderenServlet
 */
@WebServlet("/PersoneelbeheerTaakVerwijderenServlet")
public class PersoneelbeheerTaakVerwijderenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "PersoneelbeheerTaakVerwijderenServlet: ";

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

		System.out.println(TAG + "Verwijder de planning met id : " + id);

		HttpSession session = request.getSession();
		List<PersoneelbeheerTakenlijstTaak> lijst = (List<PersoneelbeheerTakenlijstTaak>) session.getAttribute("takenLijst");

		DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
		dbWerknemerOpdrachtTaakDao.verwijder(id);

		ListIterator<PersoneelbeheerTakenlijstTaak> it = lijst.listIterator();
		while (it.hasNext()) {
			PersoneelbeheerTakenlijstTaak taak = it.next();
			if (taak.getDbWerknemerOpdrachtTaakId() == id) {
				it.remove();
			}
		}

		session.setAttribute("takenLijst", lijst);

		RequestDispatcher view = request.getRequestDispatcher("/PersoneelTakenlijst.jsp");
		view.forward(request, response);
	}

}

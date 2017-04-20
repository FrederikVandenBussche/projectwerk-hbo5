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

import be.miras.programs.frederik.dao.adapter.TaakDaoAdapter;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class TaakVerwijderenServlet
 */
@WebServlet("/TaakVerwijderenServlet")
public class TaakVerwijderenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "TaakVerwijderenServlet: ";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TaakVerwijderenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		TaakDaoAdapter taakDaoAdapter = new TaakDaoAdapter();

		int id = Datatype.stringNaarInt(request.getParameter("id"));

		HttpSession session = request.getSession();
		OpdrachtDetailData opdrachtDetailData = (OpdrachtDetailData) session.getAttribute("opdrachtDetailData");

		System.out.println(TAG + "de te verwijderen taak : " + id);

		taakDaoAdapter.verwijder(id);

		// de taak uit de session verwijderen
		List<Taak> takenlijst = opdrachtDetailData.getOpdracht().getTaakLijst();

		ListIterator<Taak> it = takenlijst.listIterator();
		while (it.hasNext()) {
			Taak taak = (Taak) it.next();
			if (taak.getId() == id) {
				it.remove();
			}
		}

		RequestDispatcher view = request.getRequestDispatcher("/OpdrachtDetail.jsp");
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

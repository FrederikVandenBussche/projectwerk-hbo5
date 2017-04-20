package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.PersoneelDaoAdapter;
import be.miras.programs.frederik.model.Personeel;

/**
 * Servlet implementation class PersoneelLeeslijstServlet
 */
@WebServlet("/PersoneelLeeslijstServlet")
public class PersoneelLeeslijstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersoneelLeeslijstServlet() {
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

			PersoneelDaoAdapter dao = new PersoneelDaoAdapter();
			List<Personeel> lijst = new ArrayList<Personeel>();

			lijst = (List<Personeel>) (Object) dao.leesAlle();

			session.setAttribute("lijst", lijst);

			view = request.getRequestDispatcher("/Personeelsbeheer.jsp");

		}
		view.forward(request, response);
	}

}

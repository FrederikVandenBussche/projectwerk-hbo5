package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class MateriaalWijzigenServlet
 */
@WebServlet("/MateriaalWijzigenServlet")
public class MateriaalWijzigenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MateriaalWijzigenServlet() {
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

			ArrayList<Materiaal> lijst = (ArrayList<Materiaal>) session.getAttribute("lijst");

			Materiaal materiaal = new Materiaal();

			Iterator<Materiaal> it = lijst.iterator();
			while (it.hasNext()) {
				Materiaal m = it.next();
				if (m.getId() == id) {
					materiaal = m;
				}
			}

			request.setAttribute("materiaal", materiaal);

			view = request.getRequestDispatcher("/Materiaalbeheer.jsp");

		}
		view.forward(request, response);
	}

}

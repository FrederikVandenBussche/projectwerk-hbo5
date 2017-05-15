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

import be.miras.programs.frederik.dao.adapter.MateriaalDaoAdapter;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.util.SessieOpruimer;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class MateriaalLeeslijstServlet
 */
@WebServlet("/MateriaalLeeslijstServlet")
public class MateriaalLeeslijstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MateriaalLeeslijstServlet() {
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
			
			MateriaalDaoAdapter dao = new MateriaalDaoAdapter();
			
			Materiaal m = new Materiaal();
			// voor nieuw materiaal : id = -1
			m.setId(-1);

			List<Materiaal> lijst = new ArrayList<Materiaal>();
					
			lijst = (List<Materiaal>) (Object) dao.leesAlle();
					
			SessieOpruimer.AttributenVerwijderaar(session);
			
			session.setAttribute("materiaalLijst", lijst);

			session.setAttribute("materiaal", m);

			view = request.getRequestDispatcher("/Materiaalbeheer.jsp");
		}
		view.forward(request, response);
	}

	
}

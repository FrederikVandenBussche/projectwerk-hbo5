package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.PersoonAdresDaoAdapter;
import be.miras.programs.frederik.dao.adapter.PersoneelDaoAdapter;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Personeel;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class PersoneelVerwijderServlet
 */
@WebServlet("/PersoneelVerwijderServlet")
public class PersoneelVerwijderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersoneelVerwijderServlet() {
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

		HttpSession session = request.getSession();
		Personeel p = (Personeel) session.getAttribute("personeelslid");

		PersoneelDaoAdapter pdao = new PersoneelDaoAdapter();
		List<Personeel> lijst = new ArrayList<Personeel>();
		PersoonAdresDaoAdapter persoonAdresDao = new PersoonAdresDaoAdapter();

		pdao.verwijder(id);

		ArrayList<Adres> adreslijst = p.getAdreslijst();
		ListIterator<Adres> it = adreslijst.listIterator();
		while (it.hasNext()) {
			Adres a = it.next();

			persoonAdresDao.verwijder(a.getId());
		}

		lijst = (List<Personeel>) (Object) pdao.leesAlle();

		session.setAttribute("lijst", lijst);

		RequestDispatcher view = request.getRequestDispatcher("/Personeelsbeheer.jsp");
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

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

import be.miras.programs.frederik.dao.DbOpdrachtMateriaalDao;
import be.miras.programs.frederik.dao.adapter.MateriaalDaoAdapter;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.util.Datatype;
/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class MateriaalVerwijderenServlet
 */
@WebServlet("/MateriaalVerwijderenServlet")
public class MateriaalVerwijderenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MateriaalVerwijderenServlet() {
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

		DbOpdrachtMateriaalDao dbOpdrachtMateriaalDao = new DbOpdrachtMateriaalDao();
		boolean isKomtVoor = dbOpdrachtMateriaalDao.isMateriaalKomtVoor(id);
		
		if (isKomtVoor){
			String errorMsg = "Kan dit materiaal niet verwijderen omdat deze nog gebruikt wordt in een opdracht.";
			request.setAttribute("inputValidatieErrorMsg", errorMsg);
		} else {
			MateriaalDaoAdapter dao = new MateriaalDaoAdapter();
			dao.verwijder(id);
		}
		
		MateriaalDaoAdapter dao = new MateriaalDaoAdapter();
		
		Materiaal m = new Materiaal();
		// voor nieuw materiaal : id = -1
		m.setId(-1);

		List<Materiaal> lijst = new ArrayList<Materiaal>();
				
		lijst = (List<Materiaal>) (Object) dao.leesAlle();
		
		request.setAttribute("materiaalLijst", lijst);
		request.setAttribute("materiaal", m);
		
		RequestDispatcher view = request.getRequestDispatcher("/Materiaalbeheer.jsp");
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

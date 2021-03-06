package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
import be.miras.programs.frederik.model.TypeMateriaal;
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

		int id = Integer.valueOf(request.getParameter("materiaalId"));
		int typeMateriaalId = Integer.valueOf(request.getParameter("typeMateriaalId"));

		DbOpdrachtMateriaalDao dbOpdrachtMateriaalDao = new DbOpdrachtMateriaalDao();
		MateriaalDaoAdapter dao = new MateriaalDaoAdapter();
		Materiaal m = new Materiaal();
		List<TypeMateriaal> lijst = new ArrayList<TypeMateriaal>();
		String tabKiezer = "";
		
		boolean isKomtVoor = dbOpdrachtMateriaalDao.isMateriaalKomtVoor(id);
		
		if (isKomtVoor){
			
			String errorMsg = "Kan dit materiaal niet verwijderen omdat deze nog gebruikt wordt in een opdracht.";
			request.setAttribute("inputValidatieErrorMsg", errorMsg);
		} else {
			
			dao.verwijder(id);
		}
		
		// voor nieuw materiaal : id = -1
		m.setId(-1);
	
		lijst = (List<TypeMateriaal>) (Object) dao.leesAlle();
		
		Iterator<TypeMateriaal> it = lijst.iterator();
		while (it.hasNext()) {
			TypeMateriaal tm = it.next();
			if (tm.getId() == typeMateriaalId) {
				
				tabKiezer = tm.getNaam();
			}
		}
		
		request.setAttribute("tabKiezer", tabKiezer);
		request.setAttribute("typeMateriaalLijst", lijst);
		request.setAttribute("materiaal", m);
		
		RequestDispatcher view = request.getRequestDispatcher("/Materiaalbeheer.jsp");
		view.forward(request, response);
	}
	
}

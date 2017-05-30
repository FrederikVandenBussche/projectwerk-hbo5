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
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.MateriaalDaoAdapter;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.model.TypeMateriaal;
import be.miras.programs.frederik.util.Datatype;

/**
 * @author Frederik Vanden Bussche
 * 
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

			int materiaalId = Datatype.stringNaarInt(request.getParameter("materiaalId"));
			int typeMateriaalId = Datatype.stringNaarInt(request.getParameter("typeMateriaalId"));
			
			MateriaalDaoAdapter dao = new MateriaalDaoAdapter();
			List<TypeMateriaal> lijst = new ArrayList<TypeMateriaal>();
			Materiaal materiaal = new Materiaal();
			String tabKiezer = null;
			
			lijst = (List<TypeMateriaal>) (Object) dao.leesAlle();
			
			Iterator<TypeMateriaal> it = lijst.iterator();
			while (it.hasNext()) {
				TypeMateriaal tm = it.next();
				if (tm.getId() == typeMateriaalId) {
					
					tabKiezer = tm.getNaam();
					
					Iterator<Materiaal> materiaalIt = tm.getMateriaalLijst().iterator();
					while (materiaalIt.hasNext()){
						Materiaal m = materiaalIt.next();
						if(m.getId() == materiaalId){
							materiaal = m;
						}
					}
				}
			}

			request.setAttribute("tabKiezer", tabKiezer);
			request.setAttribute("typeMateriaalLijst", lijst);
			request.setAttribute("materiaal", materiaal);

			view = request.getRequestDispatcher("/Materiaalbeheer.jsp");

		}
		view.forward(request, response);
	}

	
}

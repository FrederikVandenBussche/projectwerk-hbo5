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

import be.miras.programs.frederik.dao.DbMateriaalDao;
import be.miras.programs.frederik.dao.DbOpdrachtMateriaalDao;
import be.miras.programs.frederik.dao.adapter.MateriaalDaoAdapter;
import be.miras.programs.frederik.dbo.DbMateriaal;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.model.TypeMateriaal;

/**
 * Servlet implementation class TypeMateriaalVerwijderenServlet
 */
@WebServlet("/TypeMateriaalVerwijderenServlet")
public class TypeMateriaalVerwijderenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TypeMateriaalVerwijderenServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		int typeMateriaalId = Integer.valueOf(request.getParameter("typeMateriaalId"));

		DbOpdrachtMateriaalDao dbOpdrachtMateriaalDao = new DbOpdrachtMateriaalDao();
		DbMateriaalDao dbMateriaalDao = new DbMateriaalDao();
		MateriaalDaoAdapter dao = new MateriaalDaoAdapter();
		Materiaal m = new Materiaal();
		
		List<TypeMateriaal> lijst = new ArrayList<TypeMateriaal>();
		String tabKiezer = "";
		
		List<DbMateriaal> dbMateriaalLijst = dbMateriaalDao.leesWaarTypeMateriaalId(typeMateriaalId);
		
		boolean isKomtVoor = false;
		
		Iterator<DbMateriaal> dbMateriaalLijstIt = dbMateriaalLijst.iterator();
		while(dbMateriaalLijstIt.hasNext()){
			DbMateriaal dbMateriaal = dbMateriaalLijstIt.next();
			
			if (dbOpdrachtMateriaalDao.isMateriaalKomtVoor(dbMateriaal.getId())){
				isKomtVoor = true;
			}
		}
		
		if (isKomtVoor){
			
			String errorMsg = "Kan deze categorie niet verwijderen omdat er nog materiaal uit deze categorie gebruikt wordt in een opdracht.";
			request.setAttribute("inputValidatieErrorMsg", errorMsg);
		} else {
			
			dao.verwijderTypeMateriaal(typeMateriaalId);
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

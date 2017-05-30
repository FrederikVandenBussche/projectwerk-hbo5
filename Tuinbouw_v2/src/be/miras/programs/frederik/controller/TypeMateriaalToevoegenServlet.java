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

import be.miras.programs.frederik.dao.DbTypeMateriaalDao;
import be.miras.programs.frederik.dao.adapter.MateriaalDaoAdapter;
import be.miras.programs.frederik.dbo.DbTypeMateriaal;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.model.TypeMateriaal;
import be.miras.programs.frederik.util.InputValidatie;
import be.miras.programs.frederik.util.InputValidatieStrings;

/**
 * Servlet implementation class TypeMateriaalToevoegenServlet
 */
@WebServlet("/TypeMateriaalToevoegenServlet")
public class TypeMateriaalToevoegenServlet extends HttpServlet  implements IinputValidatie{
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TypeMateriaalToevoegenServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		String naam = request.getParameter("naam").trim();

		MateriaalDaoAdapter dao = new MateriaalDaoAdapter();
		List<TypeMateriaal> lijst = new ArrayList<TypeMateriaal>();
		Materiaal materiaal = new Materiaal();
		String tabKiezer = "";
		
		String inputValidatieErrorMsg = inputValidatie(new String[]{naam});
		
		if (inputValidatieErrorMsg.isEmpty()) {
			DbTypeMateriaalDao dbTypeMateriaalDao = new DbTypeMateriaalDao();
			DbTypeMateriaal dbTypeMateriaal = new DbTypeMateriaal();
			dbTypeMateriaal.setNaam(naam);
			dbTypeMateriaalDao.voegToe(dbTypeMateriaal);
			
			tabKiezer = naam;
			
		} else {
			
			tabKiezer = "nieuweSoort";
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);	
		}
			
		lijst = (List<TypeMateriaal>) (Object) dao.leesAlle();
		materiaal.setId(-1);
		
		request.setAttribute("tabKiezer", tabKiezer);
		request.setAttribute("typeMateriaalLijst", lijst);
		request.setAttribute("materiaal", materiaal);
		
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

	@Override
	public String inputValidatie(String[] teValideren) {
		String naam = teValideren[0];
		
		String inputValidatieErrorMsg = "";
		String msg = null;
	
		msg = InputValidatie.ingevuld(naam);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Naam).concat(msg);
		}
				
		return inputValidatieErrorMsg;
	}
			
				
}

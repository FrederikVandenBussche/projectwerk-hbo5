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

import be.miras.programs.frederik.dao.adapter.PersoonAdresDaoAdapter;
import be.miras.programs.frederik.dao.DbWerknemerDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dao.adapter.PersoneelDaoAdapter;
import be.miras.programs.frederik.model.Personeel;
import be.miras.programs.frederik.util.Datatype;

/**
 * @author Frederik Vanden Bussche
 * 
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
		
		PersoneelDaoAdapter personeelDaoAdapter = new PersoneelDaoAdapter();
		PersoonAdresDaoAdapter persoonAdresDaoAdapter = new PersoonAdresDaoAdapter();

		DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
		DbWerknemerDao dbWerknemerDao = new DbWerknemerDao();
		int werknemerId = dbWerknemerDao.returnWerknemerId(id);
		
		boolean isKomtVoor = dbWerknemerOpdrachtTaakDao.isWerknemerKomtVoor(werknemerId);
		
		Personeel personeel = (Personeel) personeelDaoAdapter.lees(id);
		
		RequestDispatcher view = null;
		
		if (isKomtVoor){
			String errorMsg = "Kan dit personeelslid niet verwijderen omdat deze nog taken moet uitvoeren / uitgevoerd heeft.";
			
			request.setAttribute("personeelslid", personeel);
			
			request.setAttribute("inputValidatieErrorMsg", errorMsg);
			
			view = request.getRequestDispatcher("/PersoneelDetail.jsp");
		} else {
			
			persoonAdresDaoAdapter.verwijderVanPersoon(id);
			
			personeelDaoAdapter.verwijder(id);
			
			List<Personeel> personeelLijst = new ArrayList<Personeel>();
			personeelLijst = (List<Personeel>) (Object) personeelDaoAdapter.leesAlle();
			
			request.setAttribute("personeelLijst", personeelLijst);
			
			view = request.getRequestDispatcher("/Personeelsbeheer.jsp");
		}
		
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

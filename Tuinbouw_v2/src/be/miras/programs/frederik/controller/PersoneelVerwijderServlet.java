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
import be.miras.programs.frederik.dao.DbWerknemerDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dao.adapter.PersoneelDaoAdapter;
import be.miras.programs.frederik.model.Adres;
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

		HttpSession session = request.getSession();
		Personeel p = (Personeel) session.getAttribute("personeelslid");

		PersoneelDaoAdapter pdao = new PersoneelDaoAdapter();
		List<Personeel> lijst = new ArrayList<Personeel>();
		PersoonAdresDaoAdapter persoonAdresDao = new PersoonAdresDaoAdapter();

		DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
		DbWerknemerDao dbWerknemerDao = new DbWerknemerDao();
		int werknemerId = dbWerknemerDao.returnWerknemerId(id);
		boolean isKomtVoor = dbWerknemerOpdrachtTaakDao.isWerknemerKomtVoor(werknemerId);
		
		if (isKomtVoor){
			String errorMsg = "Kan dit personeelslid niet verwijderen omdat deze nog taken moet uitvoeren / uitgevoerd heeft.";
			request.setAttribute("inputValidatieErrorMsg", errorMsg);
		} else {
			
			Thread thread = new Thread(new Runnable(){

				@Override
				public void run() {
					pdao.verwijder(id);
					
				}
			});
			thread.start();
			

			ArrayList<Adres> adreslijst = p.getAdreslijst();
			ListIterator<Adres> it = adreslijst.listIterator();
			while (it.hasNext()) {
				Adres a = it.next();

				persoonAdresDao.verwijder(a.getId());
			}

			lijst = (List<Personeel>) (Object) pdao.leesAlle();
		}

		session.setAttribute("personeelLijst", lijst);

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

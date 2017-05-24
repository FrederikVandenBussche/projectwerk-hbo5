package be.miras.programs.frederik.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.PersoneelDaoAdapter;
import be.miras.programs.frederik.dao.adapter.PersoonAdresDaoAdapter;
import be.miras.programs.frederik.model.Personeel;
import be.miras.programs.frederik.util.Datatype;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class AdresVerwijderenServlet
 */
@WebServlet("/AdresVerwijderenServlet")
public class PersoneelAdresVerwijderenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersoneelAdresVerwijderenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String aanspreeknaam = request.getParameter("aanspreeknaam");
		String opslaanBtnNaam = request.getParameter("buttonNaam");
		int adresId = Datatype.stringNaarInt(request.getParameter("adres_id"));
		HttpSession session = request.getSession();
		int persoonId = (int) session.getAttribute("id");

		PersoonAdresDaoAdapter persoonAdresDaoAdapter = new PersoonAdresDaoAdapter();
		PersoneelDaoAdapter personeelDaoAdapter = new PersoneelDaoAdapter();

		persoonAdresDaoAdapter.verwijder(adresId);
		
		Personeel personeel = (Personeel) personeelDaoAdapter.lees(persoonId);
		
		request.setAttribute("tabKiezer", "adres");
		request.setAttribute("personeelslid", personeel);
		request.setAttribute("aanspreeknaam", aanspreeknaam);
		request.setAttribute("buttonNaam", opslaanBtnNaam);

		RequestDispatcher view = request.getRequestDispatcher("/PersoneelDetail.jsp");
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

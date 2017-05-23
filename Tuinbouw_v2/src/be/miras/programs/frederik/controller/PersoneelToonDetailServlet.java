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
import be.miras.programs.frederik.model.Personeel;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class PersoneelToonDetailServlet
 */
@WebServlet("/PersoneelToonDetailServlet")
public class PersoneelToonDetailServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersoneelToonDetailServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		int id = Integer.parseInt(request.getParameter("id"));

		// bovenaan de content wordt de aanspreeknaam van een personeelslid
		// weergegevne.
		String aanspreeknaam = null;
		String buttonNaam = null; // 'Voeg toe' of 'Wijziging opslaan'
		Personeel personeelslid = new Personeel();

		if (id == -1) {
			/*
			 * het gaat om een nieuw personeelslid. De naam van een
			 * personeelslid word weergegeven bovenaan de content van
			 * 'PersoneelDetail.jsp'. Daarom stellen we nu de voornaam van het
			 * personeelslid tijdelijk in.
			 */
			aanspreeknaam = "";
			buttonNaam = "Voeg toe";

		} else {
			// het gaat niet om een nieuw personeelslid.
			PersoneelDaoAdapter personeelDaoAdapter = new PersoneelDaoAdapter();
			personeelslid = (Personeel) personeelDaoAdapter.lees(id);

			aanspreeknaam = personeelslid.getVoornaam() + " " + personeelslid.getNaam();
			buttonNaam = "Wijziging opslaan";

			

		}

		HttpSession session = request.getSession();

		session.setAttribute("id", id);
		session.setAttribute("aanspreeknaam", aanspreeknaam);
		session.setAttribute("buttonNaam", buttonNaam);
		request.setAttribute("personeelslid", personeelslid);

		RequestDispatcher view = this.getServletContext().getRequestDispatcher("/PersoneelDetail.jsp");
		view.forward(request, response);
	}


}
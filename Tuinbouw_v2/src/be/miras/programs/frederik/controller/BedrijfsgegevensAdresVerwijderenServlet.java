package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.PersoonAdresDaoAdapter;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Werkgever;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class BedrijfsgegevensAdresVerwijderenServlet
 */
@WebServlet("/BedrijfsgegevensAdresVerwijderenServlet")
public class BedrijfsgegevensAdresVerwijderenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BedrijfsgegevensAdresVerwijderenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		int adresId = Datatype.stringNaarInt(request.getParameter("adres_id"));

		PersoonAdresDaoAdapter adao = new PersoonAdresDaoAdapter();
		adao.verwijder(adresId);

		HttpSession session = request.getSession();
		Werkgever werkgever = (Werkgever) session.getAttribute("werkgever");

		ArrayList<Adres> adreslijst = werkgever.getAdreslijst();
		ListIterator<Adres> it = adreslijst.listIterator();
		while (it.hasNext()) {
			Adres a = it.next();
			if (a.getId() == adresId) {
				it.remove();
			}
		}
		werkgever.setAdreslijst(adreslijst);

		session.setAttribute("werkgever", werkgever);

		RequestDispatcher view = request.getRequestDispatcher("/bedrijfsgegevensMenu");
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

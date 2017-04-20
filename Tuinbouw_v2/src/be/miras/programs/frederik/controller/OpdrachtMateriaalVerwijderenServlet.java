package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbOpdrachtMateriaalDao;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class OpdrachtMateriaalVerwijderenServlet
 */
@WebServlet("/OpdrachtMateriaalVerwijderenServlet")
public class OpdrachtMateriaalVerwijderenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "OpdrachtMateriaalVerwijderenServlet: ";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OpdrachtMateriaalVerwijderenServlet() {
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
		System.out.println(TAG + "de id = " + id);

		HttpSession session = request.getSession();
		OpdrachtDetailData opdrachtDetailData = (OpdrachtDetailData) session.getAttribute("opdrachtDetailData");

		DbOpdrachtMateriaalDao dbOpdrachtMateriaalDao = new DbOpdrachtMateriaalDao();

		// verwijderen uit db
		dbOpdrachtMateriaalDao.verwijder(id);

		// verwijdernen uit opdrachtdetailData.opdracht.materiaalLijst
		List<Materiaal> gebruiktemateriaalLijst = opdrachtDetailData.getOpdracht().getGebruiktMateriaalLijst();

		ListIterator<Materiaal> it = gebruiktemateriaalLijst.listIterator();
		while (it.hasNext()) {
			Materiaal m = it.next();
			if (m.getId() == id) {
				it.remove();
			}

		}

		RequestDispatcher view = request.getRequestDispatcher("/OpdrachtDetail.jsp");
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

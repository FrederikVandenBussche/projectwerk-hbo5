package be.miras.programs.frederik.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbOpdrachtMateriaalDao;
import be.miras.programs.frederik.dao.adapter.OpdrachtDetailDaoAdapter;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.util.Datatype;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class OpdrachtMateriaalVerwijderenServlet
 */
@WebServlet("/OpdrachtMateriaalVerwijderenServlet")
public class OpdrachtMateriaalVerwijderenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	
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

		HttpSession session = request.getSession();
		int opdrachtId = (int) session.getAttribute("id");

		DbOpdrachtMateriaalDao dbOpdrachtMateriaalDao = new DbOpdrachtMateriaalDao();
		OpdrachtDetailDaoAdapter opdrachtDetailDaoAdapter = new OpdrachtDetailDaoAdapter();

		dbOpdrachtMateriaalDao.verwijderWaarOpdrachtIdEnMateriaalId(opdrachtId, id);
		
		OpdrachtDetailData opdrachtDetailData = opdrachtDetailDaoAdapter.haalOpdrachtdetailDataOp(opdrachtId);
		
		request.setAttribute("tabKiezer", "materialen");
		request.setAttribute("opdrachtDetailData", opdrachtDetailData);
		
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

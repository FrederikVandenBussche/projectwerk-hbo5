package be.miras.programs.frederik.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.OpdrachtDetailDaoAdapter;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.util.Datatype;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class OpdrachtToonDetailServlet
 */
@WebServlet("/OpdrachtToonDetailServlet")
public class OpdrachtToonDetailServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OpdrachtToonDetailServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		int id = Datatype.stringNaarInt(request.getParameter("id"));

		HttpSession session = request.getSession();
		OpdrachtDetailDaoAdapter opdrachtDetailDaoAdapter = new OpdrachtDetailDaoAdapter();
		
		OpdrachtDetailData opdrachtDetailData = opdrachtDetailDaoAdapter.haalOpdrachtdetailDataOp(id);
		
		request.setAttribute("opdrachtDetailData", opdrachtDetailData);
		session.setAttribute("id", id);

		RequestDispatcher view = request.getRequestDispatcher("/OpdrachtDetail.jsp");
		view.forward(request, response);
	}
	
	
}

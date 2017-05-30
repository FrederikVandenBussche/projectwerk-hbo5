package be.miras.programs.frederik.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbOpdrachtTaakDao;
import be.miras.programs.frederik.dao.DbStatusDao;
import be.miras.programs.frederik.dao.DbVooruitgangDao;
import be.miras.programs.frederik.dao.adapter.OpdrachtDetailDaoAdapter;
import be.miras.programs.frederik.dbo.DbOpdrachtTaak;
import be.miras.programs.frederik.dbo.DbVooruitgang;
import be.miras.programs.frederik.model.OpdrachtDetailData;

/**
 * Servlet implementation class taakMarkerenAlsAfgewerktServlet
 */
@WebServlet("/taakMarkerenAlsAfgewerktServlet")
public class TaakMarkerenAlsAfgewerktServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TaakMarkerenAlsAfgewerktServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		HttpSession session = request.getSession();
		int opdrachtId = (int) session.getAttribute("id");
		int taakId = Integer.parseInt(request.getParameter("taakId"));
		
		DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
		DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
		DbStatusDao dbStatusDao = new DbStatusDao();
		OpdrachtDetailDaoAdapter opdrachtDetailDaoAdapter = new OpdrachtDetailDaoAdapter();
		
		int statusId = dbStatusDao.lees("Afgewerkt");
		DbOpdrachtTaak dbOpdrachtTaak = dbOpdrachtTaakDao.leesWaarTaakIdEnOpdrachtId(taakId, opdrachtId);
		int vooruitgangId = dbOpdrachtTaak.getVooruitgangId();
		DbVooruitgang dbVooruitgang = new DbVooruitgang();
		dbVooruitgang.setId(vooruitgangId);
		dbVooruitgang.setPercentage(100);
		dbVooruitgang.setStatusId(statusId);
		dbVooruitgangDao.wijzig(dbVooruitgang);
		
		OpdrachtDetailData opdrachtDetailData = opdrachtDetailDaoAdapter.haalOpdrachtdetailDataOp(opdrachtId);
		
		request.setAttribute("opdrachtDetailData", opdrachtDetailData);
		
		RequestDispatcher view = request.getRequestDispatcher("/OpdrachtDetail.jsp");
		view.forward(request, response);
	}

	
}

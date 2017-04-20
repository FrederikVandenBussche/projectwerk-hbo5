package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbStatusDao;
import be.miras.programs.frederik.dao.adapter.TaakDaoAdapter;
import be.miras.programs.frederik.dbo.DbStatus;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.model.Taak;

/**
 * Servlet implementation class TaakOpslaanServlet
 */
@WebServlet("/TaakOpslaanServlet")
public class TaakOpslaanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "TaakOpslaanServet: ";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TaakOpslaanServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String taaknaam = request.getParameter("taaknaam").trim();
		String opmerking = request.getParameter("opmerking").trim();

		HttpSession session = request.getSession();
		Taak taak = (Taak) session.getAttribute("taak");
		OpdrachtDetailData opdrachtDetailData = (OpdrachtDetailData) session.getAttribute("opdrachtDetailData");

		TaakDaoAdapter taakDaoAdapter = new TaakDaoAdapter();

		List<Taak> takenlijst = opdrachtDetailData.getOpdracht().getTaakLijst();

		int id = taak.getId();

		if (id < 0) {
			// een nieuwe taak toevoegen
			taak.setTaakNaam(taaknaam);
			taak.setOpmerking(opmerking);
			taak.setOpdrachtId(opdrachtDetailData.getOpdracht().getId());

			taakDaoAdapter.voegToe(taak);
			System.out.println(TAG + "takenlijst.size() = " + takenlijst.size());
			taak.setId(takenlijst.size() + 1);
			DbStatusDao dbStatusDao = new DbStatusDao();
			// een nieuwe taak heeft als status de waarde van DbStatus met id =
			// 1
			DbStatus dbStatus = (DbStatus) dbStatusDao.lees(1);
			taak.setStatus(dbStatus.getNaam());
			// een nieuwe taak heeft een vooruitgangspercentage van 0
			taak.setVooruitgangPercentage(0);
			takenlijst.add(taak);
		} else {
			// het betreft een bestaande taak
			// indien er iets gewijzigd werd, de wijzigingen aanpassen

			if (!taak.getTaakNaam().equals(taaknaam) || !taak.getOpmerking().equals(opmerking)) {
				taak.setTaakNaam(taaknaam);
				taak.setOpmerking(opmerking);
				taakDaoAdapter.wijzig(taak);

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

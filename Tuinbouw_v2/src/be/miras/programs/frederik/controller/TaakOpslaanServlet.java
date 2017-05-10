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
import be.miras.programs.frederik.dao.DbTaakDao;
import be.miras.programs.frederik.dao.adapter.TaakDaoAdapter;
import be.miras.programs.frederik.dbo.DbStatus;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.InputValidatie;
import be.miras.programs.frederik.util.InputValidatieStrings;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class TaakOpslaanServlet
 */
@WebServlet("/TaakOpslaanServlet")
public class TaakOpslaanServlet extends HttpServlet implements IinputValidatie {
	private static final long serialVersionUID = 1L;

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

		String inputValidatieErrorMsg = inputValidatie(new String[] { taaknaam });

		RequestDispatcher view = null;

		if (inputValidatieErrorMsg.isEmpty()) {

			HttpSession session = request.getSession();
			Taak taak = (Taak) session.getAttribute("taak");
			OpdrachtDetailData opdrachtDetailData = (OpdrachtDetailData) session.getAttribute("opdrachtDetailData");

			TaakDaoAdapter taakDaoAdapter = new TaakDaoAdapter();

			List<Taak> takenlijst = opdrachtDetailData.getOpdracht().getTaakLijst();

			int id = taak.getId();
			
			if (id < 0) {
				
				// een nieuwe taak toevoegen

				DbTaakDao dbTaakDao = new DbTaakDao();

				taak.setTaakNaam(taaknaam);
				taak.setOpmerking(opmerking);
				taak.setOpdrachtId(opdrachtDetailData.getOpdracht().getId());

				int taakId = taakDaoAdapter.voegToe(taak);
				taak.setId(taakId);
				DbStatusDao dbStatusDao = new DbStatusDao();
				// een nieuwe taak heeft als status de waarde van DbStatus met
				// id =
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
			
			session.setAttribute("taak", taak);
			view = request.getRequestDispatcher("/OpdrachtDetail.jsp");
		} else {
			
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
			view = request.getRequestDispatcher("/Taakbeheer.jsp");
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

	@Override
	public String inputValidatie(String[] teValideren) {
		String taaknaam = teValideren[0];

		String inputValidatieErrorMsg = "";

		String msg = null;

		msg = InputValidatie.enkelAlfabetisch(taaknaam);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.Taaknaam).concat(msg);
		}

		return inputValidatieErrorMsg;
	}

	
}

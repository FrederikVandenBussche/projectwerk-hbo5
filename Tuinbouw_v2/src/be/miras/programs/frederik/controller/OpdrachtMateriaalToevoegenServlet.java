package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbOpdrachtMateriaalDao;
import be.miras.programs.frederik.dao.adapter.MateriaalDaoAdapter;
import be.miras.programs.frederik.dbo.DbOpdrachtMateriaal;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.InputValidatie;

/**
 * Servlet implementation class OpdrachtMateriaalToevoegenServlet
 */
@WebServlet("/OpdrachtMateriaalToevoegenServlet")
public class OpdrachtMateriaalToevoegenServlet extends HttpServlet implements IinputValidatie {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OpdrachtMateriaalToevoegenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		int materiaalId = Datatype.stringNaarInt(request.getParameter("materialen"));
		String hoeveelheidString = request.getParameter("hoeveelheid").trim();
		
		String inputValidatieErrorMsg = inputValidatie(
				new String[]{hoeveelheidString});
		
		
		if (inputValidatieErrorMsg.isEmpty()) {
			HttpSession session = request.getSession();
			OpdrachtDetailData opdrachtDetailData = (OpdrachtDetailData) session.getAttribute("opdrachtDetailData");

			int hoeveelheid = Datatype.stringNaarInt(hoeveelheidString);

			Materiaal materiaal = new Materiaal();
			DbOpdrachtMateriaal dbOpdrachtMateriaal = new DbOpdrachtMateriaal();
			DbOpdrachtMateriaalDao dbOpdrachtMateriaalDao = new DbOpdrachtMateriaalDao();
			MateriaalDaoAdapter materiaalDaoAdapter = new MateriaalDaoAdapter();

			List<Materiaal> gebruiktemateriaalLijst = null;

			int opdrachtId = opdrachtDetailData.getOpdracht().getId();
			List<Materiaal> materiaalLijst = (List<Materiaal>) (Object) materiaalDaoAdapter.leesAlle();

			Iterator<Materiaal> it = materiaalLijst.iterator();
			while (it.hasNext()) {
				Materiaal m = it.next();
				if (m.getId() == materiaalId) {
					materiaal = m;
				}
			}

			// toevoegen aan databank
			dbOpdrachtMateriaal.setOpdrachtId(opdrachtId);
			dbOpdrachtMateriaal.setMateriaalId(materiaal.getId());
			dbOpdrachtMateriaal.setVerbruik(hoeveelheid);

			dbOpdrachtMateriaalDao.voegToe(dbOpdrachtMateriaal);

			// toevoegen aan opdrachtdetailData.opdracht.materiaalLijst
			gebruiktemateriaalLijst = opdrachtDetailData.getOpdracht().getGebruiktMateriaalLijst();

			// System.out.println(TAG + "de lijst met gebruikte materialen = " +
			// gebruiktemateriaalLijst.size());
			materiaal.setId(dbOpdrachtMateriaal.getId());
			materiaal.setHoeveelheid(hoeveelheid);

			gebruiktemateriaalLijst.add(materiaal);
			opdrachtDetailData.getOpdracht().setGebruiktMateriaalLijst(gebruiktemateriaalLijst);
			session.setAttribute("opdrachtDetailData", opdrachtDetailData);

		} else {
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
			
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

	@Override
	public String inputValidatie(String[] teValideren) {
		String hoeveelheidString = teValideren[0];

		String inputValidatieErrorMsg = "";
		
		String msg = null;
		
		msg = InputValidatie.geheelGetal(hoeveelheidString);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(" Straat").concat(msg);
		}
		
		return inputValidatieErrorMsg;

	}

}

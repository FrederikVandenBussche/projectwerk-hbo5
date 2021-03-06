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
import be.miras.programs.frederik.dao.adapter.OpdrachtDetailDaoAdapter;
import be.miras.programs.frederik.dbo.DbOpdrachtMateriaal;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.InputValidatieStrings;
import be.miras.programs.frederik.util.InputValidatie;

/**
 * @author Frederik Vanden Bussche
 * 
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
		
		HttpSession session = request.getSession();
		int opdrachtId = (int) session.getAttribute("id");
		
		OpdrachtDetailDaoAdapter opdrachtDetailDaoAdapter = new OpdrachtDetailDaoAdapter();
		
		String inputValidatieErrorMsg = inputValidatie(
				new String[]{hoeveelheidString});
		
		if (inputValidatieErrorMsg.isEmpty()) {
			
			double hoeveelheid = Datatype.stringNaarDouble(hoeveelheidString);

			Materiaal materiaal = new Materiaal();
			DbOpdrachtMateriaal dbOpdrachtMateriaal = new DbOpdrachtMateriaal();
			DbOpdrachtMateriaalDao dbOpdrachtMateriaalDao = new DbOpdrachtMateriaalDao();
			MateriaalDaoAdapter materiaalDaoAdapter = new MateriaalDaoAdapter();

			//zoek materiaalId
			List<Materiaal> materiaalLijst = materiaalDaoAdapter.leesAlleMaterialen();

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
			
			DbOpdrachtMateriaal dbOpdrachtMateriaalDuplicaat = 
					dbOpdrachtMateriaalDao.leesIdWaarOpdrachtIdEnMateriaalId(opdrachtId, materiaal.getId());
			
			if (dbOpdrachtMateriaalDuplicaat.getId() <= 0){
				
				dbOpdrachtMateriaalDao.voegToe(dbOpdrachtMateriaal);
			} else {
				
				double verbruik = hoeveelheid + dbOpdrachtMateriaalDuplicaat.getVerbruik();
				dbOpdrachtMateriaal.setId(dbOpdrachtMateriaalDuplicaat.getId());
				dbOpdrachtMateriaal.setVerbruik(verbruik);
				dbOpdrachtMateriaalDao.wijzig(dbOpdrachtMateriaal);
			}
		} else {
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
			
		}
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

	@Override
	public String inputValidatie(String[] teValideren) {
		String hoeveelheidString = teValideren[0];

		String inputValidatieErrorMsg = "";
		
		String msg = null;
		
		msg = InputValidatie.kommagetal(hoeveelheidString);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.HoeveelheidGeheelGetal).concat(msg);
		}
		
		return inputValidatieErrorMsg;
	}

	
}

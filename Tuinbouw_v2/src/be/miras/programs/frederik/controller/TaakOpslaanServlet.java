package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.adapter.TaakDaoAdapter;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.Datatype;
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
		
		int taakId = Integer.parseInt(request.getParameter("taakId"));
		String taaknaam = request.getParameter("taaknaam").trim();
		String opmerking = request.getParameter("opmerking").trim();
		
		TaakDaoAdapter taakDaoAdapter = new TaakDaoAdapter();
		Taak taak = null;
		
		String inputValidatieErrorMsg = inputValidatie(new String[] { taaknaam });
		
		HttpSession session = request.getSession();
		int opdrachtId = (int) session.getAttribute("id");

		if (inputValidatieErrorMsg.isEmpty()) {
			
			if (taakId < 0) {
				
				// een nieuwe taak toevoegen
				taak = new Taak();
				taak.setOpdrachtId(opdrachtId);
				taak.setTaakNaam(taaknaam);
				taak.setOpmerking(opmerking);

				int bestaandeTaakId = taakDaoAdapter.haalId(taak);
				if (bestaandeTaakId < 0){
					
					taakId = taakDaoAdapter.voegToe(taak);
				} else {
					
					taakId = bestaandeTaakId;
					
					taak.setId(taakId);
					taakDaoAdapter.voegToe(taak);
				}
				
				taak = taakDaoAdapter.haalTaak(taakId, opdrachtId);
				
			} else {
				
				// het betreft een bestaande taak
				// indien er iets gewijzigd werd, de wijzigingen aanpassen
				taak = taakDaoAdapter.haalTaak(taakId, opdrachtId);
				
				if (!taak.getTaakNaam().equals(taaknaam) || !taak.getOpmerking().equals(opmerking)) {
					
					taak.setTaakNaam(taaknaam);
					taak.setOpmerking(opmerking);
					taak.setOpdrachtId(opdrachtId);
					
					taakDaoAdapter.wijzig(taak);
					
				}
			}
	
		} else {
			
			taak = taakDaoAdapter.haalTaak(taakId, opdrachtId);
			
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
		}
		
		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
		DbKlantDao dbKlantDao = new DbKlantDao();
		
		String[] klantIdEnNaam = dbOpdrachtDao.selectKlantIdEnNaam(opdrachtId);
		int klantId = Datatype.stringNaarInt(klantIdEnNaam[0]);
		String opdrachtNaam = klantIdEnNaam[1];
		
		DbKlant dbKlant = (DbKlant) dbKlantDao.lees(klantId);
		String klantNaam = dbKlant.geefAanspreekNaam();
		
		// lijst van alle werknemers
		HashMap<Integer, String> werknemerMap = taakDaoAdapter.geefWerknemerMap();
		
		request.setAttribute("klantNaam", klantNaam);
		request.setAttribute("opdrachtNaam", opdrachtNaam);
		request.setAttribute("taak", taak);
		request.setAttribute("id", taakId);
		request.setAttribute("werknemerMap", werknemerMap);
		
		RequestDispatcher view = request.getRequestDispatcher("/Taakbeheer.jsp");
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

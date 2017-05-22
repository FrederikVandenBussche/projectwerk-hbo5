package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.Date;
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
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dao.adapter.TaakDaoAdapter;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbWerknemerOpdrachtTaak;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.Datum;
import be.miras.programs.frederik.util.InputValidatie;
import be.miras.programs.frederik.util.InputValidatieStrings;
import be.miras.programs.frederik.util.Datatype;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class TaakPlanningToevoegenServlet
 */
@WebServlet("/TaakPlanningToevoegenServlet")
public class TaakPlanningToevoegenServlet extends HttpServlet implements IinputValidatie{
	private static final long serialVersionUID = 1L;
	
	private Date startVanOpdracht;
	private Date eindeVanOpdracht;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TaakPlanningToevoegenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		int werknemerId = Datatype.stringNaarInt(request.getParameter("werknemer"));
		String datumString = request.getParameter("datum").trim();
		
		HttpSession session = request.getSession();
		int opdrachtId = (int) session.getAttribute("id");
		int taakId = Integer.valueOf(request.getParameter("taakId"));
		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
		DbKlantDao dbKlantDao = new DbKlantDao();
		TaakDaoAdapter taakDaoAdapter = new TaakDaoAdapter();
		
		DbOpdracht dbOpdracht = (DbOpdracht) dbOpdrachtDao.lees(opdrachtId);
		
		int klantId = dbOpdracht.getKlantId();
		String opdrachtNaam = dbOpdracht.getNaam();
		
		this.startVanOpdracht = dbOpdracht.getStartdatum();
		this.eindeVanOpdracht = dbOpdracht.getEinddatum();
		
		DbKlant dbKlant = (DbKlant) dbKlantDao.lees(klantId);
		String klantNaam = dbKlant.geefAanspreekNaam();
		
		String inputValidatieErrorMsg = inputValidatie(new String[]{datumString});
		
		if (inputValidatieErrorMsg.isEmpty()) {
			
			DbWerknemerOpdrachtTaak dbWerknemerOpdrachtTaak = new DbWerknemerOpdrachtTaak();
			DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
	
			Date beginuur = Datum.creeerDatum(datumString);

			// DbWerknemerOpdrachtTaak aanmaken en toevoegen aan databank			
			dbWerknemerOpdrachtTaak.setWerknemerId(werknemerId);
			dbWerknemerOpdrachtTaak.setOpdrachtTaakOpdrachtId(opdrachtId);
			dbWerknemerOpdrachtTaak.setOpdrachtTaakTaakId(taakId);
			dbWerknemerOpdrachtTaak.setBeginuur(beginuur);
			
			dbWerknemerOpdrachtTaakDao.voegToe(dbWerknemerOpdrachtTaak);
			
		} else {
			
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
		}
		
		Taak taak = taakDaoAdapter.haalTaak(taakId);
		
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
		String datumString = teValideren[0];
		
		String inputValidatieErrorMsg = "";
		String msg = null;
		
		msg = InputValidatie.correcteDatum(datumString);
		
		if (msg!= null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.GeplandeDatum).concat(msg);
		
		} else {
			
			Date datum = Datum.creeerDatum(datumString);
			Date nu = new Date();
			nu.setDate(nu.getDate() - 1);
			if(datum.before(nu)){
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.GeplandeDatumToekomst);
			}
			
			
			
			if (datum.before(this.startVanOpdracht)){
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.GeplandeDatumNaStartDatumOpdracht
						+ Datum.datumToString(this.startVanOpdracht) + ". ");
			}
			if (datum.after(this.eindeVanOpdracht)){
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.GeplandeDatumVoorEinddatumOpdracht
						+ Datum.datumToString(this.eindeVanOpdracht) + ". ");
			}
		}
		
		return inputValidatieErrorMsg;
	}

	
}

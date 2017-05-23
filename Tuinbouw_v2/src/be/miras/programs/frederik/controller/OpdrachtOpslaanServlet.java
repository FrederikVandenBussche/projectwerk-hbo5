package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbKlantAdresDao;
import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.adapter.AdresDaoAdapter;
import be.miras.programs.frederik.dao.adapter.OpdrachtDetailDaoAdapter;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbKlantAdres;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.util.Datum;
import be.miras.programs.frederik.util.GoogleApis;
import be.miras.programs.frederik.util.InputValidatieStrings;
import be.miras.programs.frederik.util.InputValidatie;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class OpdrachtOpslaanServlet
 */
@WebServlet("/OpdrachtOpslaanServlet")
public class OpdrachtOpslaanServlet extends HttpServlet implements IinputValidatie{
	
	private static final long serialVersionUID = 1L;
	private int id;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OpdrachtOpslaanServlet() {
		super();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String klantNaam = request.getParameter("klanten").trim();
		String opdrachtNaam = request.getParameter("opdrachtNaam").trim();
		String startDatumString = request.getParameter("nieuweStartDatum").trim();
		String eindDatumString = request.getParameter("nieuweEindDatum").trim();
		
		HttpSession session = request.getSession();
		this.id = (int) session.getAttribute("id");
		
		OpdrachtDetailDaoAdapter opdrachtDetailDaoAdapter = new OpdrachtDetailDaoAdapter();
		
		String inputValidatieErrorMsg = inputValidatie(
				new String[]{klantNaam, opdrachtNaam, startDatumString, eindDatumString});
				
		RequestDispatcher view = null;
		
		if (inputValidatieErrorMsg.isEmpty()) {
						
			String adresKeuze = request.getParameter("adressen").trim();

			DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
			DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
			DbKlantDao dbKlantDao = new DbKlantDao();
			AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();
			Opdracht opdracht = new Opdracht();
			// hashmap met klantnamen
			Map<Integer, String> klantNaamMap = new HashMap<Integer, String>();
			HashMap<Integer, String> adresMap = new HashMap<Integer, String>();
			
			int teWijzigenKlantId = Integer.MIN_VALUE;
			int teWijzigenAdresId = Integer.MIN_VALUE;
			
			ArrayList<DbKlant> klantLijst = (ArrayList<DbKlant>) (Object) dbKlantDao.leesAlle();
			
			Iterator<DbKlant> it = klantLijst.iterator();
			while (it.hasNext()) {
				DbKlant dbKlant = it.next();
				
				int itKlantId = dbKlant.getId();
				String itKlantNaam = dbKlant.geefAanspreekNaam();
				
				if (itKlantNaam.equals(klantNaam))
					teWijzigenKlantId = itKlantId;
				
				klantNaamMap.put(itKlantId, itKlantNaam);
			}
			
			int klantId = dbOpdrachtDao.geefKlantId(this.id);

			Date beginDatum = Datum.creeerDatum(startDatumString);
			Date eindDatum = Datum.creeerDatum(eindDatumString);
			
			// hashmap met adressen
			// adreslijst die bij de opdrachtgever van deze opdracht hoort
			// ophalen.
			List<Adres> adresLijst = adresDaoAdapter.leesWaarKlantId(klantId);
			
			Iterator<Adres> adresIter = adresLijst.iterator();
			while (adresIter.hasNext()) {
				Adres adres = adresIter.next();
				
				if (adres.toString().equals(adresKeuze))
					teWijzigenAdresId = adres.getId();
				
				adresMap.put(adres.getId(), adres.toString());
			}

			opdracht.setId(this.id);
			opdracht.setKlantId(klantId);
			opdracht.setKlantNaam(klantNaam);
			
			if (opdrachtNaam == null || opdrachtNaam.isEmpty()) {
				opdrachtNaam = "onbekend";
			}
			
			opdracht.setOpdrachtNaam(opdrachtNaam);
			
			if (beginDatum != null) {
				opdracht.setStartDatum(beginDatum);
			}
			
			if (eindDatum != null) {
				opdracht.setEindDatum(eindDatum);
			}

			if (this.id < 0) {
				/*
				 * een nieuwe opdracht toevoegen
				 */
				DbKlantAdresDao dkad = new DbKlantAdresDao();
				DbKlantAdres dbKlantAdres = (DbKlantAdres) dkad.lees(teWijzigenKlantId);
				System.out.println("klantid = " + teWijzigenKlantId);
				int klantAdresId = dbKlantAdres.getId();
				
				if (klantAdresId <= 0) {
					inputValidatieErrorMsg = "Kan geen opdracht toevoegen aan deze klant " + " omdat er nog geen adres bekent is. <br />"
							+ "Oplossing : voeg een adres toe aan deze klant en probeer opnieuw";
				
				} else {
					DbOpdracht dbOpdracht = new DbOpdracht();
	
					dbOpdracht.setKlantId(teWijzigenKlantId);
					dbOpdracht.setKlantAdresId(klantAdresId);
					dbOpdracht.setNaam(opdrachtNaam);
					dbOpdracht.setStartdatum(beginDatum);
					dbOpdracht.setEinddatum(eindDatum);

					Adres adres = (Adres) adresDaoAdapter.leesWaarKlantAdresId(klantAdresId);

					// aanmaak van adresMap<adresId, adresString>
					List<Integer> adresIdLijst = dbKlantAdresDao.leesLijst(teWijzigenKlantId);

					Iterator<Integer> adresIdIter = adresIdLijst.iterator();
					while (adresIdIter.hasNext()) {
						int adresIdIt = adresIdIter.next();
						Adres a = (Adres) adresDaoAdapter.lees(adresIdIt);
						adresMap.put(adresIdIt, a.toString());
					}

					double[] latlng = GoogleApis.zoeklatlng(adres);
					double latitude = latlng[0];
					double longitude = latlng[1];

					dbOpdracht.setLatitude(latitude);
					dbOpdracht.setLongitude(longitude);

					this.id = dbOpdrachtDao.voegToe(dbOpdracht);
				}

			} else {
				// het betreft een bestaande opdracht
				// indien er iets gewijzigd werd, de wijzigingen opslaan
				
				boolean isVerschillend = false;
				double latitude = 0;
				double longitude = 0;
				
				// Haal de dbOpdracht op die te wijzigen is.
				DbOpdracht dbOpdrachtTeWijzigen = (DbOpdracht) dbOpdrachtDao.lees(this.id);

				/*
				 * indien de klant gewijzigd werd
				 */
				if (teWijzigenKlantId > 0 && dbOpdrachtTeWijzigen.getKlantId() != teWijzigenKlantId) {
					dbOpdrachtTeWijzigen.setKlantId(teWijzigenKlantId);

					// opnieuw aanmaken van adresMap<adresId, adresString>
					adresMap.clear();
					
					List<Integer> adresIdLijst = dbKlantAdresDao.leesLijst(teWijzigenKlantId);
					
					if (adresIdLijst.size() == 0){
						
						inputValidatieErrorMsg = "Kan geen opdracht toevoegen aan deze klant " + " omdat er nog geen adres bekent is. <br />"
								+ "Oplossing : voeg een adres toe aan deze klant en probeer opnieuw";
					} else {
						Adres eersteAdresUitAdreslijst = null;
						
						Iterator<Integer> adresIdIter = adresIdLijst.iterator();
						while (adresIdIter.hasNext()) {
							int adresIdIt = adresIdIter.next();
							Adres a = (Adres) adresDaoAdapter.lees(adresIdIt);
							
							adresMap.put(adresIdIt, a.toString());
							
							// bij het wijzigen van een klant wordt initieel het
							// eerste adres als opdrachtAdres gekozen.
							if (eersteAdresUitAdreslijst == null){
								eersteAdresUitAdreslijst = a;
							}
						}
						
						//de opdracht.klantAdresId aanpassen naar de KlantAdres_Id met
						// de juiste klant_id en het juiste adres_id
						int klantAdresId = dbKlantAdresDao.geefId(teWijzigenKlantId, eersteAdresUitAdreslijst.getId());
						
						dbOpdrachtTeWijzigen.setKlantAdresId(klantAdresId);
						//latlng aanpassen
						double[] latlng = GoogleApis.zoeklatlng(eersteAdresUitAdreslijst);
						latitude = latlng[0];
						longitude = latlng[1];
						dbOpdrachtTeWijzigen.setLatitude(latitude);
						dbOpdrachtTeWijzigen.setLongitude(longitude);
						
						//googlemaps - staticmap aanpassen
						String staticmap = GoogleApis.urlBuilderStaticMap(eersteAdresUitAdreslijst);
						
						isVerschillend = true;
					}
				}
				
				/*
				 * indien het adres gewijzigd werd
				 */
				int klantAdresId = dbKlantAdresDao.geefId(klantId, teWijzigenAdresId);
				if (dbOpdrachtTeWijzigen.getKlantAdresId() != klantAdresId && klantAdresId >= 0) {
					if (klantAdresId < 0) {
						DbKlantAdresDao dkad = new DbKlantAdresDao();
						DbKlantAdres dbKlantAdres = (DbKlantAdres) dkad.lees(teWijzigenKlantId);
						klantAdresId = dbKlantAdres.getAdresId();
					}
					Adres adres = (Adres) adresDaoAdapter.lees(teWijzigenAdresId);
					
					dbOpdrachtTeWijzigen.setKlantAdresId(klantAdresId);;

					double[] latlng = GoogleApis.zoeklatlng(adres);
					latitude = latlng[0];
					longitude = latlng[1];
					
					dbOpdrachtTeWijzigen.setLongitude(longitude);
					dbOpdrachtTeWijzigen.setLatitude(latitude);
					
					isVerschillend = true;
				}
				
				if (!dbOpdrachtTeWijzigen.getNaam().equals(opdrachtNaam)) {
					dbOpdrachtTeWijzigen.setNaam(opdrachtNaam);
					
					isVerschillend = true;
				}

				if (beginDatum != null && !beginDatum.equals(dbOpdrachtTeWijzigen.getStartdatum())) {
					dbOpdrachtTeWijzigen.setStartdatum(beginDatum);
					
					isVerschillend = true;
				} else {
					beginDatum = dbOpdrachtTeWijzigen.getStartdatum();
				}
				if (eindDatum != null && !eindDatum.equals(dbOpdrachtTeWijzigen.getEinddatum())) {
					dbOpdrachtTeWijzigen.setEinddatum(eindDatum);
					
					isVerschillend = true;
				} else {
					eindDatum = dbOpdrachtTeWijzigen.getEinddatum();
				}
				
				/*
				 * indien er iets gewijzigd werd in de opdrachtgegevens
				 */
				if (isVerschillend) {

					dbOpdrachtDao.wijzig(dbOpdrachtTeWijzigen);
				}
			}

			OpdrachtDetailData opdrachtDetailData = opdrachtDetailDaoAdapter.haalOpdrachtdetailDataOp(this.id);
			
			request.setAttribute("id", this.id);
			request.setAttribute("opdrachtDetailData", opdrachtDetailData);
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
			
			view = request.getRequestDispatcher("/OpdrachtDetail.jsp");
			
		} else {
			OpdrachtDetailData opdrachtDetailData = opdrachtDetailDaoAdapter.haalOpdrachtdetailDataOp(this.id);
			
			request.setAttribute("opdrachtDetailData", opdrachtDetailData);
			request.setAttribute("inputValidatieErrorMsg", inputValidatieErrorMsg);
			
			view = request.getRequestDispatcher("/OpdrachtDetail.jsp");
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

	/* (non-Javadoc)
	 * @see be.miras.programs.frederik.controller.IinputValidatie#inputValidatie(java.lang.String[])
	 */
	@Override
	public String inputValidatie(String[] teValideren) {
		String klantNaam = teValideren[0];
		String opdrachtNaam = teValideren[1];
		String startDatumString = teValideren[2];
		String eindDatumString = teValideren[3];		
		
		String inputValidatieErrorMsg = "";
		
		String msg = null;
		
		msg = InputValidatie.enkelAlfabetisch(klantNaam);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.NaamOpdrachtgever).concat(msg);
		}
		
		msg = InputValidatie.ingevuld(opdrachtNaam);
		if (msg != null) {
			inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.OpdrachtNaam).concat(msg);
		}
		
		if (this.id < 0){
			
			//inputvalidatie voor aanmaak van een nieuwe opdracht
			msg = InputValidatie.correcteDatum(startDatumString);
			
			if (msg!= null) {
				inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.StartDatum).concat(msg);
			} else {
				Date datum = Datum.creeerDatum(startDatumString);
				Date nu = new Date();
				nu.setDate(nu.getDate() - 1);
				
				if (datum.before(nu)){
					inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.StartDatumToekomst);
				}
			}
			
			String datumControle = msg;
			msg = InputValidatie.correcteDatum(eindDatumString);
			
			if (datumControle != null || msg != null) {
				
				if (msg != null){
					inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.EindDatum).concat(msg);					
				}
			} else {
				Date startdatum = Datum.creeerDatum(startDatumString);
				Date einddatum = Datum.creeerDatum(eindDatumString);
				
				if (einddatum.before(startdatum)){
					inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.EindDatumNaStartDatum);
				}
			}	
		} else {
			//inputvalidatie voor wijzigen van een bestaande opdracht
			/*
			 * er bestaan 4 mogelijkheden
			 * 1. er zijn geen datums ingevuld: dus hoeft er niet gevalideert te worden
			 * 2. begindatum is ingevuld, einddatum niet : check of begindatum.before(opdracht.geteinddatum)
			 * 3. begindatum is niet ingevuld, einddatum wel: check of einddatum.after(opdracht.getbegindatum)
			 * 4. beide datums zijn ingevuld : check of einddatum.after(begindatum)
			 * 
			 */
			DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
			
			DbOpdracht dbOpdracht = (DbOpdracht) dbOpdrachtDao.lees(this.id);
			Date vorigeBeginDatum = dbOpdracht.getStartdatum();
			Date vorigeEindDatum = dbOpdracht.getEinddatum();
			
			// mogelijkheid 2
			if (!startDatumString.trim().isEmpty() && eindDatumString.isEmpty()){
				
				msg = InputValidatie.correcteDatum(startDatumString);
				
				if (msg != null){
					inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.StartDatum).concat(msg);
				} else {
					Date startDatum = Datum.creeerDatum(startDatumString);
					if ( startDatum.after(vorigeEindDatum)){
						msg = InputValidatieStrings.StartDatumNietNaEindDatum;
						inputValidatieErrorMsg = inputValidatieErrorMsg.concat(msg);
					}
				}
			}
			// mogelijkheid 3
			if (startDatumString.trim().isEmpty() && !eindDatumString.trim().isEmpty()){
				msg = InputValidatie.correcteDatum(eindDatumString);
				
				if (msg != null){
					inputValidatieErrorMsg = inputValidatieErrorMsg.concat(InputValidatieStrings.EindDatum).concat(msg);
				} else {
					Date eindDatum = Datum.creeerDatum(eindDatumString);
				
					if ( eindDatum.before(vorigeBeginDatum)){
						msg = InputValidatieStrings.EinddatumNietVoorStartDatum;
						inputValidatieErrorMsg = inputValidatieErrorMsg.concat(msg);
					}
				}
			}
			// mogelijkheid 4 ==> zie if ( id < 0)
		}
		
		return inputValidatieErrorMsg;
	}
	
	
}

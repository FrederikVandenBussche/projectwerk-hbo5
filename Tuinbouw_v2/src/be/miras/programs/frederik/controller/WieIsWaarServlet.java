package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import be.miras.programs.frederik.dao.DbOpdrachtTaakDao;
import be.miras.programs.frederik.dao.DbPersoonDao;
import be.miras.programs.frederik.dao.DbStatusDao;
import be.miras.programs.frederik.dao.DbTaakDao;
import be.miras.programs.frederik.dao.DbVooruitgangDao;
import be.miras.programs.frederik.dao.DbWerknemerDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dao.adapter.AdresAdapter;
import be.miras.programs.frederik.dbo.DbKlant;
import be.miras.programs.frederik.dbo.DbKlantAdres;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbOpdrachtTaak;
import be.miras.programs.frederik.dbo.DbPersoon;
import be.miras.programs.frederik.dbo.DbStatus;
import be.miras.programs.frederik.dbo.DbVooruitgang;
import be.miras.programs.frederik.dbo.DbWerknemerOpdrachtTaak;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.WieIsWaar;
import be.miras.programs.frederik.util.SessieOpruimer;

/**
 * Servlet implementation class WieIsWaarServlet
 */
@WebServlet("/WieIsWaarServlet")
public class WieIsWaarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WieIsWaarServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		Boolean isIngelogd = (Boolean) session.getAttribute("isIngelogd");
		RequestDispatcher view = null;
		
		if (isIngelogd == null || isIngelogd == false) {
			view = request.getRequestDispatcher("/logout");

		} else {
			DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
			DbWerknemerDao dbWerknemerDao = new DbWerknemerDao();
			DbPersoonDao dbPersoonDao = new DbPersoonDao();
			DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
			DbTaakDao dbTaakDao = new DbTaakDao();
			DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();
			DbKlantDao dbKlantDao = new DbKlantDao();
			DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
			DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
			List<DbWerknemerOpdrachtTaak> dbWerknemerOpdrachtTaakLijst = new ArrayList<DbWerknemerOpdrachtTaak>();
			DbStatusDao dbStatusdao = new DbStatusDao();
			
			AdresAdapter adresAdapter = new AdresAdapter();
			
			DbPersoon dbPersoon = new DbPersoon();
			DbOpdracht dbOpdracht = new DbOpdracht();
			DbKlant dbKlant = null;
			DbKlantAdres dbKlantAdres = new DbKlantAdres();
			Adres adres = new Adres();
			

			
			List<WieIsWaar> wieIsWaarLijst = new ArrayList<WieIsWaar>();
			
			Date vandaag = new Date();
			
			
			dbWerknemerOpdrachtTaakLijst = dbWerknemerOpdrachtTaakDao.lees(vandaag);
			
			Iterator<DbWerknemerOpdrachtTaak> dbwotlIt = dbWerknemerOpdrachtTaakLijst.iterator();
			while(dbwotlIt.hasNext()){
				DbWerknemerOpdrachtTaak wot = dbwotlIt.next();
				
				WieIsWaar wieIsWaar = new WieIsWaar();
				
				Date startuur = wot.getBeginuur();
				Date einduur = wot.getEinduur();
				
				int werknemerId = wot.getWerknemerId();
				int opdrachtId = wot.getOpdrachtTaakOpdrachtId();
				int taakId = wot.getOpdrachtTaakTaakId();
				
				int persoonId = dbWerknemerDao.returnPersoonId(werknemerId);
				dbPersoon = (DbPersoon) dbPersoonDao.lees(persoonId);
				String werknemerNaam = dbPersoon.getVoornaam().concat(" ").concat(dbPersoon.getNaam());
				
				
				dbOpdracht = (DbOpdracht) dbOpdrachtDao.lees(opdrachtId);
				String opdrachtNaam = dbOpdracht.getNaam();
								
				String taakNaam = dbTaakDao.selectNaam(taakId);
				
				int klantAdresId = dbOpdracht.getKlantAdresId();
				
				dbKlantAdres = dbKlantAdresDao.select(klantAdresId);
				int klantId = dbKlantAdres.getKlantId();
				int adresId = dbKlantAdres.getAdresId();
				
				dbKlant = (DbKlant) dbKlantDao.lees(klantId);
				String klantNaam = null;
				klantNaam = dbKlant.geefAanspreekNaam();
				adres = (Adres) adresAdapter.lees(adresId);
				String straatEnNummer = null;
				if (adres.getBus() != null && !adres.getBus().isEmpty()) {
					straatEnNummer =  adres.getStraat() + " " + adres.getNummer() + " " + adres.getBus();
				} else {
					straatEnNummer = adres.getStraat() + " " + adres.getNummer();
				}
				String postcodeEnPlaats = adres.getPostcode() + " " + adres.getPlaats();
				
				DbOpdrachtTaak dbOpdrachtTaak = dbOpdrachtTaakDao.leesWaarTaakId(taakId);
				DbVooruitgang dbVooruitgang = (DbVooruitgang) dbVooruitgangDao.lees(dbOpdrachtTaak.getVooruitgangId());
				
				// statusId == 1 betekent "niet gestart"
				if (dbVooruitgang.getStatusId() > 1){
					DbStatus dbStatus = (DbStatus) dbStatusdao.lees(dbVooruitgang.getStatusId());
					String statusNaam = dbStatus.getNaam();
					
					wieIsWaar.setStartuur(startuur);
					wieIsWaar.setEinduur(einduur);
					wieIsWaar.setWerknemer(werknemerNaam);
					wieIsWaar.setKlantNaam(klantNaam);
					wieIsWaar.setStraatEnNummer(straatEnNummer);
					wieIsWaar.setPostcodeEnPlaats(postcodeEnPlaats);
					wieIsWaar.setOpdracht(opdrachtNaam);
					wieIsWaar.setTaak(taakNaam);
					wieIsWaar.setStatus(statusNaam);
					wieIsWaarLijst.add(wieIsWaar);
				}
			}
			SessieOpruimer.AttributenVerwijderaar(session);
			request.setAttribute("wieIsWaarLijst", wieIsWaarLijst);
			
			view = request.getRequestDispatcher("/WieIsWaar.jsp");
		}
		view.forward(request, response);
	}

	
}

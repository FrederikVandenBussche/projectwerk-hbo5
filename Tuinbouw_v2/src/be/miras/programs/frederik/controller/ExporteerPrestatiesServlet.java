package be.miras.programs.frederik.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.DbOpdrachtTaakDao;
import be.miras.programs.frederik.dao.DbPersoonDao;
import be.miras.programs.frederik.dao.DbStatusDao;
import be.miras.programs.frederik.dao.DbTaakDao;
import be.miras.programs.frederik.dao.DbVooruitgangDao;
import be.miras.programs.frederik.dao.DbWerknemerDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dao.adapter.AdresDaoAdapter;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbOpdrachtTaak;
import be.miras.programs.frederik.dbo.DbPersoon;
import be.miras.programs.frederik.dbo.DbStatus;
import be.miras.programs.frederik.dbo.DbTaak;
import be.miras.programs.frederik.dbo.DbVooruitgang;
import be.miras.programs.frederik.dbo.DbWerknemer;
import be.miras.programs.frederik.dbo.DbWerknemerOpdrachtTaak;
import be.miras.programs.frederik.export.ExcelData;
import be.miras.programs.frederik.export.GenereerXls;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.model.Planning;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.Datatype;
import be.miras.programs.frederik.util.Datum;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class ExporteerPrestatiesServlet
 */
@WebServlet("/ExporteerPrestatiesServlet")
public class ExporteerPrestatiesServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ExporteerPrestatiesServlet.class);
	private final String TAG = "ExporteerPrestatiesServlet: ";

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExporteerPrestatiesServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		HttpSession session = request.getSession();
		Boolean isIngelogd = (Boolean) session.getAttribute("isIngelogd");
		
		if(isIngelogd == null || isIngelogd == false){
			RequestDispatcher view = request.getRequestDispatcher("/logout");
			view.forward(request, response);
		} else {
			int opdrachtId = Datatype.stringNaarInt(request.getParameter("opdracht"));
			String begindatumString = request.getParameter("begindatum");
			String einddatumString = request.getParameter("einddatum");
			
			String klantnaam = (String) session.getAttribute("aanspreeknaam");
			int id = (int) session.getAttribute("klantId");
			
			DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
			DbWerknemerOpdrachtTaakDao dbWerknemerOpdrachtTaakDao = new DbWerknemerOpdrachtTaakDao();
			DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
			DbTaakDao dbTaakDao = new DbTaakDao();
			DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
			DbStatusDao dbStatusDao = new DbStatusDao();
			DbWerknemerDao dbWerknemerDao = new DbWerknemerDao();
			DbPersoonDao dbPersoonDao = new DbPersoonDao();
			AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();
			ExcelData excelData = new ExcelData();
			
			// de opdrachtenlijst van deze klant ophalen
			List<DbOpdracht> dbOpdrachtLijst = (ArrayList<DbOpdracht>) dbOpdrachtDao.leesWaarKlantId(id);
			excelData.setKlantNaam(klantnaam);
			
			Date begindatum = Datum.creeerDatum(begindatumString);
			Date einddatum = Datum.creeerDatum(einddatumString);
			
			List<Opdracht> opdrachtLijst = new ArrayList<Opdracht>();
						
			if (opdrachtId == 0){
				// exporteer alle opdrachten van deze klant
				
				Iterator<DbOpdracht> dbOpdrachtIt = dbOpdrachtLijst.iterator();
				while(dbOpdrachtIt.hasNext()){
					DbOpdracht dbOpdracht = dbOpdrachtIt.next();
					
					if(begindatum == null || dbOpdracht.getStartdatum().after(begindatum)){
						if(einddatum == null || dbOpdracht.getEinddatum().before(einddatum)){
							
							Opdracht opdracht = new Opdracht();
							opdracht.setId(dbOpdracht.getId());
							opdracht.setKlantId(dbOpdracht.getKlantId());
							opdracht.setKlantAdresId(dbOpdracht.getKlantAdresId());
							opdracht.setOpdrachtNaam(dbOpdracht.getNaam());
							opdracht.setStartDatum(dbOpdracht.getStartdatum());
							opdracht.setEindDatum(dbOpdracht.getEinddatum());
							opdracht.setLatitude(dbOpdracht.getLatitude());
							opdracht.setLongitude(dbOpdracht.getLongitude());
							
							opdrachtLijst.add(opdracht);
						}
					}
				}
			} else {
				// exporteer slechts 1 opdracht van deze klant
				Iterator<DbOpdracht> dbOpdrachtIt = dbOpdrachtLijst.iterator();
				while(dbOpdrachtIt.hasNext()){
					DbOpdracht dbOpdracht = dbOpdrachtIt.next();
					if (dbOpdracht.getId() == opdrachtId){
						
						if(begindatum == null || dbOpdracht.getStartdatum().after(begindatum)){
							if(einddatum == null || dbOpdracht.getEinddatum().before(einddatum)){
								
								Opdracht opdracht = new Opdracht();
								opdracht.setId(dbOpdracht.getId());
								opdracht.setKlantId(dbOpdracht.getKlantId());
								opdracht.setKlantAdresId(dbOpdracht.getKlantAdresId());
								opdracht.setOpdrachtNaam(dbOpdracht.getNaam());
								opdracht.setStartDatum(dbOpdracht.getStartdatum());
								opdracht.setEindDatum(dbOpdracht.getEinddatum());
								opdracht.setLatitude(dbOpdracht.getLatitude());
								opdracht.setLongitude(dbOpdracht.getLongitude());
								
								opdrachtLijst.add(opdracht);
							}
						}
					}
				}
			}
			
			Iterator<Opdracht> opdrachtLijstIterator = opdrachtLijst.iterator();
			while(opdrachtLijstIterator.hasNext()){
				Opdracht opdracht = opdrachtLijstIterator.next();
				
				List<Taak> taakLijst = new ArrayList<Taak>();
				
				List<DbOpdrachtTaak> dbOpdrachtTaakLijst = dbOpdrachtTaakDao.leesLijst(opdracht.getId());
				Iterator<DbOpdrachtTaak> dbOpdrachtTaakIt = dbOpdrachtTaakLijst.iterator();
				while(dbOpdrachtTaakIt.hasNext()){
					DbOpdrachtTaak dbOpdrachtTaak = dbOpdrachtTaakIt.next();
					
					Taak taak = new Taak();
					List<Planning> planningLijst = new ArrayList<Planning>();
					
					taak.setId(dbOpdrachtTaak.getTaakId());
					taak.setOpdrachtId(opdracht.getId());
					
					DbTaak dbTaak = (DbTaak) dbTaakDao.lees(dbOpdrachtTaak.getTaakId());
					taak.setTaakNaam(dbTaak.getNaam());
					taak.setOpmerking(dbOpdrachtTaak.getOpmerking());
					
					DbVooruitgang dbVooruitgang = (DbVooruitgang) dbVooruitgangDao.lees(dbOpdrachtTaak.getVooruitgangId());
					taak.setVooruitgangPercentage(dbVooruitgang.getPercentage());
					
					DbStatus dbStatus = (DbStatus) dbStatusDao.lees(dbVooruitgang.getStatusId());
					taak.setStatus(dbStatus.getNaam());
					
					List<DbWerknemerOpdrachtTaak> dbWerknemerOpdrachtTaakLijst = 
							dbWerknemerOpdrachtTaakDao.leesWaarTaakIdOpdrachtId(taak.getId(), opdracht.getId());
					
					Iterator<DbWerknemerOpdrachtTaak> dbWOTIt = dbWerknemerOpdrachtTaakLijst.iterator();
					while(dbWOTIt.hasNext()){
						DbWerknemerOpdrachtTaak dwot = dbWOTIt.next();
						
						Planning planning = new Planning();
						
						planning.setBeginuur(dwot.getBeginuur());
						planning.setEinduur(dwot.getEinduur());
						planning.setAantalUren();
						planning.setWerknemerId(dwot.getWerknemerId());;
						
						DbWerknemer dbWerknemer = (DbWerknemer) dbWerknemerDao.lees(dwot.getWerknemerId());
						int persoonId = dbWerknemer.getPersoonId();
						DbPersoon dbPersoon = (DbPersoon) dbPersoonDao.lees(persoonId);
						String werknemer = dbPersoon.getVoornaam().concat(" ").concat(dbPersoon.getNaam());
						planning.setWerknemer(werknemer);
		
						planningLijst.add(planning);
					}
					
					taak.setPlanningLijst(planningLijst);
					
					taakLijst.add(taak);
				}
				opdracht.setTaakLijst(taakLijst);
				
				int klantAdresId = opdracht.getKlantAdresId();
				Adres adres = adresDaoAdapter.leesWaarKlantAdresId(klantAdresId);
				opdracht.setAdresString(adres.toString());
			}
			
			excelData.setOpdrachtLijst(opdrachtLijst);
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			
			response.setContentType("application/vnd.ms-excel");
			
			String PATH = "${catalina.home}/tuinbouwbedrijf/prestaties/";
			
			Date datum = new Date();
			int dag = datum.getDate();
			int maand = datum.getMonth();
			int jaar = datum.getYear() + 1900;
			int uur = datum.getHours();
			int minuten = datum.getMinutes();
			int seconden = datum.getSeconds();
			String datumString = "_" + dag + "_" + maand + "_" + jaar
					 + "_" + uur + "" + minuten + "" + seconden;
			String fileNaam = excelData.getKlantNaam() + datumString + ".xls";
			String dest = PATH.concat(fileNaam);
			
			GenereerXls genereerXls = new GenereerXls();
			genereerXls.genereer(dest, excelData);
			
			//open pdf in nieuw venster
			File file = new File(dest);
			
			response.setHeader("Content-disposition","inline; filename=" + fileNaam );

			BufferedInputStream  bufferedInputStream = null; 
			BufferedOutputStream bufferedOutputStream = null;
			
			try {

			    InputStream inputStream=new FileInputStream(file);
			    bufferedInputStream = new BufferedInputStream(inputStream);
			    bufferedOutputStream = new BufferedOutputStream(servletOutputStream);
			    byte[] buff = new byte[2048];
			    int bytesRead;
			    // read/write loop.
			    while(-1 != (bytesRead = bufferedInputStream.read(buff, 0, buff.length))) {
			        bufferedOutputStream.write(buff, 0, bytesRead);
			    }
			} 
			catch(IOException e)
			{
			    e.printStackTrace();
			    LOGGER.error(TAG + "Genereer Excell file: " , e);
			} finally {
			    if (bufferedInputStream != null)
					bufferedInputStream.close();
			    if (bufferedOutputStream != null)
			        bufferedOutputStream.close();
			}
		}
	}

	
}

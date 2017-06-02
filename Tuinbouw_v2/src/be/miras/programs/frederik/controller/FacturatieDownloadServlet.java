package be.miras.programs.frederik.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import be.miras.programs.frederik.dao.DbOpdrachtTaakDao;
import be.miras.programs.frederik.dao.DbStatusDao;
import be.miras.programs.frederik.dao.DbVooruitgangDao;
import be.miras.programs.frederik.dao.adapter.AdresDaoAdapter;
import be.miras.programs.frederik.dbo.DbVooruitgang;
import be.miras.programs.frederik.export.FactuurData;
import be.miras.programs.frederik.export.GenereerPdf;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.util.Datatype;

/**
 * @author Frederik Vanden Bussche
 * 
 * Servlet implementation class FacturatieDownloadServlet
 */
@WebServlet("/FacturatieDownloadServlet")
public class FacturatieDownloadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(FacturatieDownloadServlet.class);
	private final String TAG = "FacturatieDownloadServlet: ";
	
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FacturatieDownloadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//response.setContentType("text/html");
		response.setContentType("application/pdf");
		
		HttpSession session = request.getSession();
		FactuurData factuurData = (FactuurData) session.getAttribute("factuur");
		int adresId = Datatype.stringNaarInt(request.getParameter("adres"));
		
		AdresDaoAdapter adresDaoAdapter = new AdresDaoAdapter();
		GenereerPdf genereerPdf = new GenereerPdf();
		
		Adres facturatieAdres = (Adres) adresDaoAdapter.lees(adresId);
		factuurData.setAdres(facturatieAdres);

		ServletOutputStream servletOutputStream = response.getOutputStream();
		
		String PATH = "${catalina.home}/tuinbouwbedrijf/facturen/";
		Date datum = new Date();
		int dag = datum.getDate();
		int maand = datum.getMonth();
		int jaar = datum.getYear() + 1900;
		int uur = datum.getHours();
		int minuten = datum.getMinutes();
		int seconden = datum.getSeconds();
		String datumString = "_" + dag + "_" + maand + "_" + jaar + "_" + uur + "" + minuten + "" + seconden;
		String fileNaam = factuurData.getKlantNaam() + datumString + ".pdf";
		fileNaam = "test.pdf";
		String dest = PATH.concat(fileNaam);

		genereerPdf.genereer(dest, factuurData);

		File file = new File(dest);

		response.setHeader("Content-disposition", "inline; filename=" + fileNaam);

		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		
		try {
						
			InputStream inputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(inputStream);
			bufferedOutputStream = new BufferedOutputStream(servletOutputStream);
			
			byte[] buff = new byte[2048];
			int bytesRead;
			// read/write loop.
			while (-1 != (bytesRead = bufferedInputStream.read(buff, 0, buff.length))) {
				bufferedOutputStream.write(buff, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(TAG + "Genereer PDF file: " + e);
		} finally {
			if (bufferedInputStream != null)
				bufferedInputStream.close();
			if (bufferedOutputStream != null)
				bufferedOutputStream.close();
		}
		
		// de DbOpdrachtTaak instellen op "Gefactuureerd"
		DbStatusDao dbStatusDao = new DbStatusDao();
		DbOpdrachtTaakDao dbOpdrachtTaakDao = new DbOpdrachtTaakDao();
		DbVooruitgangDao dbVooruitgangDao = new DbVooruitgangDao();
		
		int gefactureerdId = dbStatusDao.lees("Gefactureerd");
		List<Opdracht> opdrachtLijst = factuurData.getOpdrachtLijst();
		
		Iterator<Opdracht> it = opdrachtLijst.iterator();
		while (it.hasNext()){
			Opdracht opdracht = it.next();
			
			int opdrachtId = opdracht.getId();
			
			List<Integer> vooruitgangIds = dbOpdrachtTaakDao.leesVooruitgangIds(opdrachtId);
			
			Iterator<Integer> vooruitgangIdsIt = vooruitgangIds.iterator();
			while (vooruitgangIdsIt.hasNext()){
				DbVooruitgang dbVooruitgang = new DbVooruitgang();
				
				int vooruitgangId = vooruitgangIdsIt.next();
				
				dbVooruitgang.setId(vooruitgangId);
				dbVooruitgang.setPercentage(100);
				dbVooruitgang.setStatusId(gefactureerdId);
				
				dbVooruitgangDao.wijzig(dbVooruitgang);
			}
		}
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

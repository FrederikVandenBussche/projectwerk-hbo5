package be.miras.programs.frederik.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.adapter.AdresAdapter;
import be.miras.programs.frederik.export.Factuur;
import be.miras.programs.frederik.export.GenereerPdf;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class FacturatieDownloadServlet
 */
@WebServlet("/FacturatieDownloadServlet")
public class FacturatieDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = "FacturatieDownloadServlet: ";

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
		response.setContentType("text/html");

		HttpSession session = request.getSession();
		Factuur factuur = (Factuur) session.getAttribute("factuur");
		int adresId = Datatype.stringNaarInt(request.getParameter("adres"));
		System.out.println(TAG + "adresId: " + adresId);
		AdresAdapter adresAdapter = new AdresAdapter();
		Adres facturatieAdres = (Adres) adresAdapter.lees(adresId);
		factuur.setAdres(facturatieAdres);

		ServletOutputStream servletOutputStream = response.getOutputStream();

		response.setContentType("application/pdf");

		String PATH = "c:/tuinbouwbedrijf/facturen/";
		Date datum = new Date();
		int dag = datum.getDate();
		int maand = datum.getMonth();
		int jaar = datum.getYear() + 1900;
		int uur = datum.getHours();
		int minuten = datum.getMinutes();
		int seconden = datum.getSeconds();
		String datumString = "_" + dag + "_" + maand + "_" + jaar + "_" + uur + "" + minuten + "" + seconden;
		String fileNaam = factuur.getKlantNaam() + datumString + ".pdf";
		String dest = PATH.concat(fileNaam);

		GenereerPdf genereerPdf = new GenereerPdf();
		genereerPdf.genereer(dest, factuur);

		// open pdf in nieuw venster
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedInputStream != null)
				bufferedInputStream.close();
			if (bufferedOutputStream != null)
				bufferedOutputStream.close();
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

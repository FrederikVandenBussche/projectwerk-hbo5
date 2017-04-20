package be.miras.programs.frederik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.miras.programs.frederik.dao.DbKlantAdresDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.adapter.AdresAdapter;
import be.miras.programs.frederik.dbo.DbKlantAdres;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.model.OpdrachtDetailData;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.Datum;
import be.miras.programs.frederik.util.GoogleApis;
import be.miras.programs.frederik.util.Datatype;

/**
 * Servlet implementation class OpdrachtOpslaanServlet
 */
@WebServlet("/OpdrachtOpslaanServlet")
public class OpdrachtOpslaanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String TAG = "OpdrachtOpslaanServlet: ";

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

		// variabelen ophalen
		int id = Datatype.stringNaarInt(request.getParameter("id"));
		String klantNaam = request.getParameter("klanten").trim();
		String opdrachtNaam = request.getParameter("opdrachtNaam").trim();
		String startDatumString = request.getParameter("nieuweStartDatum").trim();
		String eindDatumString = request.getParameter("nieuweEindDatum").trim();
		String adresString = request.getParameter("adressen").trim();

		HttpSession session = request.getSession();
		OpdrachtDetailData opdrachtDetailData = (OpdrachtDetailData) session.getAttribute("opdrachtDetailData");
		List<Opdracht> opdrachtLijst = (List<Opdracht>) session.getAttribute("lijst");

		DbOpdrachtDao dbOpdrachtDao = new DbOpdrachtDao();
		DbKlantAdresDao dbKlantAdresDao = new DbKlantAdresDao();

		Opdracht opdracht = new Opdracht();

		Date startDatum = Datum.creeerDatum(startDatumString);
		Date eindDatum = Datum.creeerDatum(eindDatumString);

		String errorMsg = null;

		// hashmap met klantnamen
		Map<Integer, String> klantNaamMap = opdrachtDetailData.getKlantNaamMap();
		int klantId = Integer.MIN_VALUE;
		for (int i : klantNaamMap.keySet()) {
			if (klantNaamMap.get(i).equals(klantNaam)) {
				klantId = i;
			}
		}

		// hashmap met adressen
		Map<Integer, String> adresMap = opdrachtDetailData.getAdresMap();

		System.out.println(TAG + "De grootte van de adresMap bij deze klant: " + adresMap.size());

		int klantAdresId = Integer.MIN_VALUE;
		for (int i : adresMap.keySet()) {
			System.out.println(TAG + "De adresMap: " + adresMap.get(i));
			if (adresMap.get(i).equals(adresString)) {
				klantAdresId = i;
			}
		}
		System.out.println(TAG + "KlantIdAdres: " + klantAdresId);

		opdracht.setId(id);
		opdracht.setklantId(klantId);
		opdracht.setKlantNaam(klantNaam);
		if (opdrachtNaam == null || opdrachtNaam.isEmpty()) {
			opdrachtNaam = "onbekend";
		}
		opdracht.setOpdrachtNaam(opdrachtNaam);
		if (startDatum != null) {
			opdracht.setStartDatum(startDatum);
		}
		if (eindDatum != null) {
			opdracht.setEindDatum(eindDatum);
		}

		if (id < 0) {
			// een nieuwe opdracht toevoegen
			System.out.println("Een nieuwe opdracht toevoegen");

			DbKlantAdresDao dkad = new DbKlantAdresDao();
			DbKlantAdres dbKlantAdres = (DbKlantAdres) dkad.lees(klantId);

			klantAdresId = dbKlantAdres.getId();
			System.out.println(TAG + "de klantId = " + klantId);
			System.out.println(TAG + "het klantAdresId = " + klantAdresId);
			if (klantAdresId <= 0) {
				errorMsg = "Kan geen opdracht toevoegen aan deze klant " + " omdat er nog geen adres bekent is. <br />"
						+ "Oplossing : voeg een adres toe aan deze klant en probeer opnieuw";

			} else {
				DbOpdracht dbOpdracht = new DbOpdracht();
				AdresAdapter adresAdapter = new AdresAdapter();

				dbOpdracht.setKlantId(klantId);
				dbOpdracht.setKlantAdresId(klantAdresId);
				dbOpdracht.setNaam(opdrachtNaam);
				dbOpdracht.setStartdatum(startDatum);
				dbOpdracht.setEinddatum(eindDatum);

				Adres adres = (Adres) adresAdapter.leesWaarAdresId(klantAdresId);

				// aanmaak van adresMap<adresId, adresString>
				List<Integer> adresIdLijst = dbKlantAdresDao.leesLijst(klantId);

				Iterator<Integer> adresIdIter = adresIdLijst.iterator();
				while (adresIdIter.hasNext()) {
					int adresId = adresIdIter.next();
					Adres a = (Adres) adresAdapter.lees(adresId);
					adresMap.put(adresId, a.toString());
				}

				opdrachtDetailData.setAdresMap(adresMap);

				double[] latlng = GoogleApis.zoeklatlng(adres);
				double latitude = latlng[0];
				double longitude = latlng[1];

				dbOpdracht.setLatitude(latitude);
				dbOpdracht.setLongitude(longitude);

				dbOpdrachtDao.voegToe(dbOpdracht);

				// de nieuwe opdracht toevoegen aan de session opdrachtLijst
				id = dbOpdrachtDao.geefMaxId();
				opdracht.setId(id);
				opdracht.setLatitude(latitude);
				opdracht.setLongitude(longitude);
				opdracht.setOpdrachtNaam(opdrachtNaam);
				List<Taak> taaklijst = new ArrayList<Taak>();
				List<Materiaal> materiaallijst = new ArrayList<Materiaal>();
				opdracht.setTaakLijst(taaklijst);
				opdracht.setGebruiktMateriaalLijst(materiaallijst);

				opdrachtLijst.add(opdracht);

				// de nieuwe opdracht toevoegen aan de opdrachtDetailData
				// die gebruikt wordt om te presenteren in OpdrachtDetail.jsp
				opdrachtDetailData.setAanspreeknaam("voor " + klantNaam);
				opdrachtDetailData.setAdresString(adresString);
				opdrachtDetailData.setButtonNaam("Wijzigen opslaan");
				opdrachtDetailData.setVariabelveld1(". Opdrachtgever wijzigen: ");
				opdrachtDetailData.setVariabelveld2(", wijzigen : ");

				opdrachtDetailData.setOpdracht(opdracht);
			}

		} else {
			// het betreft een bestaande opdracht
			// indien er iets gewijzigd werd, de wijzigingen opslaan
			System.out.println("Een bestaande opdracht wijzigen");

			boolean isVerschillend = false;
			double latitude = 0;
			double longitude = 0;
			AdresAdapter adresAdapter = new AdresAdapter();

			DbOpdracht dbOpdrachtTeWijzigen = (DbOpdracht) dbOpdrachtDao.lees(id);

			System.out.println(TAG + "de id van de opdracht = " + id);

			System.out.println(TAG + "klantAdresId = " + dbOpdrachtTeWijzigen.getKlantAdresId());

			if (dbOpdrachtTeWijzigen.getKlantId() != klantId) {
				dbOpdrachtTeWijzigen.setKlantId(klantId);

				// opnieuw aanmaken van adresMap<adresId, adresString>
				adresMap.clear();

				List<Integer> adresIdLijst = dbKlantAdresDao.leesLijst(klantId);

				Iterator<Integer> adresIdIter = adresIdLijst.iterator();
				while (adresIdIter.hasNext()) {
					int adresId = adresIdIter.next();
					Adres a = (Adres) adresAdapter.lees(adresId);
					adresMap.put(adresId, a.toString());
				}

				opdrachtDetailData.setAdresMap(adresMap);

				opdrachtDetailData.setAanspreeknaam(klantNaam);

				isVerschillend = true;
			}

			if (dbOpdrachtTeWijzigen.getKlantAdresId() != klantAdresId && klantAdresId >= 0) {
				if (klantAdresId < 0) {
					DbKlantAdresDao dkad = new DbKlantAdresDao();
					DbKlantAdres dbKlantAdres = (DbKlantAdres) dkad.lees(klantId);
					klantAdresId = dbKlantAdres.getAdresId();
				}
				Adres adres = (Adres) adresAdapter.lees(klantAdresId);

				opdrachtDetailData.setAdresString(adresString);

				double[] latlng = GoogleApis.zoeklatlng(adres);
				latitude = latlng[0];
				longitude = latlng[1];

				dbOpdrachtTeWijzigen.setLongitude(longitude);
				dbOpdrachtTeWijzigen.setLatitude(latitude);

				isVerschillend = true;
			}
			System.out.println(TAG + "opdrachtNaam: :::::::::::::" + dbOpdrachtTeWijzigen.getNaam());
			if (!dbOpdrachtTeWijzigen.getNaam().equals(opdrachtNaam)) {
				dbOpdrachtTeWijzigen.setNaam(opdrachtNaam);
				isVerschillend = true;
			}

			if (startDatum != null && !startDatum.equals(dbOpdrachtTeWijzigen.getStartdatum())) {
				dbOpdrachtTeWijzigen.setStartdatum(startDatum);
				isVerschillend = true;
			}
			if (eindDatum != null && !eindDatum.equals(dbOpdrachtTeWijzigen.getEinddatum())) {
				dbOpdrachtTeWijzigen.setEinddatum(eindDatum);
				isVerschillend = true;
			}
			if (isVerschillend) {
				dbOpdrachtDao.wijzig(dbOpdrachtTeWijzigen);
				ListIterator<Opdracht> it = opdrachtLijst.listIterator();
				while (it.hasNext()) {
					Opdracht o = it.next();
					if (o.getId() == id) {
						o.setklantId(klantId);
						o.setKlantNaam(klantNaam);
						o.setOpdrachtNaam(opdrachtNaam);
						o.setStartDatum(startDatum);
						o.setEindDatum(eindDatum);
						o.setLatitude(latitude);
						o.setLongitude(longitude);
						it.set(o);
					}
				}
			}

		}

		RequestDispatcher view = null;

		if (errorMsg == null || errorMsg.isEmpty()) {

			session.setAttribute("opdrachtDetailData", opdrachtDetailData);

			view = request.getRequestDispatcher("/OpdrachtDetail.jsp");
			view.forward(request, response);
		} else {

			request.setAttribute("msg", errorMsg);

			view = request.getRequestDispatcher("/ErrorPage.jsp");
			view.forward(request, response);
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

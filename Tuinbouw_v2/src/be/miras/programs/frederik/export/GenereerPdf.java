package be.miras.programs.frederik.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.model.Planning;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.model.Verplaatsing;
import be.miras.programs.frederik.util.Datum;

/**
 * @author Frederik Vanden Bussche
 * 
 * Genereer een pdf file
 */
public class GenereerPdf {
	private static final Logger LOGGER = Logger.getLogger(GenereerPdf.class);
	private static final String TAG = "GenereerPdf: ";

	
	/**
	 * @param dest String :  path + filename van de pdf file
	 * @param factuur Factuur : de data die in de pdf geplaatst wordt
	 */
	public void genereer(String dest, Factuur factuur) {
		File file = new File(dest);
		file.getParentFile().mkdirs();
		try {
			createPdf(dest, factuur);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("IOException: " + TAG + "public void genereer() : ", e); 	
		}
	}

	/**
	 * @param dest String :  path + filename van de pdf file
	 * @param factuur Factuur : de data die in de pdf geplaatst wordt
	 * @throws FileNotFoundException 
	 */
	private void createPdf(String dest, Factuur factuur) throws FileNotFoundException{
		double totaalPrijs = 0;

		// Initialize PDF writer
		PdfWriter writer = new PdfWriter(dest);

		// Initialize PDF document
		PdfDocument pdf = new PdfDocument(writer);

		// Initialize document
		Document document = new Document(pdf);

		// add paragraph to the document
		Text titelTekst = new Text("Tuinbouwbedrijf hitek");
		titelTekst.setFontSize(30);
		Paragraph titel = new Paragraph(titelTekst);
		titel.setTextAlignment(TextAlignment.CENTER);
		document.add(titel);
		
		document.add(new Paragraph("\n"));

		String klantNaam = factuur.getKlantNaam();
		Adres adres = factuur.getAdres();
		String adresDeel1 = adres.getStraat() + " " + adres.getNummer() + " ";
		if (adres.getBus() != null) {
			adresDeel1 = adresDeel1.concat(adres.getBus());
		}
		String adresDeel2 = adres.getPostcode() + " " + adres.getPlaats();
		Paragraph adresInfo = new Paragraph(klantNaam + "\n" + adresDeel1 + "\n" + adresDeel2 + "\n\n");
		adresInfo.setMarginLeft(250);
		document.add(adresInfo);
		
		document.add(new Paragraph("\n"));
		document.add(new Paragraph("\n"));
		
		List<Opdracht> opdrachtLijst = factuur.getOpdrachtLijst();
		Iterator<Opdracht> opdrachtIterator = opdrachtLijst.iterator();
		while (opdrachtIterator.hasNext()) {
			Opdracht opdracht = opdrachtIterator.next();

			Table tabel = new Table(new float[] { 1 });
			tabel.setWidthPercent(100);
			// opdrachtNaam
			Paragraph opdrachtNaam = new Paragraph(opdracht.getOpdrachtNaam());
			tabel.addCell(opdrachtNaam);
			document.add(tabel);

			// werkuren
			// tabel = taaknaam/datum/#uren/prijsPerUur/totaal
			float[] colombreedtes = { 5, 1, 1, 1, 3 };
			Table overzichtWerkuren = new Table(colombreedtes);
			overzichtWerkuren.setWidthPercent(100);

			Cell[] header = new Cell[] { new Cell().setBackgroundColor(new DeviceGray(0.75f)).add("Taak"),
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).setTextAlignment(TextAlignment.CENTER)
							.add("Datum"),
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).setTextAlignment(TextAlignment.CENTER)
							.add("# uren"),
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).setTextAlignment(TextAlignment.CENTER)
							.add("Prijs"),
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).setTextAlignment(TextAlignment.CENTER)
							.add("Totaal") };
			for (Cell hcell : header) {
				overzichtWerkuren.addHeaderCell(hcell);
			}

			Iterator<Taak> taakIt = opdracht.getTaakLijst().iterator();
			while (taakIt.hasNext()) {
				Taak taak = taakIt.next();
				// het doel van plannigLijst is om meerdere planningen waarvan
				// de datum
				// hetzelfde is samen te voegen tot 1 planning, met de
				// gemeenschappelijke
				// datum en de som het aantal Uren
				List<Planning> planningLijst = new ArrayList<Planning>();
				Iterator<Planning> planningIt = taak.getPlanningLijst().iterator();
				while (planningIt.hasNext()) {
					Planning planning = planningIt.next();

					boolean isReedsInPlanningLijst = false;
					Iterator<Planning> planningLijstIter = planningLijst.iterator();
					while (planningLijstIter.hasNext()) {
						Planning factuurPlanning = planningLijstIter.next();
						String planningDatum = Datum.datumToString(planning.getBeginuur());
						String factuurPlanningDatum = Datum.datumToString(factuurPlanning.getBeginuur());
						if (planningDatum.equals(factuurPlanningDatum)) {
							double aantalUren = factuurPlanning.getAantalUren() + planning.getAantalUren();
							factuurPlanning.setAantalUren(aantalUren);
							isReedsInPlanningLijst = true;
						}
					}
					if (!isReedsInPlanningLijst) {
						planningLijst.add(planning);
					}
				}

				Iterator<Planning> planningLijstIter = planningLijst.iterator();
				while (planningLijstIter.hasNext()) {
					Planning planning = planningLijstIter.next();

					overzichtWerkuren.addCell(new Cell().add(taak.getTaakNaam()));
					String datum = Datum.datumToString(planning.getBeginuur());
					overzichtWerkuren.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(datum));
					overzichtWerkuren.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
							.add(String.valueOf(planning.getAantalUren())));
					overzichtWerkuren.addCell(
							new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(Constanten.Uurloon)));
					double prijs = planning.getAantalUren() * Constanten.Uurloon;
					overzichtWerkuren
							.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(prijs)));
					totaalPrijs += prijs;
				}
			}
			document.add(overzichtWerkuren);
			
			document.add(new Paragraph("\n"));
			
			// verplaatsingen
			Table verplaatsingen = new Table(new float[] { 1 });
			verplaatsingen.setWidthPercent(100);
			verplaatsingen.addCell(new Cell().add("Verplaatsingen"));
			document.add(verplaatsingen);
			Table verplaatsingTabel = new Table(colombreedtes);
			verplaatsingTabel.setWidthPercent(100);

			Cell[] verplaatsingHeader = new Cell[] {
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).setTextAlignment(TextAlignment.CENTER)
							.add("Datum"),
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).setTextAlignment(TextAlignment.CENTER)
							.add("# verplaatsingen"),
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).setTextAlignment(TextAlignment.CENTER)
							.add("# km"),
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).setTextAlignment(TextAlignment.CENTER)
							.add("Prijs/km"),
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).setTextAlignment(TextAlignment.CENTER)
							.add("Totaal") };
			for (Cell hcell : verplaatsingHeader) {
				verplaatsingTabel.addHeaderCell(hcell);
			}

			Iterator<Verplaatsing> verplaatsingIt = factuur.getVerplaatsingLijst().iterator();
			while (verplaatsingIt.hasNext()) {
				Verplaatsing verplaatsing = verplaatsingIt.next();

				if (verplaatsing.getOpdrachtId() == opdracht.getId()) {
					verplaatsingTabel.addCell(
							new Cell().setTextAlignment(TextAlignment.CENTER)
							.add(Datum.datumToString(verplaatsing.getDag())));
					verplaatsingTabel.addCell(
							new Cell().setTextAlignment(TextAlignment.CENTER)
							.add(String.valueOf(verplaatsing.getAantalVerplaatsingen())));
					verplaatsingTabel.addCell(
							new Cell().setTextAlignment(TextAlignment.CENTER)
							.add(String.valueOf(verplaatsing.getAantalKm())));
					verplaatsingTabel.addCell(
							new Cell().setTextAlignment(TextAlignment.CENTER)
							.add(String.valueOf(Constanten.KmVergoeding)));
					double prijs = verplaatsing.getAantalKm() * Constanten.KmVergoeding;
					verplaatsingTabel.addCell(
							new Cell().setTextAlignment(TextAlignment.CENTER)
							.add(String.valueOf(prijs)));
					totaalPrijs += prijs;
				}
			}
			document.add(verplaatsingTabel);
			
			document.add(new Paragraph("\n"));
			
			// materialen
			Table materialen = new Table(new float[] { 1 });
			materialen.setWidthPercent(100);
			materialen.addCell(new Cell().add("Gebruikte materialen"));
			document.add(materialen);
			
			Table materiaalTabel = new Table(colombreedtes);
			materiaalTabel.setWidthPercent(100);

			Cell[] materiaalHeader = new Cell[] {
					new Cell().setBackgroundColor(new DeviceGray(0.75f))
							.add("Soort"),
					new Cell().setBackgroundColor(new DeviceGray(0.75f))
							.add("Naam"),
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).setTextAlignment(TextAlignment.CENTER)
							.add("aantal"),
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).setTextAlignment(TextAlignment.CENTER)
							.add("eenheidsprijs"),
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).setTextAlignment(TextAlignment.CENTER)
							.add("Totaal") };
			for (Cell hcell : materiaalHeader) {
				materiaalTabel.addHeaderCell(hcell);
			}

			Iterator<Materiaal> materiaalIt = opdracht.getGebruiktMateriaalLijst().iterator();
			while (materiaalIt.hasNext()) {
				Materiaal materiaal = materiaalIt.next();

				materiaalTabel.addCell(
						new Cell().add(materiaal.getSoort()));
				materiaalTabel.addCell(
						new Cell().add(materiaal.getNaam()));
				String aantal = String.valueOf(materiaal.getHoeveelheid()).concat(" ").concat(materiaal.getEenheidsmaat());
				materiaalTabel.addCell(
						new Cell().setTextAlignment(TextAlignment.CENTER)
						.add(String.valueOf(aantal)));
				materiaalTabel.addCell(
						new Cell().setTextAlignment(TextAlignment.CENTER)
						.add(String.valueOf(materiaal.getEenheidsprijs())));
				double prijs = materiaal.getHoeveelheid() * materiaal.getEenheidsprijs();
				materiaalTabel.addCell(
						new Cell().setTextAlignment(TextAlignment.CENTER)
						.add(String.valueOf(prijs)));
				totaalPrijs += prijs;
				
			}
			document.add(materiaalTabel);
			
		} // einde opdrachtIterator
		
		document.add(new Paragraph("\n"));
		
		//totaal
		float[] colombreedte2 = {1, 5, 5};
		Table totaalTabel = new Table(colombreedte2);
		totaalTabel.setWidthPercent(100);
		
		totaalTabel.addCell(new Cell().add("Totaal (BTW Excl.)"));
		totaalTabel.addCell(new Cell());
		totaalTabel.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
				.add("€ ".concat(String.valueOf(totaalPrijs))));
		
		totaalTabel.addCell(new Cell().add("BTW "));
		int btw = 0;
		double btwPrijs = 0;
		if (factuur.isBtwAanrekenen()){
			btw = Constanten.Btw;
			btwPrijs = totaalPrijs * btw / 100;
			totaalTabel.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
					.add(String.valueOf(btw).concat(" %")));
		} else {
			totaalTabel.addCell(new Cell());
		}
		
		totaalTabel.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
				.add("€ ".concat(String.valueOf(btwPrijs))));
		
		totaalTabel.addCell(new Cell().setBackgroundColor(new DeviceGray(0.75f)).add("TE BETALEN"));
		totaalTabel.addCell(new Cell().setBackgroundColor(new DeviceGray(0.75f)));
		totaalTabel.addCell(new Cell().setBackgroundColor(new DeviceGray(0.75f)).setTextAlignment(TextAlignment.CENTER)
				.add("€ ".concat(String.valueOf(totaalPrijs + btwPrijs))));
		
		document.add(totaalTabel);
		
		document.add(new Paragraph("\n"));

		//bedrijfsadres
		Adres bedrijfAdres = factuur.getBedrijfsAdres();
		Text bedrijfsnaam = new Text(Constanten.Bedrijfsnaam.concat(" "));
		Text bedrijfadres = new Text(bedrijfAdres.getStraat().concat(" ")
				.concat(String.valueOf(bedrijfAdres.getNummer())).concat(" ")
				.concat(bedrijfAdres.getBus()).concat("\n")
				.concat(String.valueOf(bedrijfAdres.getPostcode())).concat(" ")
				.concat(bedrijfAdres.getPlaats()).concat("\n"));
		bedrijfadres.setFontSize(10);
		Text bedrijfBtwNummer = new Text("BTW: " + Constanten.BtwNr + "\n");
		bedrijfBtwNummer.setFontSize(10);
		Text bedrijfRekeningNr = new Text("Iban " + Constanten.Bankrekening + "\n");
		bedrijfRekeningNr.setFontSize(10);
		
		Table bedrijfsgegevensTabel = new Table(new float[] { 1 });
		Paragraph bedrijfsgegevens = new Paragraph();
		bedrijfsgegevens.add(bedrijfsnaam);
		bedrijfsgegevens.add(bedrijfadres);
		bedrijfsgegevens.add(bedrijfBtwNummer);
		bedrijfsgegevens.add(bedrijfRekeningNr);
		bedrijfsgegevensTabel.addCell(bedrijfsgegevens);
		document.add(bedrijfsgegevensTabel);
				
		// document sluiten
		document.close();
	}

	
}

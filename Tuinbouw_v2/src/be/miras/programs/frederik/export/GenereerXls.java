package be.miras.programs.frederik.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.model.Planning;
import be.miras.programs.frederik.model.Taak;
import be.miras.programs.frederik.util.Datum;

/**
 * @author Frederik Vanden Bussche
 * 
 * Genereer een Xls file
 */
public class GenereerXls {
	
	private static final Logger LOGGER = Logger.getLogger(GenereerXls.class);
	private static final String TAG = "GenereerXls: ";
	
	public GenereerXls(){
	}

	/**
	 * @param dest String : path + filename van de excel file
	 * @param excelData ExcelData : de data die in de excel file geplaatst wordt
	 * 
	 * maak dirs indien nodig
	 * creëer xls
	 * 
	 */
	public void genereer(String dest, ExcelData excelData) {

		File file = new File(dest);
		file.getParentFile().mkdirs();
		createXls(dest, excelData);
	}

	/**
	 * @param dest String : path + filename van de excel file
	 * @param excelData ExcelData : de data die in de excel file geplaatst wordt
	 * 
	 * 	creëer xls file
	 */
	private void createXls(String dest, ExcelData excelData) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(excelData.getKlantNaam());

		// VetFont:
		HSSFFont vetFont = workbook.createFont();
		// titleFont.setFontHeightInPoints((short) 30);
		vetFont.setBold(true);
		HSSFCellStyle vetStyle = workbook.createCellStyle();
		vetStyle.setFont(vetFont);

		HSSFRow rowhead = sheet.createRow(0);
		HSSFCell cell = rowhead.createCell(0);
		cell.setCellValue(excelData.getKlantNaam());
		cell.setCellStyle(vetStyle);

		int rijenteller = 2;

		//voor elke opdracht
		Iterator<Opdracht> opdrachtIt = excelData.getOpdrachtLijst().iterator();
		while (opdrachtIt.hasNext()) {
			Opdracht opdracht = opdrachtIt.next();

			HSSFRow opdrachtNaamRij = sheet.createRow(rijenteller);

			HSSFCell opdracht0cell = opdrachtNaamRij.createCell(0);
			opdracht0cell.setCellValue("opdracht: ");
			opdracht0cell.setCellStyle(vetStyle);

			HSSFCell opdracht1cell = opdrachtNaamRij.createCell(1);
			opdracht1cell.setCellValue(opdracht.getOpdrachtNaam());
			opdracht1cell.setCellStyle(vetStyle);

			rijenteller += 2;

			HSSFRow startDatumRij = sheet.createRow(rijenteller);
			startDatumRij.createCell(1).setCellValue("Start datum: ");
			String startDatumString = Datum.datumToString(opdracht.getStartDatum());
			startDatumRij.createCell(2).setCellValue(startDatumString);

			rijenteller++;

			HSSFRow eindDatumRij = sheet.createRow(rijenteller);
			eindDatumRij.createCell(1).setCellValue("Eind datum: ");
			String eindDatumString = Datum.datumToString(opdracht.getEindDatum());
			eindDatumRij.createCell(2).setCellValue(eindDatumString);

			rijenteller += 2;

			HSSFRow latlngRij = sheet.createRow(rijenteller);
			latlngRij.createCell(1).setCellValue("Latitude: ");
			latlngRij.createCell(2).setCellValue(opdracht.getLatitude());

			latlngRij.createCell(4).setCellValue("Longitude: ");
			latlngRij.createCell(5).setCellValue(opdracht.getLongitude());

			rijenteller++;
			
			String adresString = opdracht.getAdresString();
			HSSFRow adresRij = sheet.createRow(rijenteller);
			adresRij.createCell(1).setCellValue(adresString);

			rijenteller += 2;

			//voor elke taak uit elke opdracht
			Iterator<Taak> taakIt = opdracht.getTaakLijst().iterator();
			while (taakIt.hasNext()) {
				Taak taak = taakIt.next();

				HSSFRow taakNaamRij = sheet.createRow(rijenteller);
				HSSFCell taaknaam0cell = taakNaamRij.createCell(1);
				taaknaam0cell.setCellValue("Taaknaam: ");
				taaknaam0cell.setCellStyle(vetStyle);

				HSSFCell taaknaam1cell = taakNaamRij.createCell(2);
				taaknaam1cell.setCellValue(taak.getTaakNaam());
				taaknaam1cell.setCellStyle(vetStyle);

				rijenteller++;

				HSSFRow opmerkingrij = sheet.createRow(rijenteller);
				opmerkingrij.createCell(1).setCellValue("opmerking: ");
				opmerkingrij.createCell(2).setCellValue(taak.getOpmerking());

				rijenteller++;

				HSSFRow vooruitgangrij = sheet.createRow(rijenteller);
				vooruitgangrij.createCell(1).setCellValue("vooruitgang: ");
				vooruitgangrij.createCell(2).setCellValue(taak.getVooruitgangPercentage());
				vooruitgangrij.createCell(3).setCellValue("%");

				rijenteller++;

				HSSFRow statusrij = sheet.createRow(rijenteller);
				statusrij.createCell(1).setCellValue("status: ");
				statusrij.createCell(2).setCellValue(taak.getStatus());

				rijenteller += 2;

				HSSFRow planningRij = sheet.createRow(rijenteller);
				HSSFCell planningcell = planningRij.createCell(2);
				planningcell.setCellValue("Prestaties: ");
				planningcell.setCellStyle(vetStyle);

				rijenteller ++;

				HSSFRow planningTitelsRij = sheet.createRow(rijenteller);
				planningTitelsRij.createCell(2).setCellValue("werknemer");
				planningTitelsRij.createCell(3).setCellValue("datum");
				planningTitelsRij.createCell(4).setCellValue("beginuur");
				planningTitelsRij.createCell(5).setCellValue("einduur");
				planningTitelsRij.createCell(6).setCellValue("aantal uren");

				rijenteller++;
				
				double totaalAantalUren = 0;

				// voor elke planning uit elke taak van elke opdracht
				Iterator<Planning> planningIt = taak.getPlanningLijst().iterator();
				while (planningIt.hasNext()) {
					
					Planning planning = planningIt.next();

					HSSFRow planningDataRij = sheet.createRow(rijenteller);
					planningDataRij.createCell(2).setCellValue(planning.getWerknemer());
					if (planning.getBeginuur() != null) {
						String datum = Datum.datumToString(planning.getBeginuur());
						String tijdstip = Datum.tijdstipToString(planning.getBeginuur());
						planningDataRij.createCell(3).setCellValue(datum);
						planningDataRij.createCell(4).setCellValue(tijdstip);

					}
					if (planning.getEinduur() != null) {
						String tijdstip = Datum.tijdstipToString(planning.getEinduur());
						planningDataRij.createCell(5).setCellValue(tijdstip);

					}
					if (planning.getEinduur() != null && planning.getEinduur().after(planning.getBeginuur())){
						double aantalUren = planning.getAantalUren();
						totaalAantalUren += aantalUren;
						planningDataRij.createCell(6).setCellValue(aantalUren);
					} 
					
					rijenteller++;
					
				}
				HSSFRow totaalUrenRij = sheet.createRow(rijenteller);
				HSSFCell totaalTitelCell = totaalUrenRij.createCell(5);
				totaalTitelCell.setCellValue("Totaal" );
				totaalTitelCell.setCellStyle(vetStyle);
				
				HSSFCell totaalCell = totaalUrenRij.createCell(6);
				totaalCell.setCellValue(totaalAantalUren );
				totaalCell.setCellStyle(vetStyle);
				
				HSSFCell totaalUurCell = totaalUrenRij.createCell(7);
				totaalUurCell.setCellValue("Uur" );
				totaalUurCell.setCellStyle(vetStyle);
				
				rijenteller += 2;
			}
		}
		sheet.createRow(1);

		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(dest);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("IOException: " + TAG + " workbook.write(fileOut); fileOut.close(); ", e);
		}
	}

	
}

package be.miras.programs.frederik.model;

import java.util.Date;

/*
 * Een taak zoals weergegeven in de takenlijst van een werknemer in personeelsbeheer.
 * En dit op de pagina PersoneelTakenlijst.jsp
 */
public class PersoneelbeheerTakenlijstTaak {

	
	private int dbWerknemerOpdrachtTaakId;
	private Date datum;
	private String klantnaam;
	private String opdrachtnaam;
	private String taaknaam;

	public int getDbWerknemerOpdrachtTaakId() {
		return dbWerknemerOpdrachtTaakId;
	}

	public void setDbWerknemerOpdrachtTaakId(int dbWerknemerOpdrachtTaakId) {
		this.dbWerknemerOpdrachtTaakId = dbWerknemerOpdrachtTaakId;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public String getKlantnaam() {
		return klantnaam;
	}

	public void setKlantnaam(String klantnaam) {
		this.klantnaam = klantnaam;
	}

	public String getOpdrachtnaam() {
		return opdrachtnaam;
	}

	public void setOpdrachtnaam(String opdrachtnaam) {
		this.opdrachtnaam = opdrachtnaam;
	}

	public String getTaaknaam() {
		return taaknaam;
	}

	public void setTaaknaam(String taaknaam) {
		this.taaknaam = taaknaam;
	}

	
	
}

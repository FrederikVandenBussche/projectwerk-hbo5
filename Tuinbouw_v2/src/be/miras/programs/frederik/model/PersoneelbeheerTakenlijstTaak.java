package be.miras.programs.frederik.model;

import java.util.Date;

/**
 * @author Frederik Vanden Bussche
 * 
 *         view model van een personeelbeheerTakenlijstTaak Dat is een taak
 *         zoals weergegeven in de takenlijst van een werknemer in
 *         personeelsbeheer. En dit op de pagina PersoneelTakenlijst.jsp
 *
 */
public class PersoneelbeheerTakenlijstTaak {

	private int dbWerknemerOpdrachtTaakId;
	private Date datum;
	private String klantnaam;
	private String opdrachtnaam;
	private String taaknaam;

	/**
	 * @return the dbWerknemerOpdrachtTaakId int
	 */
	public int getDbWerknemerOpdrachtTaakId() {
		return dbWerknemerOpdrachtTaakId;
	}

	/**
	 * @param dbWerknemerOpdrachtTaakId
	 *            the dbWerknemerOpdrachtTaakId to set int
	 */
	public void setDbWerknemerOpdrachtTaakId(int dbWerknemerOpdrachtTaakId) {
		this.dbWerknemerOpdrachtTaakId = dbWerknemerOpdrachtTaakId;
	}

	/**
	 * @return the datum Date
	 */
	public Date getDatum() {
		return datum;
	}

	/**
	 * @param datum
	 *            the datum to set Date
	 */
	public void setDatum(Date datum) {
		this.datum = datum;
	}

	/**
	 * @return the klantnaam String
	 */
	public String getKlantnaam() {
		return klantnaam;
	}

	/**
	 * @param klantnaam
	 *            the klantnaam to set String
	 */
	public void setKlantnaam(String klantnaam) {
		this.klantnaam = klantnaam;
	}

	/**
	 * @return the opdrachtnaam String
	 */
	public String getOpdrachtnaam() {
		return opdrachtnaam;
	}

	/**
	 * @param opdrachtnaam
	 *            the opdrachtnaam to set String
	 */
	public void setOpdrachtnaam(String opdrachtnaam) {
		this.opdrachtnaam = opdrachtnaam;
	}

	/**
	 * @return the taaknaam String
	 */
	public String getTaaknaam() {
		return taaknaam;
	}

	/**
	 * @param taaknaam
	 *            the taaknaam to set String
	 */
	public void setTaaknaam(String taaknaam) {
		this.taaknaam = taaknaam;
	}

	
}

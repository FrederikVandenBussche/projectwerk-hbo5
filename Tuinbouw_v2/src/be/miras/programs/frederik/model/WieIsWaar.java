/**
 * 
 */
package be.miras.programs.frederik.model;

import java.util.Date;

/**
 * @author Frederik
 *
 *         Een duidelijk overzicht van wie waar met wat bezit is
 */
public class WieIsWaar {
	
	private Date startuur;
	private Date einduur;
	private String werknemer;
	private String klantNaam;
	private String straatEnNummer;
	private String postcodeEnPlaats;
	private String opdracht;
	private String taak;
	private String status;
	
	
	public WieIsWaar(){
	}

	/**
	 * @return the startuur Date
	 */
	public Date getStartuur() {
		return startuur;
	}

	/**
	 * @param startuur Date
	 *            the startuur to set
	 */
	public void setStartuur(Date startuur) {
		this.startuur = startuur;
	}

	/** 
	 * @return the einduur Date
	 */
	public Date getEinduur() {
		return einduur;
	}

	/**
	 * @param einduur Date
	 *            the einduur to set 
	 */
	public void setEinduur(Date einduur) {
		this.einduur = einduur;
	}

	/**
	 * @return the werknemer String
	 */
	public String getWerknemer() {
		return werknemer;
	}

	/**
	 * @param werknemer String
	 *            the werknemer to set String
	 */
	public void setWerknemer(String werknemer) {
		this.werknemer = werknemer;
	}

	/**
	 * @return the klantNaam String
	 */
	public String getKlantNaam() {
		return klantNaam;
	}

	/**
	 * @param klantNaam the klantNaam to set String
	 */
	public void setKlantNaam(String klantNaam) {
		this.klantNaam = klantNaam;
	}

	/**
	 * @return the straatNummer String
	 */
	public String getStraatEnNummer() {
		return straatEnNummer;
	}

	/**
	 * @param straatNummer the straatNummer to set String
	 */
	public void setStraatEnNummer(String straatEnNummer) {
		this.straatEnNummer = straatEnNummer;
	}

	/**
	 * @return the postcodePlaats String
	 */
	public String getPostcodeEnPlaats() {
		return postcodeEnPlaats;
	}

	/**
	 * @param postcodePlaats the postcodePlaats to set String
	 */
	public void setPostcodeEnPlaats(String postcodeEnPlaats) {
		this.postcodeEnPlaats = postcodeEnPlaats;
	}

	/**
	 * @return the opdracht String
	 */
	public String getOpdracht() {
		return opdracht;
	}

	/**
	 * @param opdracht String
	 *            the opdracht to set
	 */
	public void setOpdracht(String opdracht) {
		this.opdracht = opdracht;
	}

	/**
	 * @return the taak String
	 */
	public String getTaak() {
		return taak;
	}

	/**
	 * @param taak String
	 *            the taak to set
	 */
	public void setTaak(String taak) {
		this.taak = taak;
	}

	/**
	 * @return the status String
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set String
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	
}

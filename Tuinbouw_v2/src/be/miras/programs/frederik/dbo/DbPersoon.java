package be.miras.programs.frederik.dbo;

import java.util.Date;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel persoon
 */
public class DbPersoon {
	private int id;
	private String naam;
	private String voornaam;
	private Date geboortedatum;

	/**
	 * @return the id int
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the naam String
	 */
	public String getNaam() {
		return naam;
	}

	/**
	 * @param naam
	 *            the naam to set String
	 */
	public void setNaam(String naam) {
		this.naam = naam;
	}

	/**
	 * @return the voornaam String
	 */
	public String getVoornaam() {
		return voornaam;
	}

	/**
	 * @param voornaam
	 *            the voornaam to set String
	 */
	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}

	/**
	 * @return the geboortedatum Date
	 */
	public Date getGeboortedatum() {
		return geboortedatum;
	}

	/**
	 * @param geboortedatum
	 *            the geboortedatum to set Date
	 */
	public void setGeboortedatum(Date geboortedatum) {
		this.geboortedatum = geboortedatum;
	}

	
}

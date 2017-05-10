package be.miras.programs.frederik.dbo;

import java.util.Date;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel opdracht
 */
public class DbOpdracht {
	private int id;
	private int klantId;
	private int klantAdresId;
	private String naam;
	private double latitude;
	private double longitude;
	private Date startdatum;;
	private Date einddatum;

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
	 * @return the klantId int
	 */
	public int getKlantId() {
		return klantId;
	}

	/**
	 * @param klantId
	 *            the klantId to set int
	 */
	public void setKlantId(int klantId) {
		this.klantId = klantId;
	}

	/**
	 * @return the klantAdresId int
	 */
	public int getKlantAdresId() {
		return klantAdresId;
	}

	/**
	 * @param klantAdresId
	 *            the klantAdresId to set int
	 */
	public void setKlantAdresId(int klantAdresId) {
		this.klantAdresId = klantAdresId;
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
	 * @return the latitude double
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set double
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude double
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set double
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the startdatum Date
	 */
	public Date getStartdatum() {
		return startdatum;
	}

	/**
	 * @param startdatum
	 *            the startdatum to set Date
	 */
	public void setStartdatum(Date startdatum) {
		this.startdatum = startdatum;
	}

	/**
	 * @return the einddatum Date
	 */
	public Date getEinddatum() {
		return einddatum;
	}

	/**
	 * @param einddatum
	 *            the einddatum to set Date
	 */
	public void setEinddatum(Date einddatum) {
		this.einddatum = einddatum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DbOpdracht [id=" + id + ", klantId=" + klantId + ", klantAdresId=" + klantAdresId + ", naam=" + naam
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", startdatum=" + startdatum + ", einddatum="
				+ einddatum + "]";
	}

	
}

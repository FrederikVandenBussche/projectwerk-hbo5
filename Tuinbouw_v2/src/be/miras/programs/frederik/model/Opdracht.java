package be.miras.programs.frederik.model;

import java.util.Date;
import java.util.List;

/**
 * @author Frederik Vanden Bussche
 * 
 *         view model van een opdracht
 *
 */
public class Opdracht implements Ivergelijk {

	private int id;
	private int klantId;
	private int klantAdresId;
	private String klantNaam;
	private String opdrachtNaam;
	private Date startDatum;
	private Date eindDatum;
	private double latitude;
	private double longitude;
	private List<Taak> taakLijst;
	private List<Materiaal> gebruiktMateriaalLijst;

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
	 * @return the klantNaam String
	 */
	public String getKlantNaam() {
		return klantNaam;
	}

	/**
	 * @param klantNaam
	 *            the klantNaam to set String
	 */
	public void setKlantNaam(String klantNaam) {
		this.klantNaam = klantNaam;
	}

	/**
	 * @return the opdrachtNaam String
	 */
	public String getOpdrachtNaam() {
		return opdrachtNaam;
	}

	/**
	 * @param opdrachtNaam
	 *            the opdrachtNaam to set String
	 */
	public void setOpdrachtNaam(String opdrachtNaam) {
		this.opdrachtNaam = opdrachtNaam;
	}

	/**
	 * @return the startDatum Date
	 */
	public Date getStartDatum() {
		return startDatum;
	}

	/**
	 * @param startDatum
	 *            the startDatum to set Date
	 */
	public void setStartDatum(Date startDatum) {
		this.startDatum = startDatum;
	}

	/**
	 * @return the eindDatum Date
	 */
	public Date getEindDatum() {
		return eindDatum;
	}

	/**
	 * @param eindDatum
	 *            the eindDatum to set Date
	 */
	public void setEindDatum(Date eindDatum) {
		this.eindDatum = eindDatum;
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
	 * @return the taakLijst List<Taak>
	 */
	public List<Taak> getTaakLijst() {
		return taakLijst;
	}

	/**
	 * @param taakLijst
	 *            the taakLijst to set List<Taak>
	 */
	public void setTaakLijst(List<Taak> taakLijst) {
		this.taakLijst = taakLijst;
	}

	/**
	 * @return the gebruiktMateriaalLijst List<Materiaal>
	 */
	public List<Materiaal> getGebruiktMateriaalLijst() {
		return gebruiktMateriaalLijst;
	}

	/**
	 * @param gebruiktMateriaalLijst
	 *            the gebruiktMateriaalLijst to set List<Materiaal>
	 */
	public void setGebruiktMateriaalLijst(List<Materiaal> gebruiktMateriaalLijst) {
		this.gebruiktMateriaalLijst = gebruiktMateriaalLijst;
	}

	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		Opdracht o1 = (Opdracht) o;
		Opdracht o2 = (Opdracht) p;
		if (o1.getKlantNaam().equals(o2.getKlantNaam()) && o1.getOpdrachtNaam().equals(o2.getOpdrachtNaam())
				&& o1.getStartDatum().equals(o2.getStartDatum()) && o1.getEindDatum().equals(o2.getEindDatum())
				&& Double.compare(o1.latitude, o2.latitude) == 0 && Double.compare(o1.longitude, o2.longitude) == 0) {
			isVerschillend = false;
		}
		return isVerschillend;
	}

	
}

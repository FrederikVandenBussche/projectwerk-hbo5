package be.miras.programs.frederik.dbo;

import java.util.Date;

public class DbOpdracht {
	private int id;
	private int klantId;
	private int klantAdresId;
	private String naam;
	private double latitude;
	private double longitude;
	private Date startdatum;;
	private Date einddatum;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getKlantId() {
		return klantId;
	}

	public void setKlantId(int klantId) {
		this.klantId = klantId;
	}

	public int getKlantAdresId() {
		return klantAdresId;
	}

	public void setKlantAdresId(int klantAdresId) {
		this.klantAdresId = klantAdresId;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Date getStartdatum() {
		return startdatum;
	}

	public void setStartdatum(Date startdatum) {
		this.startdatum = startdatum;
	}

	public Date getEinddatum() {
		return einddatum;
	}

	public void setEinddatum(Date einddatum) {
		this.einddatum = einddatum;
	}

}

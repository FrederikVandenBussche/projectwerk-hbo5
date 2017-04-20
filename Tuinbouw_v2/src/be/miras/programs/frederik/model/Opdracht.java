package be.miras.programs.frederik.model;

import java.util.Date;
import java.util.List;

public class Opdracht implements Ivergelijk {

	private int id;
	private int klantId;
	private String klantNaam;
	private String opdrachtNaam;
	private Date startDatum;
	private Date eindDatum;
	private double latitude;
	private double longitude;
	private List<Taak> taakLijst;
	private List<Materiaal> gebruiktMateriaalLijst;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getKlantId() {
		return klantId;
	}

	public void setklantId(int klantId) {
		this.klantId = klantId;
	}

	public String getKlantNaam() {
		return klantNaam;
	}

	public void setKlantNaam(String klantNaam) {
		this.klantNaam = klantNaam;
	}

	public String getOpdrachtNaam() {
		return opdrachtNaam;
	}

	public void setOpdrachtNaam(String opdrachtNaam) {
		this.opdrachtNaam = opdrachtNaam;
	}

	public Date getStartDatum() {
		return startDatum;
	}

	public void setStartDatum(Date startDatum) {
		this.startDatum = startDatum;
	}

	public Date getEindDatum() {
		return eindDatum;
	}

	public void setEindDatum(Date eindDatum) {
		this.eindDatum = eindDatum;
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

	public List<Taak> getTaakLijst() {
		return taakLijst;
	}

	public void setTaakLijst(List<Taak> taakLijst) {
		this.taakLijst = taakLijst;
	}
	
	public List<Materiaal> getGebruiktMateriaalLijst() {
		return gebruiktMateriaalLijst;
	}

	public void setGebruiktMateriaalLijst(List<Materiaal> gebruiktMateriaalLijst) {
		this.gebruiktMateriaalLijst = gebruiktMateriaalLijst;
	}

	public void setKlantId(int klantId) {
		this.klantId = klantId;
	}


	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		Opdracht o1 = (Opdracht) o;
		Opdracht o2 = (Opdracht) p;
		if (o1.getKlantNaam().equals(o2.getKlantNaam())
				&& o1.getOpdrachtNaam().equals(o2.getOpdrachtNaam()) 
				&& o1.getStartDatum().equals(o2.getStartDatum())
				&& o1.getEindDatum().equals(o2.getEindDatum())
				&& Double.compare(o1.latitude, o2.latitude) == 0
				&& Double.compare(o1.longitude, o2.longitude) == 0) {
			isVerschillend = false;
		}
		return isVerschillend;
	}

}

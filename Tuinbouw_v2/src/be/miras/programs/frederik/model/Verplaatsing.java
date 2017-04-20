package be.miras.programs.frederik.model;

import java.util.Date;

public class Verplaatsing {
	private Date dag;
	private int werknemerId;
	private int opdrachtId;
	private double aantalKm;
	private int aantalVerplaatsingen;

	public Verplaatsing() {
		super();
	}

	public Date getDag() {
		return dag;
	}

	public void setDag(Date dag) {
		this.dag = dag;
	}

	public int getWerknemerId() {
		return werknemerId;
	}

	public void setWerknemerId(int werknemerId) {
		this.werknemerId = werknemerId;
	}

	public int getOpdrachtId() {
		return opdrachtId;
	}

	public void setOpdrachtId(int opdrachtId) {
		this.opdrachtId = opdrachtId;
	}

	public double getAantalKm() {
		return aantalKm;
	}

	public void setAantalKm(double aantalKm) {
		this.aantalKm = aantalKm;
	}

	public int getAantalVerplaatsingen() {
		return aantalVerplaatsingen;
	}

	public void setAantalVerplaatsingen(int aantalVerplaatsingen) {
		this.aantalVerplaatsingen = aantalVerplaatsingen;
	}

}

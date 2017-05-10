package be.miras.programs.frederik.model;

import java.util.Date;

/**
 * @author Frederik Vanden Bussche
 * 
 *         de verplaatsing van een werknemer
 *
 */
public class Verplaatsing {
	private Date dag;
	private int werknemerId;
	private int opdrachtId;
	private double aantalKm;
	private int aantalVerplaatsingen;

	public Verplaatsing() {
		super();
	}

	/**
	 * @return the dag Date
	 */
	public Date getDag() {
		return dag;
	}

	/**
	 * @param dag
	 *            the dag to set Date
	 */
	public void setDag(Date dag) {
		this.dag = dag;
	}

	/**
	 * @return the werknemerId int
	 */
	public int getWerknemerId() {
		return werknemerId;
	}

	/**
	 * @param werknemerId
	 *            the werknemerId to set int
	 */
	public void setWerknemerId(int werknemerId) {
		this.werknemerId = werknemerId;
	}

	/**
	 * @return the opdrachtId int
	 */
	public int getOpdrachtId() {
		return opdrachtId;
	}

	/**
	 * @param opdrachtId
	 *            the opdrachtId to set int
	 */
	public void setOpdrachtId(int opdrachtId) {
		this.opdrachtId = opdrachtId;
	}

	/**
	 * @return the aantalKm double
	 */
	public double getAantalKm() {
		return aantalKm;
	}

	/**
	 * @param aantalKm
	 *            the aantalKm to set double
	 */
	public void setAantalKm(double aantalKm) {
		this.aantalKm = aantalKm;
	}

	/**
	 * @return the aantalVerplaatsingen int
	 */
	public int getAantalVerplaatsingen() {
		return aantalVerplaatsingen;
	}

	/**
	 * @param aantalVerplaatsingen
	 *            the aantalVerplaatsingen to set int
	 */
	public void setAantalVerplaatsingen(int aantalVerplaatsingen) {
		this.aantalVerplaatsingen = aantalVerplaatsingen;
	}

	
}

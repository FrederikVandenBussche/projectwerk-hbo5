package be.miras.programs.frederik.model;

import java.util.Date;

/**
 * @author Frederik Vanden Bussche
 * 
 *         view model van een planning
 *
 */
public class Planning {
	private int id;
	private String werknemer;
	private int werknemerId;
	private Date beginuur;
	private Date einduur;
	private int isAanwezig;
	private double aantalUren;
	private double aantalKm;

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
	 * @return the werknemer String
	 */
	public String getWerknemer() {
		return werknemer;
	}

	/**
	 * @param werknemer
	 *            the werknemer to set String
	 */
	public void setWerknemer(String werknemer) {
		this.werknemer = werknemer;
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
	 * @return the beginuur Date
	 */
	public Date getBeginuur() {
		return beginuur;
	}

	/**
	 * @param beginuur
	 *            the beginuur to set Date
	 */
	public void setBeginuur(Date beginuur) {
		this.beginuur = beginuur;
	}

	/**
	 * @return the einduur Date
	 */
	public Date getEinduur() {
		return einduur;
	}

	/**
	 * @param einduur
	 *            the einduur to set Date
	 */
	public void setEinduur(Date einduur) {
		this.einduur = einduur;
	}

	/**
	 * @return the isAanwezig int
	 */
	public int getIsAanwezig() {
		return isAanwezig;
	}

	/**
	 * @param isAanwezig
	 *            the isAanwezig to set int
	 */
	public void setIsAanwezig(int isAanwezig) {
		this.isAanwezig = isAanwezig;
	}

	/**
	 * @return the aantalUren double
	 */
	public double getAantalUren() {
		return aantalUren;
	}

	/**
	 * @param aantalUren
	 *            the aantalUren to set doble
	 */
	public void setAantalUren(double aantalUren) {
		this.aantalUren = aantalUren;
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
	 *  het aantal uren is het einduur min het beginuur
	 */
	public void setAantalUren() {
		if (this.einduur == null || this.beginuur == null) {
			this.aantalUren = Double.MIN_VALUE;
		} else {
			double aantalMilliseconden = this.einduur.getTime() - this.beginuur.getTime();
			double uren = ((aantalMilliseconden / 1000) / 60) / 60;
			int afgerond2decimalen = (int) (uren * 100);
			this.aantalUren = (double) afgerond2decimalen / 100;
		}
	}

	
}

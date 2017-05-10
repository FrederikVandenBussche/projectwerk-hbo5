package be.miras.programs.frederik.dbo;

import java.util.Date;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel werknemer
 */
public class DbWerknemer {
	private int id;
	private double loon;
	private Date aanwervingsdatum;
	private int persoonId;

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
	 * @return the loon double
	 */
	public double getLoon() {
		return loon;
	}

	/**
	 * @param loon
	 *            the loon to set double
	 */
	public void setLoon(double loon) {
		this.loon = loon;
	}

	/**
	 * @return the aanwervingsdatum Date
	 */
	public Date getAanwervingsdatum() {
		return aanwervingsdatum;
	}

	/**
	 * @param aanwervingsdatum
	 *            the aanwervingsdatum to set Date
	 */
	public void setAanwervingsdatum(Date aanwervingsdatum) {
		this.aanwervingsdatum = aanwervingsdatum;
	}

	/**
	 * @return the persoonId int
	 */
	public int getPersoonId() {
		return persoonId;
	}

	/**
	 * @param persoonId
	 *            the persoonId to set int
	 */
	public void setPersoonId(int persoonId) {
		this.persoonId = persoonId;
	}

}

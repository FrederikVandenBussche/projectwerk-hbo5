package be.miras.programs.frederik.dbo;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel opdracht_materiaal
 */
public class DbOpdrachtMateriaal {
	private int id;
	private int opdrachtId;
	private int materiaalId;
	private double verbruik;

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
	 * @return the materiaalId int
	 */
	public int getMateriaalId() {
		return materiaalId;
	}

	/**
	 * @param materiaalId
	 *            the materiaalId to set int
	 */
	public void setMateriaalId(int materiaalId) {
		this.materiaalId = materiaalId;
	}

	/**
	 * @return the verbruik double
	 */
	public double getVerbruik() {
		return verbruik;
	}

	/**
	 * @param verbruik
	 *            the verbruik to set double
	 */
	public void setVerbruik(double verbruik) {
		this.verbruik = verbruik;
	}

	
}

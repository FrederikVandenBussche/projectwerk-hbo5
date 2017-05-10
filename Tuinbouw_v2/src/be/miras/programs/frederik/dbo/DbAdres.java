package be.miras.programs.frederik.dbo;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel adres
 */
public class DbAdres {
	private int id;
	private int straatId;
	private int gemeenteId;
	private int huisnummer;
	private String bus;

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
	 * @return the straatId int
	 */
	public int getStraatId() {
		return straatId;
	}

	/**
	 * @param straatId
	 *            the straatId to set int
	 */
	public void setStraatId(int straatId) {
		this.straatId = straatId;
	}

	/**
	 * @return the gemeenteId int
	 */
	public int getGemeenteId() {
		return gemeenteId;
	}

	/**
	 * @param gemeenteId
	 *            the gemeenteId to set int
	 */
	public void setGemeenteId(int gemeenteId) {
		this.gemeenteId = gemeenteId;
	}

	/**
	 * @return the huisnummer int
	 */
	public int getHuisnummer() {
		return huisnummer;
	}

	/**
	 * @param huisnummer
	 *            the huisnummer to set int
	 */
	public void setHuisnummer(int huisnummer) {
		this.huisnummer = huisnummer;
	}

	/**
	 * @return the bus String
	 */
	public String getBus() {
		return bus;
	}

	/**
	 * @param bus
	 *            the bus to set String
	 */
	public void setBus(String bus) {
		this.bus = bus;
	}

	
}

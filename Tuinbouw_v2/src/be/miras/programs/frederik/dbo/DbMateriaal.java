package be.miras.programs.frederik.dbo;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel materiaal
 */
public class DbMateriaal {
	private int id;
	private String naam;
	private int typeMateriaalId;
	private double eenheidsprijs;
	private String eenheid;

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
	 * @return the typeMateriaalId int
	 */
	public int getTypeMateriaalId() {
		return typeMateriaalId;
	}

	/**
	 * @param typeMateriaalId
	 *            the typeMateriaalId to set int
	 */
	public void setTypeMateriaalId(int typeMateriaalId) {
		this.typeMateriaalId = typeMateriaalId;
	}

	/**
	 * @return the eenheidsprijs double
	 */
	public double getEenheidsprijs() {
		return eenheidsprijs;
	}

	/**
	 * @param eenheidsprijs
	 *            the eenheidsprijs to set double
	 */
	public void setEenheidsprijs(double eenheidsprijs) {
		this.eenheidsprijs = eenheidsprijs;
	}

	/**
	 * @return the eenheid String
	 */
	public String getEenheid() {
		return eenheid;
	}

	/**
	 * @param eenheid
	 *            the eenheid to set String
	 */
	public void setEenheid(String eenheid) {
		this.eenheid = eenheid;
	}

	
}

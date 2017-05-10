package be.miras.programs.frederik.dbo;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel gemeente
 */
public class DbGemeente {
	private int id;
	private String naam;
	private int postcode;

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
	 * @return the postcode int
	 */
	public int getPostcode() {
		return postcode;
	}

	/**
	 * @param postcode
	 *            the postcode to set int
	 */
	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}

	
}

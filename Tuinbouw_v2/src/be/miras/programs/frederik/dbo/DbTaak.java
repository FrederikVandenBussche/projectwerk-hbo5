package be.miras.programs.frederik.dbo;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel taak
 */
public class DbTaak {
	private int id;
	private String naam;
	private int visible;

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
	 * @return the visible int
	 */
	public int getVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set int
	 */
	public void setVisible(int visible) {
		this.visible = visible;
	}

	
}

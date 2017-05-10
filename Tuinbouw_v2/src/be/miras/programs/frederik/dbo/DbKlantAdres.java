package be.miras.programs.frederik.dbo;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel klant_adres
 */
public class DbKlantAdres {
	private int id;
	private int klantId;
	private int adresId;

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
	 * @return the klantId int
	 */
	public int getKlantId() {
		return klantId;
	}

	/**
	 * @param klantId
	 *            the klantId to set int
	 */
	public void setKlantId(int klantId) {
		this.klantId = klantId;
	}

	/**
	 * @return the adresId int
	 */
	public int getAdresId() {
		return adresId;
	}

	/**
	 * @param adresId
	 *            the adresId to set int
	 */
	public void setAdresId(int adresId) {
		this.adresId = adresId;
	}

	
}

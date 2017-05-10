package be.miras.programs.frederik.dbo;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel persoon_adres
 */
public class DbPersoonAdres {
	private int persoonId;
	private int adresId;
	private int id;

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

	
}

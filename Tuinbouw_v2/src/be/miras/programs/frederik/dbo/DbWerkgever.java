package be.miras.programs.frederik.dbo;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel werkgever
 */
public class DbWerkgever {
	private int id;
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

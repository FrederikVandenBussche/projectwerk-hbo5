package be.miras.programs.frederik.dbo;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel opdracht_taak
 */
public class DbOpdrachtTaak {

	private int id;
	private int opdrachtId;
	private int taakId;
	private int vooruitgangId;
	private String opmerking;

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
	 * @return the taakId int
	 */
	public int getTaakId() {
		return taakId;
	}

	/**
	 * @param taakId
	 *            the taakId to set int
	 */
	public void setTaakId(int taakId) {
		this.taakId = taakId;
	}

	/**
	 * @return the vooruitgangId int
	 */
	public int getVooruitgangId() {
		return vooruitgangId;
	}

	/**
	 * @param vooruitgangId
	 *            the vooruitgangId to set int
	 */
	public void setVooruitgangId(int vooruitgangId) {
		this.vooruitgangId = vooruitgangId;
	}

	/**
	 * @return the opmerking String
	 */
	public String getOpmerking() {
		return opmerking;
	}

	/**
	 * @param opmerking
	 *            the opmerking to set String
	 */
	public void setOpmerking(String opmerking) {
		this.opmerking = opmerking;
	}

	
}
